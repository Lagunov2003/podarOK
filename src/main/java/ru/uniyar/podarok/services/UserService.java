package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.NotValidCodeException;
import ru.uniyar.podarok.exceptions.UserAlreadyExistException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.UserRepository;
import ru.uniyar.podarok.utils.Builders.UserBuilder;
import ru.uniyar.podarok.utils.JwtTokenUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления пользователями и выполнения связанных операций.
 * Предоставляет методы для работы с профилем пользователя, авторизацией,
 * регистрацией и управления избранными подарками.
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private EmailService emailService;
    private ConfirmationCodeService confirmationCodeService;
    private JwtTokenUtils jwtTokenUtils;
    private PasswordEncoder passwordEncoder;

    /**
     * Получает текущего аутентифицированного пользователя.
     *
     * @return объект {@link User} текущего аутентифицированного пользователя
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws UserNotFoundException если пользователь не найден
     */
    public User getCurrentAuthenticationUser() throws UserNotAuthorizedException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new UserNotAuthorizedException("Пользователь не авторизован!");
        }

        return userRepository.findUserByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    /**
     * Ищет пользователя по email.
     *
     * @param email email пользователя
     * @return объект {@link User} пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с email %s не найден", email))
        );
    }

    /**
     * Создает нового пользователя.
     *
     * @param registrationUserDto объект с данными для регистрации
     * @return объект {@link User} нового пользователя
     * @throws UserAlreadyExistException если пользователь с таким email уже существует
     */
    @Transactional
    public User createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExistException {
        if (userRepository.findUserByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Пользователь уже существует!");
        }

        User user = new UserBuilder()
                .setEmail(registrationUserDto.getEmail())
                .setPassword(passwordEncoder.encode(registrationUserDto.getPassword()))
                .setFirstName(registrationUserDto.getFirstName())
                .setRegistrationDate(LocalDate.now())
                .setGender(true)
                .setRoles(List.of(roleService.getUserRole()))
                .build();

        emailService.sendWelcomeLetter(registrationUserDto.getEmail(), registrationUserDto.getFirstName());

        return userRepository.save(user);
    }

    /**
     * Загружает пользователя по его email для аутентификации.
     *
     * @param email email пользователя
     * @return объект {@link UserDetails} для аутентификации
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = findByEmail(email);

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(String.format("Пользователь с email %s не найден!", email), e);
        }
    }

    /**
     * Получает профиль текущего аутентифицированного пользователя.
     *
     * @return объект {@link CurrentUserDto} с данными пользователя
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws UserNotFoundException если пользователь не найден
     */
    public CurrentUserDto getCurrentUserProfile() throws UserNotAuthorizedException, UserNotFoundException {
        User currentUser = getCurrentAuthenticationUser();

        return new CurrentUserDto(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getDateOfBirth(),
                currentUser.getRegistrationDate(),
                currentUser.getGender(),
                currentUser.getPhoneNumber()
        );
    }

    /**
     * Обновляет профиль текущего аутентифицированного пользователя.
     *
     * @param updateUserDto объект с новыми данными для обновления профиля
     * @return объект {@link CurrentUserDto} с обновленными данными
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     */
    @Transactional
    public CurrentUserDto updateUserProfile(UpdateUserDto updateUserDto)
            throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = getCurrentAuthenticationUser();

        if (updateUserDto.getFirstName() != null) {
            currentUser.setFirstName(updateUserDto.getFirstName());
        }

        if (updateUserDto.getLastName() != null) {
            currentUser.setLastName(updateUserDto.getLastName());
        }

        if (updateUserDto.getPhoneNumber() != null) {
            currentUser.setPhoneNumber(updateUserDto.getPhoneNumber());
        }

        if (updateUserDto.getDateOfBirth() != null) {
            currentUser.setDateOfBirth(updateUserDto.getDateOfBirth());
        }

        if (updateUserDto.getEmail() != null && !currentUser.getEmail().equals(updateUserDto.getEmail())
                && userRepository.findUserByEmail(updateUserDto.getEmail()).isEmpty()) {
            String oldEmail = currentUser.getEmail();
            currentUser.setEmail(updateUserDto.getEmail());
            emailService.sendUpdateEmailNotifications(oldEmail, updateUserDto.getEmail());
        }

        currentUser.setGender(updateUserDto.getGender());

        userRepository.save(currentUser);

        return new CurrentUserDto(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getDateOfBirth(),
                currentUser.getRegistrationDate(),
                currentUser.getGender(),
                currentUser.getPhoneNumber()
        );
    }

    /**
     * Удаляет текущего аутентифицированного пользователя.
     *
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @Transactional
    public void deleteCurrentUser() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = getCurrentAuthenticationUser();
        userRepository.delete(currentUser);
    }

    /**
     * Отправляет код для изменения пароля пользователю.
     *
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @Transactional
    public void requestChangeUserPassword() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = getCurrentAuthenticationUser();
        confirmationCodeService.sendConfirmationCode(currentUser.getId(), currentUser.getEmail());
    }

    /**
     * Подтверждает изменение пароля с использованием кода подтверждения.
     *
     * @param code код подтверждения.
     * @param changeUserPasswordDto новый пароль пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws FakeConfirmationCodeException если код подтверждения недействителен.
     * @throws NotValidCodeException если код подтверждения неверен.
     * @throws ExpiredCodeException если код подтверждения просрочен.
     */
    @Transactional
    public void confirmChangeUserPassword(
            String code,
            ChangeUserPasswordDto changeUserPasswordDto
    ) throws UserNotFoundException, UserNotAuthorizedException, FakeConfirmationCodeException,
            NotValidCodeException, ExpiredCodeException {
        User currentUser = getCurrentAuthenticationUser();

        if (confirmationCodeService.checkConfirmationCode(currentUser.getId(), code)) {
            currentUser.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
            userRepository.save(currentUser);
        }
    }

    /**
     * Отправляет ссылку для сброса пароля на указанный email.
     *
     * @param email email пользователя.
     * @throws UserNotFoundException если пользователь с таким email не найден.
     */
    public void sendPasswordResetLink(String email) throws UserNotFoundException {
        User user = findByEmail(email);
        String token = jwtTokenUtils.generatePasswordResetToken(email);
        emailService.sendPasswordResetLetter(email, token);
    }

    /**
     * Подтверждает изменение пароля с использованием токена.
     *
     * @param token токен для сброса пароля.
     * @param changeUserPasswordDto новый пароль.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @Transactional
    public void confirmChangePassword(
            String token,
            ChangeUserPasswordDto changeUserPasswordDto
    ) throws UserNotFoundException {
        User user = findByEmail(jwtTokenUtils.getUserEmail(token));
        user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
        userRepository.save(user);
    }

    /**
     * Добавляет подарок в избранное текущего авторизованного пользователя.
     *
     * @param gift подарок для добавления в избранное.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @Transactional
    public void addGiftToFavorites(Gift gift) throws UserNotFoundException, UserNotAuthorizedException {
        User user = getCurrentAuthenticationUser();
        user.getFavorites().add(gift);
        userRepository.save(user);
    }
}
