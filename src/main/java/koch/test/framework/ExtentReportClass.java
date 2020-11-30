package koch.test.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ExtentReportClass {
	private WebDriver driver;
	private ExtentHtmlReporter objExtentHtmlReporter;
	private ExtentReports objExtentReports;
	private ExtentTest objExtentTest;
	ExtentReportClassObject objExtentReportClassObject;
	
	public class ExtentReportClassObject {
		private ExtentHtmlReporter objExtentHtmlReporter;
		private ExtentReports objExtentReports;
		private ExtentTest objExtentTest;
		
		public ExtentReportClassObject(ExtentHtmlReporter objExtentHtmlReporter, ExtentReports objExtentReports) {
			this.objExtentHtmlReporter = objExtentHtmlReporter;
			this.objExtentReports = objExtentReports;
		}
		
		public void setExtentTest(ExtentTest objExtentTest) {
			this.objExtentTest = objExtentTest;
		}
		
		public ExtentReports getExtentReports() {
			return objExtentReports;
		}

		public ExtentTest getExtentTest() {
			return objExtentTest;
		}
	}
	
	/*
	 * User defined function to create file/ folder name uniquely
	 */
	private String getUniqueFileName() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).replaceAll("[\\W]|_", "");
	}
	
	/*
	 * User defined function that check if the test-output folders are present, if not it will create the desired folders
	 */
	private void checkDirectory() {
		File reporterFolder = new File("./test-output/extentreport");
		File scrrenshotFolder= new File("./test-output/scrrenshots");
		if(!reporterFolder.exists())
			reporterFolder.mkdir();
		if(!scrrenshotFolder.exists())
			scrrenshotFolder.mkdir();
	}
	
	/*
	 * User defined function that setup the report file data
	 */
	public ExtentReportClassObject setupReport() {
		checkDirectory();
		objExtentHtmlReporter = new ExtentHtmlReporter("./test-output/extentreport/testReport"+ getUniqueFileName() + ".html");
		objExtentHtmlReporter.config().setDocumentTitle("Koch Test Report");
		objExtentHtmlReporter.config().setReportName("Koch Test Report");
		objExtentHtmlReporter.config().setTheme(Theme.STANDARD);
		objExtentHtmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
		objExtentReports = new ExtentReports();
		objExtentReports.attachReporter(objExtentHtmlReporter);
		objExtentReportClassObject = new ExtentReportClassObject(objExtentHtmlReporter, objExtentReports);
		return objExtentReportClassObject;
	}
	
	/*
	 * User defined function that fetches the data before every Method execution
	 */
	public void getTest(ExtentReportClassObject objExtentReportClassObject, String testName, WebDriver driver, String browser) {
		this.driver = driver;
		this.objExtentTest = objExtentReportClassObject.getExtentReports().createTest(testName, "<br/>Browser: " + browser);
		objExtentReportClassObject.setExtentTest(objExtentTest);
	}
	
	/*
	 * User defined function that set final status(in AfterMethod) of the text that is executed
	 */
	public void setFinalStatus(ExtentReportClassObject objExtentReportClassObject, ITestResult result) {
		objExtentTest = objExtentReportClassObject.getExtentTest();
		if(result.getStatus() == ITestResult.FAILURE) {
			objExtentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + "Failed", ExtentColor.RED));
			objExtentTest.fail(result.getThrowable());
		} else if(result.getStatus() == ITestResult.SUCCESS) {
			objExtentTest.log(Status.PASS, MarkupHelper.createLabel(result.getName() + "PASSED", ExtentColor.GREEN));
		}
	}
	
	/*
	 * Write the execution data to HTML file
	 */
	public void writeToHtml(ExtentReportClassObject objExtentReportClassObject) {
		objExtentReportClassObject.getExtentReports().flush();
	}
	
	/*
	 * User defined function to log each step detail with screenshot
	 */
	public void reportLog(Status status, String message) {
		objExtentTest.log(status, message);
		try {
			objExtentTest.addScreenCaptureFromPath(takeScreenShot());
		} catch(Exception e) {
			objExtentTest.log(Status.WARNING, "Unable to attach scrrensot to report");
			Reporter.log("Unable to attach scrrensot to report");
		}
		Reporter.log(message);
	}
	
	/*
	 * User defined function to take screenshot
	 */
	private String takeScreenShot() {
		String path = System.getProperty("user.dir") + "/test-output/scrrenshots/scrrenshotName" + getUniqueFileName() + ".jpg";
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			File destination = new File(path);
			FileUtils.copyFile(source, destination);
		} catch(Exception e) {
			objExtentTest.log(Status.WARNING, "Unable to attach scrrensot to report");
			Reporter.log("Unable to attach scrrensot to report");
		}
		return path;
	}
}
