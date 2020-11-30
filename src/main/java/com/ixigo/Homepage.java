package com.ixigo;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import koch.test.framework.CommonFunctions;
import koch.test.framework.ExtentReportClass;

public class Homepage {
	private CommonFunctions objCommonFunctions;
	private ExtentReportClass objExtentReportClass;

	private static final int DEPARTURE = 1;
	private static final int RETURN = 2;

	private String searchBtn = "xpath:=//button[contains(text(),'Search')]";
	private String fromLocation = "xpath:=//div[text()='From']/following-sibling::input";
	private String toLocation = "xpath:=//div[text()='To']/following-sibling::input";
	private String locationSuggestionList = "xpath:=//div[contains(@class,'autocompleter-results-caret') and contains(@style,'block')]//div[@class='city']";
	private String departureDatePicker = "xpath:=//div[text()='Departure']/following-sibling::input";
	private String returnDatePicker = "xpath:=//div[text()='Return']/following-sibling::input";
	private String depCalMonthHdr = "xpath:=//div[contains(@class,'flight-dep-cal')]//div[contains(@class,'month-label')]";
	private String retCalMonthHdr = "xpath:=//div[contains(@class,'flight-ret-cal')]//div[contains(@class,'month-label')]";
	private String depCalNextBtn = "xpath:=//div[contains(@class,'flight-dep-cal')]//button[contains(@class,'rd-next')]";
	private String retCalNextBtn = "xpath:=//div[contains(@class,'flight-ret-cal')]//button[contains(@class,'rd-next')]";
	private String depDateOnCal = "xpath:=//div[contains(@class,'flight-dep-cal')]//td[@data-date='xxxx']";
	private String retDateOnCal = "xpath:=//div[contains(@class,'flight-ret-cal')]//td[@data-date='xxxx']";
	private String travellerSelector = "xpath:=//div[text()='Travellers | Class']/following-sibling::input";
	private String adultTravellerCount = "xpath:=//div[text()='Adult']/../following-sibling::div/span[@data-val='xxxx']";

	public Homepage(WebDriver driver, ExtentReportClass objExtentReportClass) {
		objCommonFunctions = new CommonFunctions(driver);
		this.objExtentReportClass = objExtentReportClass;
	}

	// *****************************************************************************************
	// Description: Function that verifies if the page is displayed
	// *****************************************************************************************
	public boolean isHomePageDisplayed() {
		if (objCommonFunctions.waitTillElementIsDisplayed(searchBtn)) {
			objExtentReportClass.reportLog(Status.PASS, "Homepage is displayed");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Homepage is not displayed");
		return false;
	}

	// *****************************************************************************************
	// Description: Enter location
	// Param: String locType: String locator for the input box
	// Param: String location: String value for the To/ From location
	// *****************************************************************************************
	private boolean enterLocation(String locType, String location) {
		if (objCommonFunctions.clearAndEnterValueInTextFeild(locType, location)) {
			objExtentReportClass.reportLog(Status.PASS, "Location is entered successfully");
			List<WebElement> suggestionList = objCommonFunctions
					.getWebElementsListWithRetryTillContains(locationSuggestionList, location);
			if (suggestionList != null) {
				for (WebElement we : suggestionList) {
					if (objCommonFunctions.getInnerTextOfWebElement(we).contains(location))
						if (objCommonFunctions.clickOnElement(we)) {
							objExtentReportClass.reportLog(Status.PASS,
									"Selected location: " + objCommonFunctions.getInnerTextOfWebElement(we) + " from sugestion list successfully");
							return true;
						}
				}
				objExtentReportClass.reportLog(Status.FAIL, location + ": location not displayed in suggestion list");
				return false;
			}
			objExtentReportClass.reportLog(Status.FAIL, "Auto suggestion options are not displayed");
			return false;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Not able to enter location");
		return false;
	}

	// *****************************************************************************************
	// Description: Enter from location
	// Param: String location: String value for the From location
	// *****************************************************************************************
	public boolean enterFromLocation(String location) {
		return enterLocation(fromLocation, location);
	}

	// *****************************************************************************************
	// Description: Enter to location
	// Param: String location: String value for the To location
	// *****************************************************************************************
	public boolean enterToLocation(String location) {
		return enterLocation(toLocation, location);
	}

	// *****************************************************************************************
	// Description: Select date
	// Param: int dateType: DEPARTURE(1)/ RETURN(2)
	// Param: String dateLocator: Web locator for the date field
	// Param: String location: Value of date to selected
	// *****************************************************************************************
	public boolean enterDate(int dateType, String dateLocator, String dateVal) {
		String dateOnCal;
		if (dateType == DEPARTURE)
			dateOnCal = depDateOnCal;
		else
			dateOnCal = retDateOnCal;
		if (objCommonFunctions.clickOnElement(dateLocator)) {
			if (reachToDate(dateType, objCommonFunctions.getCalendarYearValue(dateVal),
					objCommonFunctions.getCalendarMonthString(dateVal))) {
				if (objCommonFunctions.clickOnElement(dateOnCal.replaceAll("xxxx", (dateVal.replaceAll("-", ""))))) {
					objExtentReportClass.reportLog(Status.PASS, "Selected the desired date");
					return true;
				}
				objExtentReportClass.reportLog(Status.FAIL, "Unable to click on the desired date");
				return false;
			}
			objExtentReportClass.reportLog(Status.FAIL, "Unable to reach to the desired date");
			return false;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Unable to click on date picker input box");
		return false;
	}

	// *****************************************************************************************
	// Description: Select departure date
	// Param: String location: Value of date to selected
	// *****************************************************************************************
	public boolean enterDepartureDate(String dateVal) {
		if (objCommonFunctions.checkDateValidity(dateVal)) {
			return enterDate(DEPARTURE, departureDatePicker, dateVal);
		}
		objExtentReportClass.reportLog(Status.FAIL, "Data is incorrect: Departure date is in the past");
		return false;
	}

	// *****************************************************************************************
	// Description: Select return date
	// *****************************************************************************************
	public boolean enterReturnDate(String depDateVal, String returnDateVal) {
		if (objCommonFunctions.checkDateValidity(depDateVal, returnDateVal)) {
			return enterDate(RETURN, returnDatePicker, returnDateVal);
		}
		objExtentReportClass.reportLog(Status.FAIL,
				"Data is incorrect: Return date is in the past or after departure date");
		return false;
	}

	// *****************************************************************************************
	// Description: Click next to reach to the desired date on calendar
	// Param: int dateType: DEPARTURE(1)/ RETURN(2)
	// Param: String calYear: Year value to be selected
	// Param: String calMonthString: Month value to be selected
	// *****************************************************************************************
	private boolean reachToDate(int dateType, String calYear, String calMonthString) {
		String calMonthHdr, calNextBtn;
		if (dateType == DEPARTURE) {
			calMonthHdr = depCalMonthHdr;
			calNextBtn = depCalNextBtn;
		} else {
			calMonthHdr = retCalMonthHdr;
			calNextBtn = retCalNextBtn;
		}
		List<WebElement> calMonthList = objCommonFunctions.getWebElementsListWithRetry(calMonthHdr);
		for (WebElement we : calMonthList) {
			if (we.getText().contains(calYear) && we.getText().contains(calMonthString))
				return true;
		}
		if (objCommonFunctions.clickOnElement(calNextBtn))
			return reachToDate(dateType, calYear, calMonthString);
		return false;
	}

	// *****************************************************************************************
	// Description: Select the number of travellers
	// Param: String travellerCount: Number of travellers to be selected
	// *****************************************************************************************
	public boolean selectAdultTravellers(String travellerCount) {
		int tCount = Integer.parseInt(travellerCount.trim());
		if (tCount > 1 && tCount < 10) {
			if (objCommonFunctions.clickOnElement(travellerSelector)) {
				if (objCommonFunctions.clickOnElement(adultTravellerCount.replaceAll("xxxx", travellerCount))) {
					if (getTravellersAndClassDetails().contains(travellerCount + " Passengers")) {
						objExtentReportClass.reportLog(Status.FAIL, "Number of passengers selected: " + travellerCount);
						return true;
					}
					objExtentReportClass.reportLog(Status.FAIL, "Number of travellers are not getting updated");
					return false;
				}
				objExtentReportClass.reportLog(Status.FAIL, "Unable to select the number of travellers");
				return false;
			}
			objExtentReportClass.reportLog(Status.FAIL, "Unable to click on traveller input box");
			return false;
		} else if (tCount < 1 || tCount > 9) {
			objExtentReportClass.reportLog(Status.FAIL,
					"Invalid data: Number of traveller(s) should be greater than 0 and less than 10");
			return false;
		}
		if (getTravellersAndClassDetails().contains(travellerCount + " Passenger")) {
			objExtentReportClass.reportLog(Status.SKIP, "By default 1 traveller is selected");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "No default value of passenger is selected");
		return false;
	}

	// *****************************************************************************************
	// Description: Return the travellers and class details
	// *****************************************************************************************
	private String getTravellersAndClassDetails() {
		return objCommonFunctions.getAttributeValueOfElement(travellerSelector, "value");
	}

	// *****************************************************************************************
	// Description: Return the travellers and class details
	// *****************************************************************************************
	public boolean clickOnSearchButton() {
		if(objCommonFunctions.clickOnElement(searchBtn)) {
			objExtentReportClass.reportLog(Status.PASS, "Clicked on Search button successfully");
			return true;
		}
		objExtentReportClass.reportLog(Status.FAIL, "Not able to click on search button");
		return false;
	}

}
