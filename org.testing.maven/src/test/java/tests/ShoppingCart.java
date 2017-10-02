package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.io.File;
import java.io.IOException;

public class ShoppingCart extends SeleniumMethods{
	
	@Test
	public void shoppingCartTest() throws IOException, InterruptedException {
		
		// Create Test for report
		test = extent.createTest("Shopping Cart");
		
		// Setting Chromedriver binary directory
		//ChromeOptions options = new ChromeOptions();
		//options.setBinary(new File("C://chrome//chromedriver.exe"));
		
		
		// Create Webdriver instances
		System.setProperty("webdriver.chrome.driver","C:\\chrome\\chromedriver.exe");
		//DesiredCapabilities cap = DesiredCapabilities.chrome();
		//cap.setPlatform(Platform.WINDOWS);
		//cap.setBrowserName("chrome");
		//WebDriver driver = new RemoteWebDriver(new URL("http://192.168.1.67:7777/wd/hub"), cap);
		WebDriver driver = new ChromeDriver();
		//driver.manage().deleteAllCookies();
		WebDriverWait waitForObject = new WebDriverWait(driver, 5);
		
		// Get partial URL and construct complete URL
		String url = getData("Url");
		String completeUrl = "https://" + url;
				
		// Maximize window and go to site
		driver.manage().window().maximize();
		driver.navigate().to(completeUrl);
		SeleniumMethods.waitingTime(3);
		
		// Check for carousel
		String name = waitForObject.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[contains(@class, 'carousel')]//p[contains(@class, 'phoneName')])[1]"))).getText();
		String price = waitForObject.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[contains(@class, 'carousel')]//span[contains(@ng-if, 'Price')])[1]"))).getText();
		SeleniumMethods.carouselData(name, price);
		
		// Enter shop
		Actions builder = new Actions(driver);
		builder.moveToElement(driver.findElement(By.xpath("(//div[contains(@class, 'header_container')]//a[contains(@class, 'dropdown')])[1]"))).perform();
		SeleniumMethods.waitingTime(3);
		driver.findElement(By.xpath("(//div[contains(@class, 'header_container')]//li[contains(@class, 'dropdown')])[1]//a[contains(text(), 'All')]")).click();
		SeleniumMethods.waitingTime(5);
		
		// Change filter
		Select select01 = new Select(driver.findElement(By.xpath("//div[contains(@class, 'dropdown pull-left')]//select[contains(@class, 'sortby')]")));
		select01.selectByVisibleText("Price: Low to High");
		test.info(MarkupHelper.createLabel("Filter changed for 'Price: Low to High'.", ExtentColor.GREEN));
		
		// iPhone Filter
		JavascriptExecutor phoneFilterExecutor = (JavascriptExecutor)driver;
		WebElement phoneFilter = driver.findElement(By.xpath("//div[contains(@class, 'panel-default')]//a[contains(text(), 'Types')]"));
		phoneFilterExecutor.executeScript("arguments[0].click();", phoneFilter);
		test.info(MarkupHelper.createLabel("iPhone filter set.", ExtentColor.GREEN));
		
		// Click on checkbox element
        WebElement checkboxElement = driver.findElement(By.xpath("//section[contains(@class,'phonewall')]/div[2]/div[2]/div[2]/div[1]/div[contains(@class,'panel-collapse')]/div[contains(@class,'panel-body')]/div[contains(@class,'ng-scope')]/form/label[2]/input"));
        checkboxElement.click();        
        SeleniumMethods.waitingTime(5);
        
        // Price comparison
        String price01 = driver.findElement(By.xpath("//section[contains(@class,'phonewall')]/div[contains(@id,'phoneListArea')]/div[3]/div[1]/div[4]/div[contains(@class,'prices')]/h2")).getText();
        String price02 = driver.findElement(By.xpath("//section[contains(@class,'phonewall')]/div[contains(@id,'phoneListArea')]/div[3]/div[2]/div[4]/div[contains(@class,'prices')]/h2")).getText();
        SeleniumMethods.compareStrings(price01, price02);
        
        // Add phone to the shopping cart
        driver.findElement(By.xpath("//section[contains(@class,'phonewall')]/div[contains(@id,'phoneListArea')]/div/div/div[contains(@class,'phone')]/div[contains(@class,'pull-right')]/form/span[contains(@class,'ng-scope')]/a[contains(@class,'btn')]")).click();
        test.info(MarkupHelper.createLabel("Phone added to the 'Shopping Cart'", ExtentColor.GREEN));
        SeleniumMethods.waitingTime(10);
        
        // Check Cart Number
        String cartNumber = driver.findElement(By.xpath("//section[contains(@id,'checkout_step_1')]/div[1]/div[1]/form/div/div/div[2]/input")).getAttribute("value");
        SeleniumMethods.compareToZero(cartNumber);
        
        // Filling shopping cart with 4 elements
        driver.findElement(By.xpath("//section[contains(@id,'checkout_step_1')]/div[1]/div[1]/form/div/div/div[2]/input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.xpath("//section[contains(@id,'checkout_step_1')]/div[1]/div[1]/form/div/div/div[2]/input")).sendKeys("4");
        
        // Taking Screenshoot
        SeleniumMethods.captureScreenshot(driver, "ShopCartScreen");
        test.info(MarkupHelper.createLabel("4 items on cart validation.", ExtentColor.GREEN));
        
        // Filling shopping cart with 2 elements
        driver.findElement(By.xpath("//section[contains(@id,'checkout_step_1')]/div[1]/div[1]/form/div/div/div[2]/input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.xpath("//section[contains(@id,'checkout_step_1')]/div[1]/div[1]/form/div/div/div[2]/input")).sendKeys("2");
        test.info(MarkupHelper.createLabel("Adding 2 items on the cart.", ExtentColor.GREEN));
        
        // First Name
        String firstName = getData("FirstName");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='firstName']")).sendKeys(firstName);

        // Last Name
        String lastName = getData("LastName");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='lastName']")).sendKeys(lastName);

        // Address
        String cartAddress = getData("Address");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='address1']")).sendKeys(cartAddress);

        // City
        String city = getData("City");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='city']")).sendKeys(city);

        // Zipcode
        String zipcode = getData("Zipcode");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='zipCode']")).sendKeys(zipcode);
        
        // State
        Select select02 = new Select(driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/select[contains(@class, 'form-control')]")));
        String state = getData("State");
        select02.selectByVisibleText(state);

        // Phone
        String phone = getData("Phone");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='phoneNumber']")).sendKeys(phone);
        
        // Email
        String email = getData("Mail");
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='email']")).sendKeys(email);
        driver.findElement(By.xpath("//section[contains(@class, 'billing_form')]/div/form[contains(@class,'form-group')]/div/input[@id='validateEmail']")).sendKeys(email);
        
        // Card Type
        driver.findElement(By.xpath("//section[contains(@class, 'payment_form')]/div/form/div/div/div/label/input[contains(@value,'VISA')]")).click();

        // Card Number
        String card = getData("Card");
        driver.findElement(By.xpath("//section[contains(@class, 'payment_form')]/div/form/div/div/div/input[@id='cardNumber']")).sendKeys(card);
        
        // Select month
        Select select03 = new Select(driver.findElement(By.xpath("//section[contains(@class, 'payment_form')]/div/form/div/div/div/select[@id='expirationMonth']")));
        String month = getData("Month");
        select03.selectByVisibleText(month);

        // Year
        Select select04 = new Select(driver.findElement(By.xpath("//section[contains(@class, 'payment_form')]/div/form/div/div/div/select[contains(@class,'expireYearSelect')]")));
        String year = getData("Year");
        select04.selectByVisibleText(year);

        // Security Code
        String code = getData("Code");
        driver.findElement(By.xpath("//section[contains(@class, 'payment_form')]/div/form/div/div/div/input[@id='securityCode']")).sendKeys(code);

        // Terms and Conditions
        driver.findElement(By.xpath("//*[@id='checkout_step_1']/div[1]/section[contains(@class,'terms-conditions')]/div[contains(@class,'tcs')]/label/div/input[contains(@type,'checkbox')]")).click();
        test.info(MarkupHelper.createLabel("Information added to order.", ExtentColor.GREEN));
        
        // Review Order button
        driver.findElement(By.xpath("//section[@id='checkout_step_1']/div[1]/section[contains(@class,'summary')]/div[contains(@class,'finalamount')]/ul/a[contains(@class,'btn')]")).click();
        SeleniumMethods.waitingTime(5);

        // Taking Screenshoot
        SeleniumMethods.captureScreenshot(driver, "EvidenceScreen");
        
        Assert.assertTrue(true,"Test Completed.");
		driver.close();
	}
	
	
}
