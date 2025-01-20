import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TestCase {
    private WebDriver driver;
    private final String shopWebsite = "https://demo.opencart.com/";
    private final String automationPracticeUrl = "https://testautomationpractice.blogspot.com/";

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
            if (Objects.equals(driver.getCurrentUrl(), "https://www.google.com/")) {
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

    @Test
    void testDropdowns() {
        driver.get(automationPracticeUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement countrySelector = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country")));
        Select countryList = new Select(countrySelector);
        List<WebElement> countries = countryList.getOptions();
        for (WebElement country : countries) {
            System.out.println("Country: " + country.getText());
        }
        countryList.selectByValue("japan");
        Assert.assertTrue(driver.findElement(By.xpath("//option[@value='japan']")).isSelected());
    }

    @Test
    void testBootstrapDropdown() {
        driver.get("https://www.w3schools.com/bootstrap/bootstrap_dropdowns.asp");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept-choices"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu1"))).click();
        driver.findElement(By.xpath("//li/a[text()='HTML']")).click();
    }

    @Test
    void testAutoSuggestDropdown() {
        driver.get("https://google.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[id='L2AGLb']"))).click();
        driver.findElement(By.cssSelector("textarea[name=q]")).sendKeys("selenium");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']/li[2]"))).click();
    }

    @Test
    void testWebTables() {
        driver.get("https://testautomationpractice.blogspot.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        int rowsSize = driver.findElements(By.xpath("//table[@name='BookTable']//tr")).size();
        int columnsSize = driver.findElements(By.xpath("//table[@name='BookTable']//tr/th")).size();
        for (int i = 1; i < columnsSize; i++) {
            for (int j = 2; j < rowsSize; j++) {
                String tableText = driver.
                        findElement(By.xpath(String.format("//table[@name='BookTable']//tr[%d]/td[%d]", j, i))).getText();
                System.out.println(tableText);
            }
            System.out.print("\n");
        }
    }

    @Test
    void testDatePickers() {
        LocalDate expectedDate = LocalDate.of(2025, 3, 17);
        int expectedYear = expectedDate.getYear();
        String expectedMonth = expectedDate.getMonth().toString();
        int expectedDay = expectedDate.getDayOfMonth();

        driver.get("https://jqueryui.com/datepicker/");
        driver.switchTo().frame(driver.findElement(By.className("demo-frame")));
        By forwardLocator = By.xpath("//a[@data-handler='next']");
        By backLocator = By.xpath("//a[@data-handler='prev']");
        By directionLocator = expectedDate.isAfter(LocalDate.now()) ? forwardLocator: backLocator;
        By dayLocator = By.xpath(String.format("//td[@data-handler='selectDay']/a[@data-date='%d']", expectedDay));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker"))).click();

        while (true) {
            String currentMonth = driver.
                    findElement(By.xpath("//div[@class='ui-datepicker-title']/span[1]")).
                    getText().toUpperCase();
            int currentYear = Integer.parseInt(driver.
                    findElement(By.xpath("//div[@class='ui-datepicker-title']/span[2]")).getText());
            if (currentYear == expectedYear && currentMonth.equals(expectedMonth)) {
                break;
            }
            else {
                driver.findElement(directionLocator).click();
            }
        }
        driver.findElement(dayLocator).click();
    }

    @Test
    void testMouseOperations() {
        driver.get("https://testautomationpractice.blogspot.com/");
        WebElement source = driver.findElement(By.xpath("//p[text()='Drag me to my target']"));
        WebElement target = driver.findElement(By.xpath("//p[text()='Drop here']"));
        Actions actions = new Actions(driver);
        actions
                .scrollToElement(driver.findElement(By.id("draggable")))
                .dragAndDrop(source, target)
                .perform();
        String txt = driver.findElement(By.id("droppable")).getText();
        Assert.assertEquals(txt, "Dropped!");
    }

    @Test
    void testJsExecutor() {
        // JS Executor is an interface, can be used when standard methods lead to ElementInterceptedException
        // we can also perform some other actions such as zoom in/out, scroll to view etc
        driver.get("https://testautomationpractice.blogspot.com/");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        WebElement inputBox = driver.findElement(By.id("name"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement button = driver.findElement(By.name("start"));
        javascriptExecutor.executeScript("arguments[0].click", button);
        javascriptExecutor.executeScript("arguments[0].setAttribute('value', 'John')", inputBox);
        javascriptExecutor.executeScript("scrollTo(0, 2000)");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("comboBox"))).click();
        driver.findElement(By.xpath("//div[@id='dropdown']/div[text()='Item 11']")).click();
    }
}
