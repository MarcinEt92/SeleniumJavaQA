package POM;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Objects;

public class LoginTest {
    private WebDriver driver;

    @BeforeTest
    void setUp() {
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @AfterTest
    void tearDown() {
        driver.quit();
    }

    @Test
    void testLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.setUserName("username");
        loginPage.setPassword("password");
        loginPage.clickLogin();
        Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("Invalid credentials"));
    }

    @Test
    void testLoginPageWithFactory() {
        LoginPageWithFactory loginPage = new LoginPageWithFactory(driver);
        loginPage.setUserName("username");
        loginPage.setPassword("password");
        loginPage.clickLogin();
        Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("Invalid credentials"));
    }
}
