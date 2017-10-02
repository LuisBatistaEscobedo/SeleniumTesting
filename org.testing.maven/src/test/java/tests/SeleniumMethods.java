package tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class SeleniumMethods {
	
	public static DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat screenday = new SimpleDateFormat("yyyyMMdd");
	//private static DateTimeFormatter screenhour = DateTimeFormatter.ofPattern("HHmmss");

	// Report elements  created
	static Properties properties;
	static ExtentHtmlReporter htmlReport;
	static ExtentReports extent;
	static ExtentTest test;
	
	//Screenshot folder created
	static Calendar cal = Calendar.getInstance();
	//static File directory = new File(System.getProperty("user.dir") + "//src//ExtentReport//Screenshots" + screenday.format(cal.getTime()));
	static File directory = new File(System.getProperty("user.dir") + "//src//ExtentReport//Screenshots");
	static String fileDirectory = directory.toString();
	
	public static void loadData() throws IOException{
		properties = new Properties();
		File f = new File(System.getProperty("user.dir")+"//src//test//java//tests//data//data.properties");
		FileReader obj = new FileReader(f);
		properties.load(obj);
	}
	
	public static void captureScreenshot(WebDriver driver, String name) throws IOException {
		
		if(!directory.exists()){
			directory.mkdir();
		}
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//String path = fileDirectory + "//" + name + "_" + screenhour.format(LocalDateTime.now()) + ".png";
		String path = fileDirectory + "//" + name + ".png";
        FileUtils.copyFile(scrFile, new File(path));
        test.pass("Screenshot below: ", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        
	}
	
	public static String getData(String Data) throws IOException{
		loadData();
		String data = properties.getProperty(Data);
		return data;
	}
	
	public static void waitingTime(int sec) throws InterruptedException {
		Thread.sleep(sec * 1000);
	}
		
	public static void compareStrings(String element01, String element02)
    {

        if (Double.parseDouble(element01) <= Double.parseDouble(element02))
        {
            test.info(MarkupHelper.createLabel("Comparison made correctly.", ExtentColor.GREEN));
        }
        else
        {
        	test.info(MarkupHelper.createLabel("Error: error in comparison.", ExtentColor.RED));
        	Assert.assertTrue(false, "Error on prices.");
        }
    }
	
	public static void compareToZero(String value)
    {
        if (Integer.parseInt(value) == 0)
        {
        	test.info(MarkupHelper.createLabel("Error: Value is not equal to 0.", ExtentColor.RED));
        	Assert.assertTrue(false, "Value is not equal to 0.");
        }
        else
        {
        	test.info(MarkupHelper.createLabel("Number of items on the cart: " + value , ExtentColor.GREEN));
        }
    }
	
	public static void carouselData(String name, String price)
	{
		if(name != null && price != null) {
			
			test.info(MarkupHelper.createLabel("First phone on the carousel: " + name + " and price: " + price, ExtentColor.GREEN));
		}
		else {
			test.info(MarkupHelper.createLabel("Error: Carousel is not visible.", ExtentColor.RED));
			Assert.assertTrue(false, "Test Failed.");
		}
	}
	
	@BeforeTest
	public void startReport() throws IOException {
		
		Calendar cal = Calendar.getInstance();
		htmlReport = new ExtentHtmlReporter(System.getProperty("user.dir") + "//src//ExtentReport//TestReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReport);
		
		String url = getData("Url");
		
		// Check for the ID on Host
		InetAddress address = InetAddress.getByName(url);
		String ip = address.getHostAddress();
		
		extent.setSystemInfo("Execution Date", day.format(cal.getTime()));
		extent.setSystemInfo("Platform", "Jenkins");
		extent.setSystemInfo("Plug-In", "Maven");
		extent.setSystemInfo("IP Address", ip);
		extent.setSystemInfo("Enviroment", "Prod");
		extent.setSystemInfo("URL", getData("Url"));
		
		htmlReport.config().setDocumentTitle("Continous Testing");
		htmlReport.config().setReportName("Shopping Cart Report");
		htmlReport.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReport.config().setTheme(Theme.DARK);
	}
	
	@AfterMethod
    public void getResult(ITestResult result)
    {
        if(result.getStatus() == ITestResult.FAILURE)
        {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
            test.fail(result.getThrowable());
        }
        else if(result.getStatus() == ITestResult.SUCCESS)
        {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
        }
        else
        {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }
	
	@AfterTest
	public void tearDown() {
		extent.flush();
	}
	
	
	
}