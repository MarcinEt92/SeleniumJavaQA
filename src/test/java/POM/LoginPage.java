// This class does not use PageFactory

package POM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;

    // constructor for WebDriver init
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // locators
    By input_username = By.xpath("//input[@name='username']");
    By input_password = By.xpath("//input[@name='password']");
    By btn_login = By.xpath("//button[normalize-space()='Login']");

    // action methods
    public void setUserName(String userName) {
        driver.findElement(input_username).sendKeys(userName);
    }

    public void setPassword(String password) {
        driver.findElement(input_password).sendKeys(password);
    }

    void clickLogin() {
        driver.findElement(btn_login).click();
    }
}
