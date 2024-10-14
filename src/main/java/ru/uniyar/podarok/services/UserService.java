package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
//    private EmailService emailService;
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

    @Transactional
    public User createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExist {
        if (userRepository.findUserByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExist("Пользователь уже существует!");
        }
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setFirstName(registrationUserDto.getFirstName());
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }
}
