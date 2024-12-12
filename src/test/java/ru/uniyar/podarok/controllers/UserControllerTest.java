package ru.uniyar.podarok.controllers;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.ForgotUserPasswordDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Autowired
    private  ObjectMapper objectMapper;
    @Test
    void UserController_ShowProfile_ReturnsStatusIsOK() throws Exception {
        CurrentUserDto userDto = new CurrentUserDto(1L, "test@example.com", "Test", "User", LocalDate.now(), LocalDate.now(), true, "123456789");
        Mockito.when(userService.getCurrentUserProfile()).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void UserController_ShowProfile_ReturnsStatusIsUnauthorized() throws Exception {
        Mockito.when(userService.getCurrentUserProfile()).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_ShowProfile_ReturnsStatusIsNotFound() throws Exception {
        Mockito.when(userService.getCurrentUserProfile()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsOk() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName", LocalDate.now(), true, "new@example.com", "87654321");
        CurrentUserDto updatedUserDto = new CurrentUserDto(1L, "new@example.com", "NewFirstName", "NewLastName", LocalDate.now(), LocalDate.now(), true, "87654321");
        Mockito.when(userService.updateUserProfile(Mockito.any())).thenReturn(updatedUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsUnauthorized() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName", LocalDate.now(), true, "new@example.com", "87654321");
        Mockito.when(userService.updateUserProfile(Mockito.any())).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsBadRequest_WhenSomeFieldsNotFilledIn() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("", "", null, true, null, "");

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Некоторые поля заполнены некорректно!"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsNotFound() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName", LocalDate.now(), true, "new@example.com", "87654321");
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!")).when(userService).updateUserProfile(Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsBadRequest_WhenAllFieldsNotFilledIn() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName", LocalDate.now(), true, "new@example.com", "87654321");
        Mockito.doThrow(new NullPointerException("Все поля должны быть заполнены!")).when(userService).updateUserProfile(Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Все поля должны быть заполнены!"));
    }


    @Test
    void UserController_RequestChangeUserPassword_ReturnsStatusIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Перейдите по ссылке в письме для подтверждения смены пароля!"));
    }

    @Test
    void UserController_RequestChangeUserPassword_ReturnsStatusIsUnauthorized() throws Exception {
        Mockito.doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(userService).requestChangeUserPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_RequestChangeUserPassword_ReturnsStatusIsNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!")).when(userService).requestChangeUserPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsOk() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword", "newpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пароль успешно изменён!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsBadRequest_PasswordDoesNotMatch() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword1", "newpassword2");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароли не совпадают!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsBadRequest_PasswordTooShort() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpa", "newpa");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен быть минимум 6 символов!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsUnauthorized() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword", "newpassword");
        Mockito.doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(userService).confirmChangeUserPassword(Mockito.anyString(), Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsNotFound() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword", "newpassword");
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!")).when(userService).confirmChangeUserPassword(Mockito.anyString(), Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsForbidden() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword", "newpassword");
        Mockito.doThrow(new FakeConfirmationCodeException("Неправильный код подтверждения!")).when(userService).confirmChangeUserPassword(Mockito.anyString(), Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Неправильный код подтверждения!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsGone() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto("newpassword", "newpassword");
        Mockito.doThrow(new ExpiredCodeException("Код подтверждения истёк!")).when(userService).confirmChangeUserPassword(Mockito.anyString(), Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isGone())
                .andExpect(content().string("Код подтверждения истёк!"));
    }

    @Test
    void UserController_DeleteUser_ReturnsStatusIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удалён!"));
    }

    @Test
    void UserController_DeleteUser_ReturnsStatusIsUnauthorized() throws Exception {
        Mockito.doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(userService).deleteCurrentUser();

        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_DeleteUser_ReturnsStatusIsNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!")).when(userService).deleteCurrentUser();

        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsBadRequest_PasswordMismatch() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto( "newPassword", "newNewPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароли не совпадают!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsBadRequest_ShortPassword() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("12345", "12345");

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен быть минимум 6 символов!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsOk() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("newPassword", "newPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пароль успешно изменён!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsNotFound() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("newPassword", "newPassword");
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(userService).confirmChangePassword(Mockito.anyString(), Mockito.any(ChangeUserPasswordDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsUnauthorized_ExpiredToken() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("newPassword", "newPassword");
        Mockito.doThrow(new ExpiredJwtException(null, null, "Токен устарел!"))
                .when(userService).confirmChangePassword(Mockito.anyString(), Mockito.any(ChangeUserPasswordDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "expiredToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Токен устарел!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsUnauthorized_InvalidToken() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("newPassword", "newPassword");
        Mockito.doThrow(new SignatureException("Некорректный токен!"))
                .when(userService).confirmChangePassword(Mockito.anyString(), Mockito.any(ChangeUserPasswordDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Некорректный токен!"));
    }

    @Test
    void UserController_ResetPassword_ReturnsStatusIsUnauthorized_InvalidTokenFormat() throws Exception {
        ChangeUserPasswordDto changeUserPasswordDto = new ChangeUserPasswordDto("newPassword", "newPassword");
        Mockito.doThrow(new MalformedJwtException("Неправильный формат токена!"))
                .when(userService).confirmChangePassword(Mockito.anyString(), Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword")
                        .param("token", "invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неправильный формат токена!"));
    }

    @Test
    void UserController_ProcessForgotPassword_ReturnsStatusIsOk() throws Exception {
        ForgotUserPasswordDto forgotUserPasswordDto = new ForgotUserPasswordDto("test@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotUserPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Перейдите по ссылке в письме для восстановления пароля!"));
    }

    @Test
    void UserController_ProcessForgotPassword_ReturnsStatusIsBadRequest() throws Exception {
        ForgotUserPasswordDto forgotUserPasswordDto = new ForgotUserPasswordDto("nonexistent@example.com");
        Mockito.doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(userService).sendPasswordResetLink(Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotUserPasswordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пользователь не найден!"));
    }


}

