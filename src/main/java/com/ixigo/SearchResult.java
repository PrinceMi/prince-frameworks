package com.ixigo;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import koch.test.framework.CommonFunctions;
import koch.test.framework.ExtentReportClass;

public class SearchResult {
	private CommonFunctions objCommonFunctions;
	private ExtentReportClass objExtentReportClass;

	private String flightLoader = "xpath:=//*[@class='poll-ldr']";
	private String bookBtn = "xpath:=//button[contains(text(),'Book')]";
	private String stopFilterHdr = "xpath:=//div[@class='fltr-hdr' and text()='Stops']";
	private String departureTimeFilter = "xpath:=//div[@class='fltr-hdr' and contains(text(),'Departure from xxxx')]";
	private String airlinesFilter = "xpath:=//div[@class='fltr-hdr' and text()='Airlines']";
	private String depAirportCode = "xpath:=//div[text()='From']/following-sibling::input";
	private String nonStopFilter = "xpath:=//div[text()='Non stop']";
	private String listOfResultPrice = "xpath:=(//div[contains(@class,'result')]//div[contains(@class,'flight-listing')])//div[@class='price']";
	private String listOfAirlineNumber = "xpath:=(//div[contains(@class,'result')]//div[contains(@class,'flight-listing')])//div[@class='airline-text']";
	private String listOfDepTime = "xpath:=(//div[contains(@class,'result')]//div[contains(@class,'flight-listing')])//div[@class='time'][1]";

	public SearchResult(WebDriver driver, ExtentReportClass objExtentReportClass) {
		objCommonFunctions = new CommonFunctions(driver);
		this.objExtentReportClass = objExtentReportClass;
	}

	// *****************************************************************************************
	// Description: Function that verifies if the page is displayed with search
	// result
	// *****************************************************************************************
	public boolean isSearchResultPageDisplayed() {
		if ((objCommonFunctions.getWebElementsListWithRetry(bookBtn).size() > 0)
				&& objCommonFunctions.waitTillElementIsNotDisplayedAnymore(flightLoader)) {
			objExtentReportClass.reportLog(Status.PASS, "Search result has completely loaded");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Search result has not loaded");
		return false;
	}

	// *****************************************************************************************
	// Description: Verify Stops filter is displayed
	// *****************************************************************************************
	public boolean isStopFilterDisplayed() {
		if (objCommonFunctions.waitTillElementIsDisplayed(stopFilterHdr)) {
			objExtentReportClass.reportLog(Status.PASS, "Stops filter is displayed");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Stops filter is not displayed");
		return false;
	}

	// *****************************************************************************************
	// Description: Verify Stops filter is displayed
	// *****************************************************************************************
	public boolean isDepartureTimeFilterDisplayed() {
		if (objCommonFunctions.waitTillElementIsDisplayed(
				departureTimeFilter.replace("xxxx", (getDepartureAirportCode().split("-")[0]).trim()))) {
			objExtentReportClass.reportLog(Status.PASS, "Departure time filter");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Departure time filter is not");
		return false;
	}

	// *****************************************************************************************
	// Description: Verify Stops filter is displayed
	// *****************************************************************************************
	public boolean isAirlinesFilterDisplayed() {
		if (objCommonFunctions.waitTillElementIsDisplayed(airlinesFilter)) {
			objExtentReportClass.reportLog(Status.PASS, "Airlines filter is displayed");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Airlines filter is not displayed");
		return false;
	}

	// *****************************************************************************************
	// Description: Verify Stops filter is displayed
	// *****************************************************************************************
	private String getDepartureAirportCode() {
		return objCommonFunctions.getAttributeValueOfElement(depAirportCode, "value");
	}

	// *****************************************************************************************
	// Description: Select Non-Stop filter
	// *****************************************************************************************
	public boolean selectNonStopFilter() {
		if (objCommonFunctions.clickOnElement(nonStopFilter)) {
			objExtentReportClass.reportLog(Status.FAIL, "Selected Non stop filter");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Not able to select Non stop filter");
		return false;
	}

	// *****************************************************************************************
	// Description: Display the flight options below the given price argument
	// Param: String price: fare below which the flight details have to be displayed
	// *****************************************************************************************
	public boolean displayFlightDetailsBelowPrice(String price) {
		List<WebElement> priceList = objCommonFunctions.getWebElementsListWithRetry(listOfResultPrice);
		List<WebElement> airlineNumList = objCommonFunctions.getWebElementsListWithRetry(listOfAirlineNumber);
		List<WebElement> depTimeList = objCommonFunctions.getWebElementsListWithRetry(listOfDepTime);

		if (priceList != null && airlineNumList != null && depTimeList != null) {
			for (int i = 0; i < priceList.size(); ++i) {
				String tempPrice = objCommonFunctions.getInnerTextOfWebElement(priceList.get(i));
				if (Integer.parseInt(tempPrice) < 6000) {
					String tempFlightAirlineNum = objCommonFunctions.getInnerTextOfWebElement(airlineNumList.get(i));
					String tempFlightDepTime = objCommonFunctions.getInnerTextOfWebElement(depTimeList.get(i));
					objExtentReportClass.reportLog(Status.INFO,
							tempFlightAirlineNum + ": " + tempFlightDepTime + ": " + tempPrice);
					System.out.println(tempFlightAirlineNum + ": " + tempFlightDepTime + ": " + tempPrice);
				}
			}
			objExtentReportClass.reportLog(Status.PASS, "Flights details are displayed");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Failed to get the list of available flights");
		return false;
	}

}
