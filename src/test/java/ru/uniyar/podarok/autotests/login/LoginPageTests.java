package ru.uniyar.podarok.autotests.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.uniyar.podarok.autotests.registration.RegistrationPage;
import ru.uniyar.podarok.autotests.home.HomePage;
import ru.uniyar.podarok.autotests.home.HomePageLogIn;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoginPageTests {
    @Autowired
    private HomePage homePage;
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private HomePageLogIn homePageLogIn;
    @Autowired
    private LoginPageUserOrEmailNotFound loginPageUserOrEmailNotFound;
    @Autowired
    private RegistrationPage registrationPage;

    private final String appUrl = "http://localhost:3000";

    @Test
    void LoginPage_PerformLogin_Admin() throws InterruptedException {
        homePage.navigate(appUrl);
        Thread.sleep(1000);
        homePage.clickLogin();
        Thread.sleep(1000);
        loginPage.login("petrov.nikita702@mail.ru", "Nikita");
        Thread.sleep(1000);
        loginPage.clickLogin();
        Thread.sleep(1000);

        assertEquals("Кабинет", homePageLogIn.lnkAccount.getText());
    }

    @Test
    void LoginPage_PerformLogin_User() throws InterruptedException {
        homePage.navigate(appUrl);
        Thread.sleep(1000);
        homePage.clickLogin();
        Thread.sleep(1000);
        loginPage.login("zhizhindm@yandex.ru", "UserTest");
        Thread.sleep(1000);
        loginPage.clickLogin();
        Thread.sleep(1000);

        assertEquals("Кабинет", homePageLogIn.lnkAccount.getText());
    }

    @Test
    void LoginPage_PerformLogin_UserNotFound() throws InterruptedException {
        homePage.navigate(appUrl);
        Thread.sleep(1000);
        homePage.clickLogin();
        Thread.sleep(1000);
        loginPage.login("user@mail.ru", "useruser");
        Thread.sleep(1000);
        loginPage.clickLogin();
        Thread.sleep(1000);

        assertEquals("Неверная почта или пароль", loginPageUserOrEmailNotFound.lnkError.getText());
    }

    @Test
    void LoginPage_PerformLogin_WrongPassword() throws InterruptedException {
        homePage.navigate(appUrl);
        Thread.sleep(1000);
        homePage.clickLogin();
        Thread.sleep(1000);
        loginPage.login("petrov.nikita702@mail.ru", "useruser");
        Thread.sleep(1000);
        loginPage.clickLogin();
        Thread.sleep(1000);

        assertEquals("Неверная почта или пароль", loginPageUserOrEmailNotFound.lnkError.getText());
    }

    @Test
    void LoginPage_RedirectToSignUp() throws InterruptedException {
        homePage.navigate(appUrl);
        Thread.sleep(1000);
        homePage.clickLogin();
        Thread.sleep(1000);
        loginPage.clickSignUp();
        Thread.sleep(1000);

        assertEquals("Создать аккаунт", registrationPage.txtSignUp.getText());
    }
}
