package ru.uniyar.podarok.autotests.home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.autotests.BasePage;

@Component
public class HomePageLogIn extends BasePage {
    @FindBy(how = How.CLASS_NAME, using = "header__sing-in")
    public WebElement lnkAccount;

}
