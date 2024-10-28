package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.config.JwtRequestFilter;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
import ru.uniyar.podarok.services.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void showProfile_statusIsOk_whenUserIsAuthorized() throws Exception {
        CurrentUserDto userDto = new CurrentUserDto(1L, "test@example.com", "Test", "User", LocalDate.now(), LocalDate.now(), true, "123456789");
        Mockito.doNothing().when(jwtRequestFilter).doFilterInternal(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(userService.getCurrentUserProfile()).thenReturn(userDto);
        String token = "validToken";

        mockMvc.perform(MockMvcRequestBuilders.get("/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void showProfile_statusIsUnauthorized_whenUserNotAuthorized() throws Exception {
        Mockito.when(userService.getCurrentUserProfile()).thenThrow(new UserNotAuthorized("Пользователь не авторизован!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void updateProfile_success_whenCorrectData() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName", LocalDate.now(), true, "new@example.com", "87654321");
        CurrentUserDto updatedUserDto = new CurrentUserDto(1L, "new@example.com", "NewFirstName", "NewLastName", LocalDate.now(), LocalDate.now(), true, "87654321");
        Mockito.when(userService.updateUserProfile(Mockito.any())).thenReturn(updatedUserDto);
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void updateProfile_badRequest_whenEmptyDataFields() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("", "", null, true, null, "");

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Все поля должны быть заполнены!"));
    }

    @Test
    void requestChangeUserPassword_success_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Перейдите по ссылке в письме для подтверждения смены пароля!"));
    }

    @Test
    void requestChangeUserPassword_unauthorized_whenUserNotAuthorized() throws Exception {
        Mockito.doThrow(new UserNotAuthorized("Пользователь не авторизован!")).when(userService).requestChangeUserPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void confirmChangeUserPassword_success_correctData() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto(1L, "test@example.com", "newpassword", "newpassword", "123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пароль успешно изменён!"));
    }

    @Test
    void confirmChangeUserPassword_badRequest_whenPasswordsDoNotMatch() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto(1L, "test@example.com","newpassword1", "newpassword2", "123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароли не совпадают!"));
    }

    @Test
    void deleteUser_success_whenUserIsAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удалён!"));
    }

    @Test
    void deleteUser_unauthorized_whenUserNotAuthorized() throws Exception {
        Mockito.doThrow(new UserNotAuthorized("Пользователь не авторизован!")).when(userService).deleteCurrentUser();

        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }
}

