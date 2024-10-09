package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.ActivationCodeEntity;
import ru.uniyar.podarok.repositories.ActivationCodeRepository;
import ru.uniyar.podarok.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class ActivationCodeService {
    private ActivationCodeRepository activationCodeRepository;
    private UserRepository userRepository;

    public ActivationCodeEntity generationActivationCode(String addressee) {
        ActivationCodeEntity activationCodeEntity = new ActivationCodeEntity();
        activationCodeEntity.setUserId(userRepository.findByEmail(addressee).getId());
        activationCodeEntity.setCode(String.valueOf(100000 + new Random().nextInt(900000)));
        activationCodeEntity.setExpirationTime(LocalDateTime.now().plusHours(24));
        return activationCodeRepository.save(activationCodeEntity);
    }

    // TODO
    // Метод проверки валидности кода, генерация нового
}
