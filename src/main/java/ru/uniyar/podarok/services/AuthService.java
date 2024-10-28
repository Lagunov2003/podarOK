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
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.utils.JwtTokenUtils;

@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;

    public UserDto createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExist {
        User user = userService.createNewUser(registrationUserDto);
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName());
    }

    public JwtResponse createAuthToken(JwtRequest authRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
