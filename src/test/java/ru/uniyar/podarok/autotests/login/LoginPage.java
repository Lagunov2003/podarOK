package ru.uniyar.podarok.autotests.login;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.autotests.BasePage;

@Component
public class LoginPage extends BasePage {

    @FindBy(how = How.XPATH, using = "//input[@type='email']")
    public WebElement txtUserEmail;

    @FindBy(how = How.XPATH, using = "//input[@type='password']")
    public WebElement txtPassword;

    @FindBy(how = How.CLASS_NAME, using = "sing-in__enter-button")
    public WebElement btnLogin;

    @FindBy(how = How.CLASS_NAME, using = "sing-in__block-button")
    public WebElement btnSignUp;
    public void login(String userName, String password) {
        txtUserEmail.sendKeys(userName);
        txtPassword.sendKeys(password);
    }

    public void clickLogin() {
        btnLogin.click();
    }

    public void clickSignUp() {
        btnSignUp.click();
    }
}
