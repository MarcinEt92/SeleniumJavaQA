import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TestCase {
    private WebDriver driver;
    private final String shopWebsite = "https://demo.opencart.com/";
    private final String automationPracticeUrl = "https://testautomationpractice.blogspot.com/";
    private final String authPageUrl = "https://testpages.eviltester.com/styled/auth/basic-auth-results.html";

    @BeforeTest
    void setUp() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    @AfterTest
    void tearDown() {
        driver.quit();
    }

    @Test
    void testWebsiteTitle() {
        driver.get(shopWebsite);
        String websiteTitle = driver.getTitle();
        String expectedTitle = "Your Store";
        assert websiteTitle != null;
        Assert.assertEquals(websiteTitle, expectedTitle);
    }

    @Test
    void isLogoDisplayed() {
        driver.get(shopWebsite);
        WebElement logo = driver.findElement(By.cssSelector("img[title='Your Store']"));
        Assert.assertTrue(logo.isDisplayed());
    }

    @Test
    void testWebsiteSearch() {
        String searchPhrase = "Mac";

        driver.get(shopWebsite);
        driver.findElement(By.name("search")).sendKeys(searchPhrase + Keys.ENTER);
        List<WebElement> items = driver.findElements(By.xpath("//div[@id='display-control']/div"));

        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(searchPhrase));
        Assert.assertFalse(items.isEmpty());
    }

    @Test
    void testXpathSearch() {
        driver.get(shopWebsite);
        String searchXpath = "//input[starts-with(@placeholder, 'Sea')]";
        driver.findElement(By.xpath(searchXpath)).sendKeys("Shirt" + Keys.ENTER);
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains("Search"));
    }

    @Test
    void basicGetCommands() {
        driver.get(shopWebsite);
        System.out.println("Driver title: " + driver.getTitle());
        System.out.println("Current url: " + driver.getCurrentUrl());
        System.out.println("Current Window Id: " + driver.getWindowHandle());
        System.out.println("Current Window Ids: " + driver.getWindowHandles());
        Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("Shopping Cart"));
    }

    @Test
    void basicBrowserMethods() {
        String[] websites = {shopWebsite, "https://www.google.com/", "https://onet.pl", "https://wp.pl"};
        for (String website : websites) {
            driver.get(website);
            driver.switchTo().newWindow(WindowType.TAB);
        }
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            System.out.println(driver.getCurrentUrl());
            if (driver.getCurrentUrl().equals("https://www.google.com/")) {
                driver.close();
            }
        }
        // browser methods are close and quit
        // close() closes one single browser window
        // quit() closes all browser window
    }

    @Test
    void testConditionalMethods() {
        // access through webElement: isDisplayed, isEnabled, isSelected
        driver.get(shopWebsite);
        WebElement image = driver.findElement(By.cssSelector("img[title='Your Store']"));
        Assert.assertTrue(image.isDisplayed());
    }

    @Test
    void testWaits() {
        // No Such Element Exception - element is not presented, locator is correct, problem with sync
        // Element not found exception - incorrect locator

        // implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");

        // explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement userInput =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        userInput.sendKeys("user");
        driver.findElement(By.name("password")).sendKeys("password");
        WebElement loginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']"))
        );
    }

    @Test
    void navCommands() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get("https://google.com");
        driver.navigate().to("https://wp.pl");
        driver.navigate().to("https://onet.pl");
        driver.navigate().back();
        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains("Google"));
        driver.navigate().forward();
        driver.navigate().refresh();
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains("Polska"));
    }

    @Test
    void testCheckboxes() {
        driver.get(automationPracticeUrl);
        String sevenDaysCheckBoxLocator = "//label[contains(normalize-space(), 'Days')]/following-sibling::div/input";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sunday")));
        List<WebElement> sevenDaysCheckBoxes = driver.findElements(By.xpath(sevenDaysCheckBoxLocator));
        List<WebElement> sevenDaysCheckBoxesSublist = sevenDaysCheckBoxes.subList(3, sevenDaysCheckBoxes.size() - 1);

        for (WebElement sevenDaysCheckBox : sevenDaysCheckBoxesSublist) {
            sevenDaysCheckBox.click();
        }

        for (WebElement sevenDaysCheckBox : sevenDaysCheckBoxesSublist) {
            Assert.assertTrue(sevenDaysCheckBox.isSelected());
        }
    }

    @Test
    void testAlerts() {
        driver.get(automationPracticeUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("alertBtn"))).click();
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmBtn"))).click();
        Alert alertElement = driver.switchTo().alert();
        System.out.println("Alert text: " + alertElement.getText());
        alertElement.dismiss();
    }

    @Test
    void testFrames() {
        String frameUrl = "https://testpages.eviltester.com/styled/iframes-test.html";
        driver.get(frameUrl);
        driver.switchTo().frame(driver.findElement(By.id("thedynamichtml")));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String elemText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iframe1"))).getText();
        Assert.assertEquals(elemText, "iFrame List Item 1");
        driver.switchTo().defaultContent();
    }

    @Test
    void testFramesAssignment() {
        String frameUrl = "https://ui.vision/demo/webtest/frames/";
        int frameIndex = 5;
        String xpathFrameLocator = String.format("//frame[contains(@src, '%d')]", frameIndex);
        driver.get(frameUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement fifthFrame = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFrameLocator)));
        driver.switchTo().frame(fifthFrame);
        WebElement link = driver.findElement(By.tagName("a"));
        Assert.assertTrue(link.isDisplayed());
    }
}
