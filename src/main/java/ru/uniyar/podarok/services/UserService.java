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
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private EmailService emailService;
    private ConfirmationCodeService confirmationCodeService;
    private PasswordEncoder passwordEncoder;

    private User getCurrentAuthenticationUser() throws UserNotAuthorized, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthorized("Пользователь не авторизован!");
        }
        String email = authentication.getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    public User createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExist {
        if (userRepository.findUserByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExist("Пользователь уже существует!");
        }
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setFirstName(registrationUserDto.getFirstName());
        user.setRegistrationDate(LocalDate.now());
        user.setRoles(List.of(roleService.getUserRole()));
        emailService.sendWelcomeLetter(registrationUserDto.getEmail(), registrationUserDto.getFirstName());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с email %s не найден", email)
                )
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(
                                role.getName())).collect(Collectors.toList()
                        )
        );
    }

    public UserDto getCurrentUserProfile() throws UserNotAuthorized, UserNotFoundException {
        User currentUser = getCurrentAuthenticationUser();
        return new UserDto(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
    }

    @Transactional
    public UserDto updateUserProfile(UpdateUserDto updateUserDto) throws UserNotFoundException, UserNotAuthorized {
        User currentUser = getCurrentAuthenticationUser();
        if (updateUserDto.getFirstName() != null && !currentUser.getFirstName().equals(updateUserDto.getFirstName())) {
            currentUser.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null && !currentUser.getLastName().equals(updateUserDto.getLastName())) {
            currentUser.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getGender() != currentUser.getGender()) {
            currentUser.setGender(updateUserDto.getGender());
        }
        if (updateUserDto.getPhoneNumber() != null && !currentUser.getPhoneNumber().equals(updateUserDto.getPhoneNumber())) {
            currentUser.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
        if (updateUserDto.getDateOfBirth() != null && currentUser.getDateOfBirth() != updateUserDto.getDateOfBirth()) {
            currentUser.setDateOfBirth(updateUserDto.getDateOfBirth());
        }
        if (updateUserDto.getEmail() != null && !currentUser.getEmail().equals(updateUserDto.getEmail())
                && userRepository.findUserByEmail(updateUserDto.getEmail()).isEmpty()) {
            currentUser.setEmail(updateUserDto.getEmail());
            emailService.sendUpdateEmailNotifications(currentUser.getEmail(), updateUserDto.getEmail());
        }
        userRepository.save(currentUser);
        return new UserDto(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
    }

    @Transactional
    public void requestChangeUserPassword() throws UserNotFoundException, UserNotAuthorized {
        User currentUser = getCurrentAuthenticationUser();
        confirmationCodeService.sendConfirmationCode(currentUser.getId(), currentUser.getEmail());
    }

    @Transactional
    public void confirmChangeUserPassword(String code, ChangeUserPasswordDto changeUserPasswordDto) throws UserNotFoundException, UserNotAuthorized, FakeConfirmationCode, NotValidCode, ExpiredCode {
        User currentUser = getCurrentAuthenticationUser();
        String newPassword = passwordEncoder.encode(changeUserPasswordDto.getPassword());
        if (confirmationCodeService.checkConfirmationCode(currentUser.getId(), code)) {
            currentUser.setPassword(newPassword);
            userRepository.save(currentUser);
        }
    }

    @Transactional
    public void deleteCurrentUser() throws UserNotFoundException, UserNotAuthorized {
        User currentUser = getCurrentAuthenticationUser();
        userRepository.delete(currentUser);
    }
}
