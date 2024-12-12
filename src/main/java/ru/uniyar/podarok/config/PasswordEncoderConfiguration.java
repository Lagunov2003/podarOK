package ru.uniyar.podarok.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Конфигурационный класс для создания экземпляра BCryptPasswordEncoder для кодирования паролей.
 */
@Configuration
public class PasswordEncoderConfiguration {
    /**
     * Создает бин для использования BCryptPasswordEncoder для кодирования паролей.
     *
     * @return экземпляр BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
