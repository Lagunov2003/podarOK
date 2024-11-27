package ru.uniyar.podarok.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
import ru.uniyar.podarok.utils.JwtTokenUtils;

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

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Spy
    @InjectMocks
    private UserService userService;

    private RegistrationUserDto registrationUserDto;
    private User user;
    private Role role;
    @BeforeEach
    void setUp() {
        registrationUserDto = new RegistrationUserDto(1L, "test", "test@example.com", "12345");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encoded_password");
        user.setFirstName("test");
        user.setLastName("user");
        user.setDateOfBirth(LocalDate.now());
        user.setRegistrationDate(LocalDate.now());
        user.setGender(true);
        user.setPhoneNumber("80000000000");

        role = new Role();
        role.setName("ROLE_USER");

    }
    @Test
    void UserService_CreateNewUser_ReturnsCreatedUser() throws UserAlreadyExistException {
        Mockito.when(userRepository.findUserByEmail(registrationUserDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registrationUserDto.getPassword())).thenReturn("encoded_password");
        Mockito.when(roleService.getUserRole()).thenReturn(role);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createNewUser(registrationUserDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());
        assertEquals("test", result.getFirstName());
        assertEquals("user", result.getLastName());
        assertEquals(LocalDate.now(), result.getDateOfBirth());
        assertEquals(LocalDate.now(), result.getRegistrationDate());
        assertTrue(result.getGender());
        assertEquals("80000000000", result.getPhoneNumber());
        Mockito.verify(emailService, Mockito.times(1)).sendWelcomeLetter("test@example.com", "test");
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void UserService_CreateNewUser_ThrowsUserAlreadyExistException() {
        Mockito.when(userRepository.findUserByEmail(registrationUserDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> userService.createNewUser(registrationUserDto));

        Mockito.verify(emailService, Mockito.never()).sendWelcomeLetter(anyString(), anyString());
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void UserService_LoadUserByUsername_ReturnsUserDetails() {
        String email = "test@example.com";
        user.setRoles(List.of(role));
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void UserService_LoadUserByUsername_ThrowsUsernameNotFoundException() {
        String email = "test@example.com";
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void UserService_GetCurrentAuthenticationUser_ReturnsCurrentUser() throws UserNotAuthorizedException, UserNotFoundException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getCurrentAuthenticationUser();

        assertNotNull(result);
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());
        assertEquals("test", result.getFirstName());
        assertEquals("user", result.getLastName());
        assertEquals(LocalDate.now(), result.getDateOfBirth());
        assertEquals(LocalDate.now(), result.getRegistrationDate());
        assertTrue(result.getGender());
        assertEquals("80000000000", result.getPhoneNumber());
    }

    @Test
    void UserService_GetCurrentAuthenticationUser_ThrowsUserNotAuthorizedException() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotAuthorizedException.class, () -> userService.getCurrentAuthenticationUser());
    }

    @Test
    void UserService_GetCurrentAuthenticationUser_ThrowsUserNotFoundException() {
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
    void UserService_GetCurrentUserProfile_ReturnsCurrentUserDto() throws UserNotAuthorizedException, UserNotFoundException {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        CurrentUserDto result = userService.getCurrentUserProfile();
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("test", result.getFirstName());
        assertEquals("user", result.getLastName());
        assertEquals(LocalDate.now(), result.getDateOfBirth());
        assertEquals(LocalDate.now(), result.getRegistrationDate());
        assertTrue(result.isGender());
        assertEquals("80000000000", result.getPhoneNumber());
    }

    @Test
    void UserService_UpdateUserProfile_ReturnsUpdatedCurrentUserDto() throws UserNotFoundException, UserNotAuthorizedException {
        UpdateUserDto updateUserDto = new UpdateUserDto("Jane", "Smith", LocalDate.of(1990, 5, 4), false, "new@example.com", "0987654321");
        Mockito.doReturn(user).when(userService).getCurrentAuthenticationUser();
        Mockito.when(userRepository.findUserByEmail("new@example.com")).thenReturn(Optional.empty());

        CurrentUserDto result = userService.updateUserProfile(updateUserDto);

        assertEquals("new@example.com", result.getEmail());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertFalse(result.isGender());
        assertEquals("0987654321", result.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 5, 4), result.getDateOfBirth());
        Mockito.verify(emailService).sendUpdateEmailNotifications("test@example.com", "new@example.com");
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void UserService_RequestChangeUserPassword_SendsConfirmationCode() throws UserNotFoundException, UserNotAuthorizedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        userService.requestChangeUserPassword();

        Mockito.verify(confirmationCodeService).sendConfirmationCode(1L, "test@example.com");
    }

    @Test
    void UserService_ConfirmChangeUserPassword_ReturnsNewPassword() throws UserNotFoundException, UserNotAuthorizedException, FakeConfirmationCodeException, NotValidCodeException, ExpiredCodeException {

        String code = "validCode";
        String newPassword = "newPassword";
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto(1L, "test@example.com", newPassword, newPassword, "validCode");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        Mockito.when(confirmationCodeService.checkConfirmationCode(1L, code)).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.confirmChangeUserPassword(code, changeUserPasswordDto);

        Mockito.verify(userRepository).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void UserService_DeleteCurrentUser_VerifiesUserIsDeleted() throws UserNotFoundException, UserNotAuthorizedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        userService.deleteCurrentUser();

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    void UserService_SendPasswordResetLink_VerifiesSendsLink() throws UserNotFoundException {
        String email = "test@example.com";
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(jwtTokenUtils.generatePasswordResetToken(email)).thenReturn("resetToken");

        userService.sendPasswordResetLink(email);

        Mockito.verify(jwtTokenUtils).generatePasswordResetToken(email);
        Mockito.verify(emailService).sendPasswordResetLetter(email, "resetToken");
    }

    @Test
    void UserService_SendPasswordResetLink_ThrowsUserNotFoundException() {
        String email = "nonexistent@example.com";
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.sendPasswordResetLink(email));
        Mockito.verify(emailService, Mockito.never()).sendPasswordResetLetter(anyString(), anyString());
    }

    @Test
    void UserService_ConfirmChangePassword_VerifiesChangedPasswordSaved() throws UserNotFoundException {
        String token = "validToken";
        String email = "test@example.com";
        ChangeUserPasswordDto changePasswordDto = new ChangeUserPasswordDto();
        changePasswordDto.setPassword("newPassword");
        Mockito.when(jwtTokenUtils.getUserEmail(token)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        userService.confirmChangePassword(token, changePasswordDto);

        Mockito.verify(userRepository).save(user);
        Mockito.verify(passwordEncoder).encode("newPassword");
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void UserService_ConfirmChangePassword_ThrowsUserNotFoundException() {
        String token = "validToken";
        String email = "test@example.com";
        ChangeUserPasswordDto changePasswordDto = new ChangeUserPasswordDto();
        changePasswordDto.setPassword("newPassword");

        Mockito.when(jwtTokenUtils.getUserEmail(token)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.confirmChangePassword(token, changePasswordDto));
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }
}

