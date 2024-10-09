package ru.uniyar.podarok.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entity.UserEntity;
import ru.uniyar.podarok.exception.UserAlreadyExist;
import ru.uniyar.podarok.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void registration(UserEntity user) throws UserAlreadyExist {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExist("Пользователь уже существует!");
        }
        userRepository.save(user);
    }
}
