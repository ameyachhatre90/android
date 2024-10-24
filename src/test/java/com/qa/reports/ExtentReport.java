package com.qa.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the Extent Report instance and provides methods for creating
 * and retrieving test reports.
 */
public class ExtentReport {
    static ExtentReports extent;
    final static String filePath = "Extent.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap();

    /**
     * Retrieves the Extent Reports instance.
     * <p>
     * If the Extent Reports instance is null, it creates a new instance with an
     * ExtentSparkReporter, configures the report with title, name, and theme, and
     * attaches the reporter to the Extent Reports instance.
     *
     * @return The Extent Reports instance.
     */
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            ExtentSparkReporter html = new ExtentSparkReporter("Extent.html");
            html.config().setDocumentTitle("Appium Framework");
            html.config().setReportName("MyApp");
            html.config().setTheme(Theme.DARK);
            extent = new ExtentReports();
            extent.attachReporter(html);
        }

        return extent;
    }

    /**
     * Retrieves the Extent Test instance associated with the current thread.
     *
     * @return The Extent Test instance.
     */
    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    /**
     * Starts a new test in the Extent Report.
     * Creates a new Extent Test instance with the given test name and description,
     * adds it to the extentTestMap with the current thread ID as the key, and returns
     * the test instance.
     *
     * @param testName The name of the test.
     * @param desc     The description of the test.
     * @return The Extent Test instance.
     */
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = getReporter().createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
}
