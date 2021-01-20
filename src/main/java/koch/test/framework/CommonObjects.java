package koch.test.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CommonObjects {
	
	private String dataPath = System.getProperty("user.dir");
	WebDriver driver;

	
	/*
	 * User defined class that extends the java HashMap where get function is over overridden
	 */
	public static class HashMapNew extends HashMap<String, String> {
		static final long serialVersionUID = 1L;

		@Override
		public String get(Object key) {
			try {
				String value = super.get(key);
				if (value != null)
					return value;
			} catch (Exception e) {
				return "";
			}
			return "";
		}
	}
	
	
	/*
	 * Initialize file path from which data is fetched during execution
	 */
	public CommonObjects(String className) {
		dataPath = dataPath + File.separator + "data" + File.separator + className + ".xlsx";
	}
	
	/*
	 * User defined function that setup and initialize the desired WebDriver object
	 * Param: String driverType: Browser name that has to be initialized
	 */
	public WebDriver getWebDriver(String driverType) {
		WebDriver driver = null;
		if (driverType.contains("CHROME")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("-incognito");
			 WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			this.driver = driver;
			return driver;
		} else if (driverType.contains("FIREFOX")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			this.driver = driver;
			return driver;
		} else {
			System.out.println("Driver type " + driverType + " is invalid");
			return null;
		}
	}
	
	/*
	 * User defined function to read the test data from excel file
	 */
	public List<HashMapNew> readTestDataFromExcel() {
		List<HashMapNew> dataFromExcel = new ArrayList<HashMapNew>();
		File file = new File(dataPath);
		Workbook workbook = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			for (int i = 0; i < rowCount; i = i + 2) {
				HashMapNew dataMap = new HashMapNew();
				int columnCount = sheet.getRow(i).getLastCellNum();
				for (int j = 0; j < columnCount; ++j) {
					dataMap.put(sheet.getRow(i).getCell(j).getStringCellValue(),
							sheet.getRow(i + 1).getCell(j).getStringCellValue());
				}
				dataFromExcel.add(dataMap);
				workbook.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println(dataPath + ": Not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception on " + dataPath);
			e.printStackTrace();
		}
		return dataFromExcel;
	}
	
	/*
	 * User defined function to close all the open browsers by the class driver
	 */
	public void closeAllDrivers() {
		if (driver != null)
			driver.quit();
	}

}
