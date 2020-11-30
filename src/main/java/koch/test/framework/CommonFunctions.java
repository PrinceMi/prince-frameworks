package koch.test.framework;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctions {

	private WebDriver driver;
	private final int EXPLICIT_WAIT_TIME = 120;
	private final int RETRYCOUNT = 5;

	public CommonFunctions(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * Description: Opens URL Param: String url: The URL that has to be opened
	 */
	public boolean launchEnv(String url) {
		if (url.length() > 0 && driver != null) {
			driver.get(url);
			return true;
		}
		return false;
	}

	/*
	 * Description: User defined function that returns the WebElement value 
	 * Param: String elementLocation: The element location in user defined format that has
	 * to be located
	 */
	private WebElement getWebElement(String elementLocation) {
		try {
			String[] locator = elementLocation.split(":=");
			String locatorType = locator[0];
			String locatorValue = locator[1];
			switch (locatorType.toLowerCase()) {
			case "xpath":
				return driver.findElement(By.xpath(locatorValue));
			case "id":
				return driver.findElement(By.id(locatorValue));
			case "name":
				return driver.findElement(By.name(locatorValue));
			case "classname":
				return driver.findElement(By.className(locatorValue));
			case "tagname":
				return driver.findElement(By.tagName(locatorValue));
			case "linktext":
				return driver.findElement(By.linkText(locatorValue));
			case "css":
				return driver.findElement(By.cssSelector(locatorValue));
			default:
				return null;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Not able to find element: " + elementLocation);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Description: User defined wait function that waits till the WebELement is
	 * visible, wait time is <EXPLICIT_WAIT_TIME> seconds 
	 * Param: String elementLocation: The element location in user defined format that has to be
	 * located
	 */
	public boolean waitTillElementIsDisplayed(String elementLocation) {
		try {
			WebDriverWait objWait = new WebDriverWait(driver, EXPLICIT_WAIT_TIME);
			WebElement objWebElement = getWebElement(elementLocation);
			if (objWebElement != null)
				if (objWait.until(ExpectedConditions.visibilityOf(objWebElement)) != null)
					return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Description: User defined function that clear the text input box and enter
	 * the value in the same 
	 * Param: String elementLocation: The location of the text
	 * input area String value: Value to be entered in the text field
	 */
	public boolean clearAndEnterValueInTextFeild(String elementLocation, String value) {
		try {
			if (waitTillElementIsDisplayed(elementLocation)) {
				WebElement objWebElement = getWebElement(elementLocation);
				if (objWebElement != null) {
					objWebElement.clear();
					objWebElement.sendKeys(value);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Description: User defined function that returns the list of WebElements that
	 * is located by the given locator The function tries to find the WebElement for
	 * <RETRYCOUNT> times 
	 * Param: String elementLocation: The element location in
	 * user defined format that has to be located
	 */
	public List<WebElement> getWebElementsListWithRetry(String elementLocation) {
		try {
			int count = 0;
			while (true) {
				try {
					List<WebElement> webElementList = getWebElements(elementLocation);
					if (webElementList != null)
						if (webElementList.size() > 0) {
							System.out.println("Element Found in " + (count + 1) + " attept(s)");
							return webElementList;
						}
					throw new Exception("Retry count: ");
				} catch (Exception e) {
					++count;
					manualWait(1500);
					if (count == RETRYCOUNT)
						break;
					System.out.println(e.getMessage() + count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Description: User defined function that returns the list of WebElements that
	 * is located by the given locator The function tries to find the WebElement for
	 * <RETRYCOUNT> times 
	 * Param: String elementLocation: The element location in
	 * user defined format that has to be located
	 */
	public List<WebElement> getWebElementsListWithRetryTillContains(String elementLocation, String val) {
		try {
			int count = 0;
			while (true) {
				try {
					List<WebElement> webElementList = getWebElements(elementLocation);
					if (webElementList != null)
						if (webElementList.size() > 0) {
							for (WebElement we : webElementList)
								if (we.getText().contains(val)) {
									System.out.println("Element Found in " + (count + 1) + " attept(s)");
									return webElementList;
								}
						}
					throw new Exception("Retry count: ");
				} catch (Exception e) {
					++count;
					manualWait(1500);
					if (count == RETRYCOUNT)
						break;
					System.out.println(e.getMessage() + count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Description: User defined private function that returns the list of
	 * WebElements that is located by the given locator 
	 * Param: String elementLocation: The element location in user defined format that has to be
	 * located
	 */
	private List<WebElement> getWebElements(String elementLocation) {
		try {
			By byWebElement = getWebElementByObj(elementLocation);
			if (byWebElement != null)
				return driver.findElements(byWebElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Description: User defined function that returns the By Object that locates
	 * the WebElement 
	 * Param: String elementLocation: The element location in user defined format that has to be located
	 */
	private By getWebElementByObj(String elementLocation) {
		try {
			String[] locator = elementLocation.split(":=");
			String locatorType = locator[0];
			String locatorValue = locator[1];
			switch (locatorType.toLowerCase()) {
			case "xpath":
				return By.xpath(locatorValue);
			case "id":
				return By.id(locatorValue);
			case "name":
				return By.name(locatorValue);
			case "classname":
				return By.className(locatorValue);
			case "tagname":
				return By.tagName(locatorValue);
			case "linktext":
				return By.linkText(locatorValue);
			case "css":
				return By.cssSelector(locatorValue);
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Description: User defined function to click on element 
	 * Param: String elementLocation: The element location that has to be clicked
	 */
	public boolean clickOnElement(String elementLocation) {
		try {
			if (waitTillElementIsDisplayed(elementLocation)) {
				WebElement objWebELement = getWebElement(elementLocation);
				if (objWebELement != null) {
					getWebElement(elementLocation).click();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Description: User defined function to click on element 
	 * Param: WebElement elementLocation: The WebElement object that has to be clicked
	 */
	public boolean clickOnElement(WebElement elementLocation) {
		try {
			if (elementLocation != null) {
				elementLocation.click();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public boolean checkDateValidity(String dateVal) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date dateToBeChecked, currentDate;
		try {
			dateToBeChecked = sdf.parse(dateVal);
			currentDate = sdf.parse(sdf.format(new Date()));
			if (dateToBeChecked.after(currentDate) || dateToBeChecked.equals(currentDate)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public boolean checkDateValidity(String depDateVal, String returnDateVal2) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date depDateToBeChecked, returnDateToBeChecked2;
		try {
			depDateToBeChecked = sdf.parse(depDateVal);
			returnDateToBeChecked2 = sdf.parse(returnDateVal2);
			if (returnDateToBeChecked2.after(depDateToBeChecked) || returnDateToBeChecked2.equals(depDateToBeChecked)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public String getCalendarDateValue(String dateVal) {
		return dateVal.split("-")[0];
	}
	
	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public String getCalendarMonthString(String dateVal) {
		return (new DateFormatSymbols().getMonths()[Integer.parseInt(dateVal.split("-")[1])-1]);
	}
	
	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public String getCalendarMonthValue(String dateVal) {
		return dateVal.split("-")[1];
	}
	
	/*
	 * Description: Check if provided date is valid 
	 * Param: String date: Data to be processed
	 */
	public String getCalendarYearValue(String dateVal) {
		return dateVal.split("-")[2];
	}

	
	/*
	 * Description: User defined wait function that waits till the WebELement is
	 * present in DOM, wait time is <EXPLICIT_WAIT_TIME> seconds 
	 * Param: String elementLocation: The element location in user defined format that has to be
	 * located
	 */
	public boolean waitTillElementIsPresent(String elementLocation) {
		try {
			WebDriverWait objWait = new WebDriverWait(driver, EXPLICIT_WAIT_TIME);
			if (getWebElementByObj(elementLocation) != null)
				if (objWait.until(
						ExpectedConditions.presenceOfElementLocated(getWebElementByObj(elementLocation))) != null)
					return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * Description: User defined wait function that waits till the WebELement is
	 * visible, wait time is <EXPLICIT_WAIT_TIME> seconds 
	 * Param: String elementLocation: The element location in user defined format that has to be
	 * located
	 */
	public boolean waitTillElementIsNotDisplayedAnymore(String elementLocation) {
		try {
			WebDriverWait objWait = new WebDriverWait(driver, EXPLICIT_WAIT_TIME);
			WebElement objWebElement = getWebElement(elementLocation);
			if (objWebElement != null) {
				if (objWait.until(ExpectedConditions.invisibilityOf(objWebElement)) == true)
					return true;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Description: User defined function to get inner text of the WebElement 
	 * Param: String elementLocation: The element location from which inner text has to be
	 * fetched
	 */
	public String getInnerTextOfElement(String elementLocation) {
		try {
			if (waitTillElementIsDisplayed(elementLocation)) {
				WebElement objWebElement = getWebElement(elementLocation);
				if (objWebElement != null)
					return objWebElement.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * Description: User defined function to get inner text of the WebElement 
	 * Param: WebElement elementLocation: The WebElement object of location from which
	 * inner text has to be fetched
	 */
	public String getInnerTextOfWebElement(WebElement elementLocation) {
		try {
			if (elementLocation != null) {
				return elementLocation.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * Manual wait function, used in user defined retry functions
	 * Param: long time: Duration for which we want to wait before retry
	 */
	public boolean manualWait(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * Description: User defined function to get the given attribute value of given
	 * WebElement Param: WebElement elementLocation: The WebElement object of
	 * location from which attribute value has to be fetched String attributeName:
	 * Name of the attribute whose value has to be fetched
	 */
	public String getAttributeValueOfWebElement(WebElement elementLocation, String attributeName) {
		try {
			if (elementLocation != null) {
				return elementLocation.getAttribute(attributeName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * Description: User defined function to get the given attribute value of given
	 * WebElement Param: String elementLocation: The web element location from which
	 * attribute value has to be fetched String attributeName: Name of the attribute
	 * whose value has to be fetched
	 */
	public String getAttributeValueOfElement(String elementLocation, String attributeName) {
		try {
			if (waitTillElementIsPresent(elementLocation)) {
				WebElement objWebElement = getWebElement(elementLocation);
				if (objWebElement != null)
					return objWebElement.getAttribute(attributeName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * Function that returns only the numeric value from the give string data Param:
	 * String data: Data string from which only numerics has to be fetched
	 */
	public String getOnlyNumericValue(String data) {
		if (data != null)
			return data.trim().replaceAll("[^\\d.]", "");
		return "";
	}

	/*
	 * Function that switch focus to the give frame id Param: String frameId: Frame
	 * id to which focus has to be switched
	 */
	public boolean switchToFrame(String frameId) {
		try {
			int count = 0;
			while (true) {
				try {
					if (getWebElements("xpath:=//iframe").size() > 0) {
						driver.switchTo().frame(frameId);
						System.out.println("Successful in " + (count + 1) + " attept(s)");
						return true;
					}
					throw new Exception("Retry count: ");
				} catch (Exception e) {
					++count;
					if (!manualWait(2000))
						return false;
					if (count == RETRYCOUNT)
						break;
					System.out.println(e.getMessage() + count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Function that switch focus to the default content in case multiple frames are
	 * present
	 */
	public boolean switchToDefaultContentIfRequired() {
		try {
			int count = 0;
			while (true) {
				try {
					List<WebElement> objWebELement = getWebElements("xpath:=//iframe");
					if (objWebELement != null)
						if (objWebELement.size() > 0) {
							System.out.println("Successful in " + (count + 1) + " attept(s)");
							driver.switchTo().defaultContent();
							return true;
						}
					throw new Exception("Retry count: ");
				} catch (Exception e) {
					++count;
					if (!manualWait(2500))
						return false;
					if (count == RETRYCOUNT)
						break;
					if (e.getMessage() != null)
						System.out.println(e.getMessage() + count);
					else {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Function that switch focus to the default content
	 */
	public boolean switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Function that switch focus to the frame that contains the given web element
	 * Param: String elementLocation: The web element location that identifies the
	 * frame on which we have to focus
	 */
	public boolean switchToFrameWithWebElement(String elementLocation) {
		try {
			int count = 0;
			while (true) {
				try {

					int numberOfFrames = getWebElements("xpath:=//iframe").size();
					if (numberOfFrames > 0) {
						for (int i = 0; i < numberOfFrames; ++i) {
							driver.switchTo().frame(i);
							List<WebElement> objWebElement = getWebElementsListWithRetry(elementLocation);
							if (objWebElement != null)
								if (objWebElement.size() > 0) {
									System.out.println("Successful in " + (count + 1) + " attept(s)");
									return true;
								}
						}
						System.out.println("Not able to locate " + elementLocation + " in any frames");
						return false;
					}
					throw new Exception("Retry count: ");
				} catch (Exception e) {
					driver.switchTo().defaultContent();
					++count;
					if (!manualWait(2000))
						return false;
					if (count == RETRYCOUNT)
						break;
					if (e.getMessage() != null)
						System.out.println(e.getMessage() + count);
					else {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
