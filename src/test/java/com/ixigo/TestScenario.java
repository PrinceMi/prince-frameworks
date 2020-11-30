package com.ixigo;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import koch.test.framework.ExtentReportClass.ExtentReportClassObject;
import koch.test.framework.ExtentReportClass;
import koch.test.framework.CommonFunctions;
import koch.test.framework.CommonObjects;
import koch.test.framework.CommonObjects.HashMapNew;

public class TestScenario {
	WebDriver driver;
	CommonObjects objCommonObjects;
	CommonFunctions objCommonFunctions;
	private ExtentReportClass objExtentReportClass;
	private ExtentReportClassObject objExtentReportClassObject;

	@BeforeSuite
	public void beforeSuite() {
		objExtentReportClass = new ExtentReportClass();
		objExtentReportClassObject = objExtentReportClass.setupReport();
	}

	@Parameters("browser")
	@BeforeTest
	public void beforeTest(String browser) {
		String className = this.getClass().getName();
		className = (className.substring(className.lastIndexOf(".") + 1));
		objCommonObjects = new CommonObjects(className);
	}

	@Parameters("browser")
	@BeforeMethod
	public void beforeMethod(@Optional("CHROME") String browser, Method m) {
		driver = objCommonObjects.getWebDriver(browser);
		objCommonFunctions = new CommonFunctions(driver);
		objExtentReportClass.getTest(objExtentReportClassObject, m.getName(), driver, browser);
	}

	@Test(dataProvider = "getTestData")
	public void getFlightBookingPrice(HashMapNew dataDictionary) {
		Assert.assertTrue(objCommonFunctions.launchEnv(dataDictionary.get("Test_URL")));

		Homepage objHomepage = new Homepage(driver, objExtentReportClass);
		Assert.assertTrue(objHomepage.isHomePageDisplayed());
		Assert.assertTrue(objHomepage.enterFromLocation(dataDictionary.get("FromLocation")));
		Assert.assertTrue(objHomepage.enterToLocation(dataDictionary.get("ToLocation")));
		Assert.assertTrue(objHomepage.enterDepartureDate(dataDictionary.get("DepartureDate")));
		Assert.assertTrue(
				objHomepage.enterReturnDate(dataDictionary.get("DepartureDate"), dataDictionary.get("ReturnDate")));
		Assert.assertTrue(objHomepage.selectAdultTravellers(dataDictionary.get("NumOfTravelers")));
		Assert.assertTrue(objHomepage.clickOnSearchButton());
		
		SearchResult objSearchResult = new SearchResult(driver, objExtentReportClass);
		Assert.assertTrue(objSearchResult.isSearchResultPageDisplayed());
		Assert.assertTrue(objSearchResult.isStopFilterDisplayed());
		Assert.assertTrue(objSearchResult.isDepartureTimeFilterDisplayed());
		Assert.assertTrue(objSearchResult.isAirlinesFilterDisplayed());
		Assert.assertTrue(objSearchResult.selectNonStopFilter());
		Assert.assertTrue(objSearchResult.displayFlightDetailsBelowPrice("PriceLimit"));
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		objExtentReportClass.setFinalStatus(objExtentReportClassObject, result);
		objCommonObjects.closeAllDrivers();
	}

	@AfterSuite
	public void afterSuite() {
		objExtentReportClass.writeToHtml(objExtentReportClassObject);
	}

	@DataProvider(name = "getTestData")
	public Object[][] getTestData() {
		List<HashMapNew> dataFromExcel = objCommonObjects.readTestDataFromExcel();
		int testRunCount = dataFromExcel.size();
		Object dataProviderObject[][] = new Object[testRunCount][1];
		for (int i = 0; i < testRunCount; ++i)
			dataProviderObject[i][0] = dataFromExcel.get(i);
		for (int i = 0; i < testRunCount; ++i)
			System.out.println(dataProviderObject[i][0]);
		return dataProviderObject;

	}

}
