package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.entities.Role;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConfirmationCodeService confirmationCodeService;

    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    void createNewUser_success_whenCorrectData() throws UserAlreadyExist {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encoded_password");
        savedUser.setFirstName("test");

        Mockito.when(userRepository.findUserByEmail(registrationUserDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registrationUserDto.getPassword())).thenReturn("encoded_password");
        Mockito.when(roleService.getUserRole()).thenReturn(userRole);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createNewUser(registrationUserDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());
        Mockito.verify(emailService, Mockito.times(1)).sendWelcomeLetter("test@example.com", "test");
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void createNewUser_exception_whenUserAlreadyExist() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");

        Mockito.when(userRepository.findUserByEmail(registrationUserDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExist.class, () -> userService.createNewUser(registrationUserDto));

        Mockito.verify(emailService, Mockito.never()).sendWelcomeLetter(anyString(), anyString());
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void loadUserByUsername_success_whenDetailsCorrect() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345");
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        user.setRoles(List.of(userRole));

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("12345", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_exception_whenUserNotFound() {
        String email = "test@example.com";

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void getCurrentAuthenticationUser_success_whenUserIsAuthenticated() throws UserNotAuthorized, UserNotFoundException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getCurrentAuthenticationUser();

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getCurrentAuthenticationUser_userNotAuthorized_whenUserNotAuthenticated() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotAuthorized.class, () -> userService.getCurrentAuthenticationUser());
    }

    @Test
    void getCurrentAuthenticationUser_exception_whenUserNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentAuthenticationUser());
    }

    @Test
    void getCurrentUserProfile_success_whenCorrectData() throws UserNotAuthorized, UserNotFoundException {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("test@example.com");
        currentUser.setFirstName("John");
        currentUser.setLastName("Doe");
        currentUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        currentUser.setRegistrationDate(LocalDate.of(2020, 1, 1));
        currentUser.setGender(true);
        currentUser.setPhoneNumber("1234567890");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(currentUser));

        CurrentUserDto result = userService.getCurrentUserProfile();

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        assertEquals("1234567890", result.getPhoneNumber());
    }

    @Test
    void updateUserProfile_success_whenCorrectData() throws UserNotFoundException, UserNotAuthorized {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("old@example.com");
        currentUser.setFirstName("John");
        currentUser.setLastName("Doe");
        currentUser.setGender(true);
        currentUser.setPhoneNumber("1234567890");
        currentUser.setDateOfBirth(LocalDate.of(2000, 10, 24));

        UpdateUserDto updateUserDto = new UpdateUserDto("Jane", "Smith", LocalDate.of(1990, 5, 4), false, "new@example.com", "0987654321");

        Mockito.doReturn(currentUser).when(userService).getCurrentAuthenticationUser();
        Mockito.when(userRepository.findUserByEmail("new@example.com")).thenReturn(Optional.empty());

        CurrentUserDto result = userService.updateUserProfile(updateUserDto);

        assertEquals("new@example.com", result.getEmail());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertFalse(result.isGender());
        assertEquals("0987654321", result.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 5, 4), result.getDateOfBirth());

        Mockito.verify(emailService).sendUpdateEmailNotifications("old@example.com", "new@example.com");
        Mockito.verify(userRepository).save(currentUser);
    }

    @Test
    void requestChangeUserPassword_success_whenCorrectData() throws UserNotFoundException, UserNotAuthorized {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("test@example.com");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(currentUser));

        userService.requestChangeUserPassword();

        Mockito.verify(confirmationCodeService).sendConfirmationCode(1L, "test@example.com");
    }

    @Test
    void confirmChangeUserPassword_success_whenCorrectData() throws UserNotFoundException, UserNotAuthorized, FakeConfirmationCode, NotValidCode, ExpiredCode {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("test@example.com");
        currentUser.setPassword("oldPassword");

        String code = "validCode";
        String newPassword = "newPassword";
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto(1L, "test@example.com", newPassword, newPassword, "validCode");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(currentUser));
        Mockito.when(confirmationCodeService.checkConfirmationCode(1L, code)).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.confirmChangeUserPassword(code, changeUserPasswordDto);

        Mockito.verify(userRepository).save(currentUser);
        assertEquals("encodedNewPassword", currentUser.getPassword());
    }

    @Test
    void deleteCurrentUser_success_whenCorrectData() throws UserNotFoundException, UserNotAuthorized {
        User currentUser = new User();
        currentUser.setId(1L);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(currentUser));

        userService.deleteCurrentUser();

        Mockito.verify(userRepository).delete(currentUser);
    }
}

