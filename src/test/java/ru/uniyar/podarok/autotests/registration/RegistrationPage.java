package ru.uniyar.podarok.autotests.registration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.autotests.BasePage;

@Component
public class RegistrationPage extends BasePage {
    @FindBy(how = How.CLASS_NAME, using = "sing-in__register-title")
    public WebElement txtSignUp;
}
