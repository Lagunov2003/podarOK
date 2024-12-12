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
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.repositories.UserRepository;
import ru.uniyar.podarok.utils.Builder.UserBuilder;
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

    public User getCurrentAuthenticationUser() throws UserNotAuthorizedException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserNotAuthorizedException("Пользователь не авторизован!");
        }
        String email = authentication.getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с email %s не найден", email))
        );
    }

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

    public CurrentUserDto getCurrentUserProfile() throws UserNotAuthorizedException, UserNotFoundException {
        User currentUser = getCurrentAuthenticationUser();
        return new CurrentUserDto(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName(),
                currentUser.getDateOfBirth(), currentUser.getRegistrationDate(), currentUser.getGender(), currentUser.getPhoneNumber());
    }

    @Transactional
    public CurrentUserDto updateUserProfile(UpdateUserDto updateUserDto) throws UserNotFoundException, UserNotAuthorizedException {
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
        return new CurrentUserDto(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName(),
                currentUser.getDateOfBirth(), currentUser.getRegistrationDate(), currentUser.getGender(), currentUser.getPhoneNumber());
    }

    @Transactional
    public void deleteCurrentUser() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = getCurrentAuthenticationUser();
        userRepository.delete(currentUser);
    }

    @Transactional
    public void requestChangeUserPassword() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = getCurrentAuthenticationUser();
        confirmationCodeService.sendConfirmationCode(currentUser.getId(), currentUser.getEmail());
    }

    @Transactional
    public void confirmChangeUserPassword(String code, ChangeUserPasswordDto changeUserPasswordDto) throws UserNotFoundException, UserNotAuthorizedException, FakeConfirmationCodeException, NotValidCodeException, ExpiredCodeException {
        User currentUser = getCurrentAuthenticationUser();
        if (confirmationCodeService.checkConfirmationCode(currentUser.getId(), code)) {
            currentUser.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
            userRepository.save(currentUser);
        }
    }

    public void sendPasswordResetLink(String email) throws UserNotFoundException {
        findByEmail(email);
        String token = jwtTokenUtils.generatePasswordResetToken(email);
        emailService.sendPasswordResetLetter(email, token);
    }

    @Transactional
    public void confirmChangePassword(String token, ChangeUserPasswordDto changeUserPasswordDto) throws UserNotFoundException {
        String email = jwtTokenUtils.getUserEmail(token);
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void addGiftToFavorites(Gift gift) throws UserNotFoundException, UserNotAuthorizedException {
        User user = getCurrentAuthenticationUser();
        user.getFavorites().add(gift);
        userRepository.save(user);
    }
}
