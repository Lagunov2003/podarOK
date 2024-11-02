package ru.uniyar.podarok.controllers;

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
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
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
        Mockito.when(userService.getCurrentUserProfile()).thenThrow(new UserNotAuthorized("Пользователь не авторизован!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_UpdateProfile_ReturnsStatusIsOk() throws Exception {
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
    void UserController_UpdateProfile_ReturnsStatusIsBadRequest() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("", "", null, true, null, "");

        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateUserDto)))
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
        Mockito.doThrow(new UserNotAuthorized("Пользователь не авторизован!")).when(userService).requestChangeUserPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/changePassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsOk() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto(1L, "test@example.com", "newpassword", "newpassword", "123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пароль успешно изменён!"));
    }

    @Test
    void UserController_ConfirmChangeUserPassword_ReturnsStatusIsBadRequest() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto(1L, "test@example.com","newpassword1", "newpassword2", "123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmChanges")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароли не совпадают!"));
    }

    @Test
    void UserController_DeleteUser_ReturnsStatusIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удалён!"));
    }

    @Test
    void UserController_DeleteUser_ReturnsStatusIsUnauthorized() throws Exception {
        Mockito.doThrow(new UserNotAuthorized("Пользователь не авторизован!")).when(userService).deleteCurrentUser();

        mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }
}

