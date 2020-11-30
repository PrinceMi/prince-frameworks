#Description:
###This is a test assignment project submitted by Suvendu Das.
###This is created using a TestNG framework following a page object model design pattern.

##Project Requirement:
###Java 1.8 Maven 3.5.4
###Chrome Version 86

##Project Details:

#Setup:
###Install Java and Maven. Open terminal at the project root and execute the below command.
#### "mvn clean install -Dbrowser=CHROME"
#### Parameterization is done o achieve execution in different browser

###Project can also be run from any IDE from file testngTestScenario.xml. Update can be made to the following line
<parameter name="browser" value="CHROME"></parameter>
###in the xml file valid values are   value="CHROME" and  value="FIREFOX"


#Execution Data:
###The execution data is fetched from /root/data/<fileName>.xlsx using a Data Provider class. The <filName> should be same as that of test class name that is currently being executed, in our case its "com.ixigo.TestScenario". The data is read in a user defined map in key value pairs. Hence, data 2 rows in data sheet comprises the data for 1 test run.

#Execution Flow:
###As previously mentioned the design pattern is page object model, we have created java classes for each page and member functions of those classes have the validation and operation functions. The common Selenium functions are wrapped with user defined logic to support scalability, increase modular coding and hence reduce maintenance.
###The actual test script is in TestScenario.java that holds the complete execution flow.

#Reporting:
###Report can be found at /root/test-output/extentreport
###For reporting I have used extent report that enables logging info, status and warning in the report file which is accommodated by user defined functions created in file ExtentReportClass.java

#Special-feature:
###As the AUT has some asynchronous calls sometimes even the explicit wait fails, to compensate the same I have created user defined retry mechanism which helps to overcome the drawbacks.