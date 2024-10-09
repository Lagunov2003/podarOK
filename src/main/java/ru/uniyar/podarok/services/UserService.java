package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.ActivationCodeEntity;
import ru.uniyar.podarok.entities.UserEntity;
import ru.uniyar.podarok.exceptions.IncorrectUserId;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
import ru.uniyar.podarok.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserEntity user) throws UserAlreadyExist {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExist("Пользователь уже существует!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        emailService.sendActivationCode(user.getEmail());
    }

    // Приходит код в параметре -> меняется состояние, если код совпадает со значением из бд по этому id
    public void activateUser(UserEntity user,  ActivationCodeEntity activationCode) throws IncorrectUserId {
        if (user.getId() != activationCode.getUserId()) {
            throw new IncorrectUserId("Идентификаторы пользователей не совпадают!");
        }
        user.setActivated(true);
        user.setRole("USER");
    }

    public UserDetails getCurrentUser() throws UserNotAuthorized {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails)authentication.getPrincipal();
        }
        throw new UserNotAuthorized("Пользователь не авторизован!");
    }
}
