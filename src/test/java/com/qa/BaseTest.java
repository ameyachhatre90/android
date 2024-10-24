package com.qa;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class serves as the base for all test classes and provides methods for
 * driver management, property and string loading, Appium server setup, and common
 * utility functions like waiting for element visibility, clicking, sending keys, etc.
 */
public class BaseTest {
    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();
    protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
    protected static ThreadLocal<String> platform = new ThreadLocal<String>();
    protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();

    /**
     * Gets the Appium driver instance.
     *
     * @return The Appium driver.
     */
    public AppiumDriver getDriver() {
        return driver.get();
    }

    /**
     * Sets the Appium driver instance.
     */
    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    /**
     * Gets the properties loaded from config.properties.
     *
     * @return The properties.
     */
    public Properties getProps() {
        return props.get();
    }

    /**
     * Sets the properties.
     */
    public void setProps(Properties props2) {
        props.set(props2);
    }

    /**
     * Gets the string resources loaded from strings.xml.
     *
     * @return The HashMap containing string resources.
     */
    public HashMap<String, String> getStrings() {
        return strings.get();
    }

    /**
     * Sets the string resources.
     * <p>
     * The HashMap containing string resources to set.
     */
    public void setStrings(HashMap<String, String> strings2) {
        strings.set(strings2);
    }

    /**
     * Gets the current platform (Android or iOS).
     *
     * @return The platform.
     */
    public String getPlatform() {
        return platform.get();
    }

    /**
     * Sets the platform.
     */
    public void setPlatform(String platform2) {
        platform.set(platform2);
    }

    /**
     * Gets the current date and time.
     *
     * @return The date and time.
     */
    public String getDateTime() {
        return dateTime.get();
    }

    /**
     * Sets the date and time.
     */
    public void setDateTime(String dateTime2) {
        dateTime.set(dateTime2);
    }

    /**
     * Gets the device name.
     *
     * @return The device name.
     */
    public String getDeviceName() {
        return deviceName.get();
    }

    /**
     * Sets the device name.
     */
    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    /**
     * Constructor for the BaseTest class.
     */
    public BaseTest() {
	/*
	In Appium java client versions 9.x.x and later, passing a null driver at the beginning of execution is not
	permitted, unlike in previous versions. To resolve this issue, comment out the line below and move it into
	the constructor of each page object class. This ensures that the driver is initialized before the BaseTest
	constructor is called, preventing it from being null.
	// PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	 */
    }

    /**
     * Runs before each test method.
     * <p>
     * Starts recording the screen.
     */
    @BeforeMethod
    public void beforeMethod() {
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    /**
     * Runs after each test method.
     * <p>
     * Stops recording the screen, saves the video, and logs the video path.
     *
     * @param result The test result.
     * @throws Exception If there is an error stopping the recording or saving the video.
     */
    //stop video capturing and create *.mp4 file
    @AfterMethod
    public synchronized void afterMethod(ITestResult result) throws Exception {
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

        Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
        String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName") + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

        File videoDir = new File(dirPath);

        synchronized (videoDir) {
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
            stream.write(Base64.decodeBase64(media));
            stream.close();
            utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
        } catch (Exception e) {
            utils.log().error("error during video capture" + e.toString());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }


    /**
     * Runs before the entire test suite.
     * <p>
     * Starts the Appium server if it is not already running.
     *
     * @throws Exception If there is an error starting the Appium server.
     */
    @BeforeSuite
    public void beforeSuite() throws Exception, Exception {
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
        server = getAppiumService(); // -> If using Mac, uncomment this statement and comment below statement
//		server = getAppiumServerDefault(); // -> If using Windows, uncomment this statement and comment above statement
        if (!checkIfAppiumServerIsRunnning(4723)) {
            server.start();
            server.clearOutPutStreams(); // -> Comment this if you want to see server logs in the console
            utils.log().info("Appium server started");
        } else {
            utils.log().info("Appium server already running");
        }
    }

    /**
     * Checks if the Appium server is running on the specified port.
     *
     * @param port The port to check.
     * @return True if the Appium server is running, false otherwise.
     * @throws Exception If there is an error checking the port.
     */
    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }


    /**
     * Stops the Appium server after the test suite execution.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (server.isRunning()) {
            server.stop();
            utils.log().info("Appium server stopped");
        }
    }

    /**
     * Builds the default Appium driver local service for Windows.
     *
     * @return AppiumDriverLocalService object
     */
    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    /**
     * Builds the Appium driver local service for Mac.
     * Update the paths as per your Mac setup.
     *
     * @return AppiumDriverLocalService object
     */
    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "enter_your_path_here" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "enter_android_home_path");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingDriverExecutable(new File("/usr/local/bin/node")).withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")).usingPort(4723).withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//				.withArgument(() -> "--allow-insecure","chromedriver_autodownload")
                .withEnvironment(environment).withLogFile(new File("ServerLogs/server.log")));
    }

    /**
     * Initializes the Appium driver before each test.
     *
     * @param emulator             Optional parameter for Android emulator. Set to "true" to use an emulator.
     * @param platformName         The platform name (Android or iOS).
     * @param udid                 The device UDID.
     * @param deviceName           The device name.
     * @param systemPort           Optional parameter for Android system port.
     * @param chromeDriverPort     Optional parameter for Android ChromeDriver port.
     * @param wdaLocalPort         Optional parameter for iOS WDA local port.
     * @param webkitDebugProxyPort Optional parameter for iOS WebKit debug proxy port.
     * @throws Exception If the driver initialization fails.
     */
    @Parameters({"emulator", "platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeTest
    public void beforeTest(@Optional("androidOnly") String emulator, String platformName, String udid, String deviceName, @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort, @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {
        setDateTime(utils.dateTime());
        setPlatform(platformName);
        setDeviceName(deviceName);
        URL url;
        InputStream inputStream = null;
        InputStream stringsis = null;
        Properties props = new Properties();
        AppiumDriver driver;

        String strFile = "logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        //route logs to separate file for each thread
        ThreadContext.put("ROUTINGKEY", strFile);
        utils.log().info("log path: " + strFile);

        try {
            props = new Properties();
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";

            utils.log().info("load " + propFileName);
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);

            utils.log().info("load " + xmlFileName);
            stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setStrings(utils.parseStringXML(stringsis));

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("platformName", platformName);
            desiredCapabilities.setCapability("deviceName", deviceName);
            desiredCapabilities.setCapability("udid", udid);
            url = new URL(props.getProperty("appiumURL"));

            switch (platformName) {
                case "Android":
                    desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
                    desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
                    desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
                    if (emulator.equalsIgnoreCase("true")) {
                        desiredCapabilities.setCapability("avd", deviceName);
                        desiredCapabilities.setCapability("avdLaunchTimeout", 120000);
                    }
                    desiredCapabilities.setCapability("systemPort", systemPort);
                    desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
                    String androidAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    //	String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
                    utils.log().info("appUrl is" + androidAppUrl);
                    desiredCapabilities.setCapability("app", androidAppUrl);

                    driver = new AndroidDriver(url, desiredCapabilities);
                    break;
                case "iOS":
                    desiredCapabilities.setCapability("automationName", props.getProperty("iOSAutomationName"));
                    String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "SwagLabsMobileApp.app";
                    //	String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
                    utils.log().info("appUrl is" + iOSAppUrl);
                    desiredCapabilities.setCapability("bundleId", props.getProperty("iOSBundleId"));
                    desiredCapabilities.setCapability("wdaLocalPort", wdaLocalPort);
                    desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
                    desiredCapabilities.setCapability("app", iOSAppUrl);

                    driver = new IOSDriver(url, desiredCapabilities);
                    break;
                default:
                    throw new Exception("Invalid platform! - " + platformName);
            }
            setDriver(driver);
            utils.log().info("driver initialized: " + driver);
        } catch (Exception e) {
            utils.log().fatal("driver initialization failure. ABORT!!!\n" + e.toString());
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (stringsis != null) {
                stringsis.close();
            }
        }
    }

    /**
     * Waits for the given element to be visible.
     *
     * @param e The WebElement to wait for.
     */
    public void waitForVisibility(WebElement e) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

/*  public void waitForVisibility(WebElement e){
	  Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	  .withTimeout(Duration.ofSeconds(30))
	  .pollingEvery(Duration.ofSeconds(5))
	  .ignoring(NoSuchElementException.class);
	  wait.until(ExpectedConditions.visibilityOf(e));
	  }*/

    /**
     * Clears the text field of the given element.
     *
     * @param e The WebElement to clear.
     */
    public void clear(WebElement e) {
        waitForVisibility(e);
        e.clear();
    }

    /**
     * Clicks the given element.
     *
     * @param e The WebElement to click.
     */
    public void click(WebElement e) {
        waitForVisibility(e);
        e.click();
    }

    /**
     * Clicks the given element and logs a message.
     *
     * @param e   The WebElement to click.
     * @param msg The message to log.
     */
    public void click(WebElement e, String msg) {
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.click();
    }

    /**
     * Sends keys to the given element.
     *
     * @param e   The WebElement to send keys to.
     * @param txt The text to send.
     */
    public void sendKeys(WebElement e, String txt) {
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    /**
     * Sends keys to the given element and logs a message.
     *
     * @param e   The WebElement to send keys to.
     * @param txt The text to send.
     * @param msg The message to log.
     */
    public void sendKeys(WebElement e, String txt, String msg) {
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.sendKeys(txt);
    }

    /**
     * Gets the value of the given attribute of the element.
     *
     * @param e         The WebElement to get the attribute from.
     * @param attribute The name of the attribute.
     * @return The value of the attribute.
     */
    public String getAttribute(WebElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    /**
     * Gets the text of the given element and logs a message.
     *
     * @param e   The WebElement to get the text from.
     * @param msg The message to log.
     * @return The text of the element.
     */
    public String getText(WebElement e, String msg) {
        String txt = null;
        switch (getPlatform()) {
            case "Android":
                txt = getAttribute(e, "text");
                break;
            case "iOS":
                txt = getAttribute(e, "label");
                break;
        }
        utils.log().info(msg + txt);
        ExtentReport.getTest().log(Status.INFO, msg + txt);
        return txt;
    }

    /**
     * Closes the app.
     */
    public void closeApp() {
        switch (getPlatform()) {
            case "Android":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    /**
     * Launches the app.
     */
    public void launchApp() {
        switch (getPlatform()) {
            case "Android":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    /**
     * Scrolls to the element with the given description.
     *
     * @return The WebElement that was scrolled to.
     */
    public WebElement scrollToElement() {
        return getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView(" + "new UiSelector().description(\"test-Price\"));"));
    }

    /**
     * Scrolls to the element with the given name for iOS.
     */
    public void iOSScrollToElement() {
//	  RemoteWebElement element = (RemoteWebElement)getDriver().findElement(By.name("test-ADD TO CART"));
//	  String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
//	  scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
//	  scrollObject.put("toVisible", "sdfnjksdnfkld");
        getDriver().executeScript("mobile:scroll", scrollObject);
    }

    /**
     * Quits the driver after each test.
     */
    @AfterTest(alwaysRun = true)
    public void afterTest() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}
