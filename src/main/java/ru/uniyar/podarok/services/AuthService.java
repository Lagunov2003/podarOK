package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.JwtRequest;
import ru.uniyar.podarok.dtos.JwtResponse;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserAlreadyExistException;
import ru.uniyar.podarok.utils.JwtTokenUtils;

/**
 * Сервис для работы с аутентификацией и регистрацией пользователей.
 */
@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;

    /**
     * Создает нового пользователя в системе на основе переданных данных регистрации.
     *
     * @param registrationUserDto объект {@link RegistrationUserDto}, содержащий данные для регистрации пользователя.
     * @return объект {@link UserDto}, содержащий основные данные о зарегистрированном пользователе.
     * @throws UserAlreadyExistException если пользователь с указанным email уже существует.
     */
    public UserDto createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExistException {
        User user = userService.createNewUser(registrationUserDto);
        return new UserDto(user.getEmail(), user.getFirstName());
    }

    /**
     * Создает JWT токен для аутентифицированного пользователя.
     *
     * @param authRequest объект {@link JwtRequest}, содержащий email и пароль пользователя.
     * @return объект {@link JwtResponse}, содержащий сгенерированный токен.
     * @throws BadCredentialsException если аутентификация не удалась (неверный email или пароль).
     */
    public JwtResponse createAuthToken(JwtRequest authRequest) throws BadCredentialsException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
