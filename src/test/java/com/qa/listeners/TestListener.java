package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the ITestListener interface to listen for test events and
 * perform actions like logging results, capturing screenshots, and reporting.
 */
public class TestListener implements ITestListener {
	TestUtils utils = new TestUtils();

	/**
	 * Invoked each time a test fails.
	 * <p>
	 * This method logs the exception stack trace, captures a screenshot of the failed test,
	 * and adds the screenshot and failure information to the Extent Report.
	 *
	 * @param result An ITestResult object representing the failed test.
	 */
	public void onTestFailure(ITestResult result) {
		if(result.getThrowable() != null) {
			  StringWriter sw = new StringWriter();
			  PrintWriter pw = new PrintWriter(sw);
			  result.getThrowable().printStackTrace(pw);
			  utils.log().error(sw.toString());
		}

		BaseTest base = new BaseTest();
		File file = base.getDriver().getScreenshotAs(OutputType.FILE);

		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Map <String, String> params = new HashMap<String, String>();
		params = result.getTestContext().getCurrentXmlTest().getAllParameters();

		String imagePath = "Screenshots" + File.separator + params.get("platformName")
		+ "_" + params.get("deviceName") + File.separator + base.getDateTime() + File.separator
		+ result.getTestClass().getRealClass().getSimpleName() + File.separator + result.getName() + ".png";

		String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;

		try {
			FileUtils.copyFile(file, new File(imagePath));
			Reporter.log("This is the sample screenshot");
			Reporter.log("<a href='"+ completeImagePath + "'> <img src='"+ completeImagePath + "' height='400' width='400'/> </a>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExtentReport.getTest().fail("Test Failed",
				MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());
		ExtentReport.getTest().fail("Test Failed",
				MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
		ExtentReport.getTest().fail(result.getThrowable());
	}

	/**
	 * Invoked each time a test starts.
	 * <p>
	 * This method starts a new test in the Extent Report and assigns categories and author information.
	 *
	 * @param result An ITestResult object representing the started test.
	 */
	@Override
	public void onTestStart(ITestResult result) {
		BaseTest base = new BaseTest();
		ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
		.assignCategory(base.getPlatform() + "_" + base.getDeviceName())
		.assignAuthor("achhatre");
	}

	/**
	 * Invoked each time a test succeeds.
	 * <p>
	 * This method logs a success message to the Extent Report.
	 *
	 * @param result An ITestResult object representing the successful test.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReport.getTest().log(Status.PASS, "Test Passed");

	}

	/**
	 * Invoked each time a test is skipped.
	 * <p>
	 * This method logs a skip message to the Extent Report.
	 *
	 * @param result An ITestResult object representing the skipped test.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReport.getTest().log(Status.SKIP, "Test Skipped");

	}

	/**
	 * Invoked each time a method fails but is within the success percentage requested.
	 *
	 * @param result An ITestResult object representing the partially successful test.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	/**
	 * Invoked before running all the test methods belonging to the classes inside the &lt;test&gt; tag
	 * and calling all  
	 their Configuration methods.
	 *
	 * @param context The test context.
	 */
	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	/**
	 * Invoked after all the test methods belonging to the classes inside
	 the &lt;test&gt; tag
	 * have run and all their Configuration methods have been called.
	 * <p>
	 * This  
	 method flushes the Extent Report, ensuring that all results are written to the report file.
	 *
	 * @param context The test context.
	 */
	@Override
	public void onFinish(ITestContext context) {
		ExtentReport.getReporter().flush();
	}

}
