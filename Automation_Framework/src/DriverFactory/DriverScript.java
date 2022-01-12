package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
String inputpath ="D:\\Ojtmrng\\Automation_Framework\\TestInput\\HybridTest.xlsx";
String outputpath ="D:\\Ojtmrng\\Automation_Framework\\TestOutPut\\HybridResults.xlsx";
ExtentReports report;
ExtentTest test;
public void startTest()throws Throwable
{
	
	
	//access excel methods
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in MasterTestCases sheet
	for(int i=1; i<=xl.rowCount("MasterTestCases");i++)
	{
		String moduleStatus="";
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//store corresponding sheet into TCModule
			String TCModule =xl.getCellData("MasterTestCases", i, 1);
			report = new ExtentReports("./Reports/"+TCModule+FunctionLibrary.generateDate()+" "+".html");
			//iterate all in TCModule sheet
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				test=report.startTest(TCModule);
				String Description=xl.getCellData(TCModule, j, 0);
				String FunctionName =xl.getCellData(TCModule, j, 1);
				String LocatorType =xl.getCellData(TCModule, j, 2);
				String LocatorValue =xl.getCellData(TCModule, j, 3);
				String TestData =xl.getCellData(TCModule, j, 4);
				//call methods
				try
				{
				if(FunctionName.equalsIgnoreCase("startBrowser"))
				{
				driver=	FunctionLibrary.startBrowser(driver);
				test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("openApplication"))
				{
					FunctionLibrary.openApplication(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("waitForElement"))
				{
					FunctionLibrary.waitForElement(driver, LocatorType, LocatorValue, TestData);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("typeAction"))
				{
					FunctionLibrary.typeAction(driver, LocatorType, LocatorValue, TestData);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("clickAction"))
				{
					FunctionLibrary.clickAction(driver, LocatorType, LocatorValue);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("validateTitle"))
				{
					FunctionLibrary.validateTitle(driver, TestData);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("closeBrowser"))
				{
					FunctionLibrary.closeBrowser(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("captureData"))
				{
					FunctionLibrary.captureData(driver, LocatorType, LocatorValue);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("validateSupplierTable"))
				{
					FunctionLibrary.validateSupplierTable(driver, TestData);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("mouseClick"))
				{
					FunctionLibrary.mouseClick(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(FunctionName.equalsIgnoreCase("stockTable"))
				{
					FunctionLibrary.stockTable(driver, TestData);
					test.log(LogStatus.INFO, Description);
				}
				//write as pass into status cell in TCModule
				xl.setCellData(TCModule, j, 5, "Pass", outputpath);
				test.log(LogStatus.PASS, Description);
				moduleStatus="True";
				
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as Fail into status cell in TCModule
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Description);
					moduleStatus="False";
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					
					FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".png"));
					
				}
				if(moduleStatus.equalsIgnoreCase("True"))
				{
					xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
				}
				if(moduleStatus.equalsIgnoreCase("False"))
				{
					xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
				}
			}
		}
		else
		{
			//write as blocked into results sheet
			xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
		}
		report.endTest(test);
		report.flush();
	}
}

}
