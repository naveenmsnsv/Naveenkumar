package CommonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import Utilities.PropertyFileUtil;

public class FunctionLibrary {
	WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser(WebDriver driver)throws Throwable
	{
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
		{
			
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();

		}
		else
		{
			Reporter.log("Browser value is not macthing",true);
		}
		return driver;
	}
	//method for launch url
	public static void openApplication(WebDriver driver)throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("AppUrl"));
		

	}
	//method for wait for element
	public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String wait)
	{
		WebDriverWait myWait = new WebDriverWait(driver, Integer.parseInt(wait));
		if(locatortype.equalsIgnoreCase("name"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
		}
		else
		{
			Reporter.log("Unable to execute waitforelement method",true);
		}
	}
	//method for text boxes
	public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,
			String testdata)
	{
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).clear();
			driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).clear();
			driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).clear();
			driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
		}
		else
		{
			Reporter.log("Unable to execute typeAction method",true);
		}
	}
	//method for click action
	public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
	{
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).click();
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();
		}
		else
		{
			Reporter.log("Unable to execute clickaction method",true);
		}
	}
	public static void validateTitle(WebDriver driver,String expectedtitle)
	{
		String actualtitle=driver.getTitle();
		try {
			Assert.assertEquals(actualtitle, expectedtitle,"Title is Not Matching");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	public static void closeBrowser(WebDriver driver)
	{
		driver.close();
	}
	//method for capture data into note pad
	public static void captureData(WebDriver driver,String locatortype,
			String locatorvalue)throws Throwable
	{
		String suppliernum ="";
		if(locatortype.equalsIgnoreCase("name"))
		{
			suppliernum=driver.findElement(By.name(locatorvalue)).getAttribute("value");	
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			suppliernum = driver.findElement(By.id(locatorvalue)).getAttribute("value");
		}
		//create new file
		File f = new File("./CaptureData/supplier.txt");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(suppliernum);
		bw.flush();
		bw.close();
		
	}
	//method for supplier table validation
	public static void validateSupplierTable(WebDriver driver,String column)throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/supplier.txt");
		BufferedReader br = new BufferedReader(fr);
		String expectedsuppliernum =br.readLine();
		//convert column number in to integer type
		int colNum =Integer.parseInt(column);
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
		//if search textbox not displayed clcik serch panel
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(expectedsuppliernum);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(5000);
		WebElement table =driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("web-table")));
		//get collection of rows
		List<WebElement> rows =table.findElements(By.tagName("tr"));
		for(int i=1;i<rows.size();i++)
		{
		String actualsuppliernum =driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Assert.assertEquals(actualsuppliernum, expectedsuppliernum);
		Reporter.log(actualsuppliernum+"   "+expectedsuppliernum,true);
		break;
		}
		}
	//method for mouse click
	public static void mouseClick(WebDriver driver)throws Throwable
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(driver.findElement(By.xpath("//body/div[2]/div[2]/div[1]/div[1]/ul[1]/li[2]/a[1]"))).perform();
		Thread.sleep(4000);
		ac.moveToElement(driver.findElement(By.xpath("//body/div[2]/div[2]/div[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]"))).click().perform();
		Thread.sleep(4000);
	}
	//method stock table
	public static void stockTable(WebDriver driver,String testData)throws Throwable
	{
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(testData);
		Thread.sleep(4000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(4000);
		WebElement table = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("web-table1")));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for(int i=1;i<=rows.size();i++)
		{
		String actualdata =driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr[1]/td[4]/div/span/span")).getText();
		Assert.assertEquals(actualdata, testData);
		Reporter.log(actualdata+"    "+testData,true);
		break;
		}
	}
	//method for date generate
	public static String generateDate()
	{
		Date d= new Date();
	DateFormat datef = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
	return datef.format(d);
	}
}











