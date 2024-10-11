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
import ru.uniyar.podarok.entities.ActivationCodeEntity;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.IncorrectUserId;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
import ru.uniyar.podarok.repositories.RoleRepository;
import ru.uniyar.podarok.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    // TODO
    // Заменить RoleRepository на RoleService
    private RoleRepository roleRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с email '%s' не найден", email)
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

//    public void createNewUser(User user) {
//        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
//        userRepository.save(user);
//    }

    public void registerUser(User user) throws UserAlreadyExist {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExist("Пользователь уже существует!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        emailService.sendActivationCode(user.getEmail());
    }

    // Приходит код в параметре -> меняется состояние, если код совпадает со значением из бд по этому id
    public void activateUser(User user, ActivationCodeEntity activationCode) throws IncorrectUserId {
        if (user.getId() != activationCode.getUserId()) {
            throw new IncorrectUserId("Идентификаторы пользователей не совпадают!");
        }
        user.setActivated(true);
    }

    public UserDetails getCurrentUser() throws UserNotAuthorized {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new UserNotAuthorized("Пользователь не авторизован!");
    }
}
