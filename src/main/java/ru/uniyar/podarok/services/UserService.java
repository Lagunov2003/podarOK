package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

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

    public UserDto getCurrentUserProfile() throws UserNotAuthorized, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
            return new UserDto(user.getId(), user.getEmail());
        }
        throw new UserNotAuthorized("Пользователь не авторизован!");
    }
}
