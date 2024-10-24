package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * This class contains test methods for Login functionality.
 */
public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;
    TestUtils utils = new TestUtils();

    /**
     * Runs before the class starts.
     * <p>
     * Reads login user data from a JSON file and stores it in a JSONObject.
     *
     * @throws Exception If there is an error reading the JSON file.
     */
    @BeforeClass
    public void beforeClass() throws Exception {
        InputStream datais = null;
        try {
            String dataFileName = "data/loginUsers.json";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (datais != null) {
                datais.close();
            }
        }
		  /*
		  To ensure each test starts with a fresh app state, closeApp() and launchApp() have been moved to the
		  @BeforeMethod section.
		  // closeApp();
		  // launchApp();
		  */
    }

    /**
     * Runs after the class has finished.
     */
    @AfterClass
    public void afterClass() {
    }

    /**
     * Runs before each test method.
     * <p>
     * Closes and relaunches the app to ensure a fresh state for each test,
     * and logs the starting of the test.
     *
     * @param m The Method object representing the current test method.
     */
    @BeforeMethod
    public void beforeMethod(Method m) {
        closeApp();
        launchApp();

        utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        loginPage = new LoginPage();
    }

    /**
     * Runs after each test method.
     */
    @AfterMethod
    public void afterMethod() {
    }

    /**
     * Tests the login functionality with an invalid username.
     * <p>
     * Verifies that the correct error message is displayed when an invalid username is entered.
     */
    @Test
    public void invalidUserName() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginBtn();

        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = getStrings().get("err_invalid_username_or_password");

        Assert.assertEquals(actualErrTxt, expectedErrTxt);
    }

    /**
     * Tests the login functionality with an invalid password.
     * Verifies that the correct error message is displayed when an invalid password is entered.
     */
    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginBtn();

        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = getStrings().get("err_invalid_username_or_password");

        Assert.assertEquals(actualErrTxt, expectedErrTxt);
    }

    /**
     * Tests the login functionality with valid credentials.
     * Verifies that the user is successfully logged in and redirected to the Products Page.
     */
    @Test
    public void successfulLogin() {
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginBtn();

        String actualProductTitle = productsPage.getTitle();
        String expectedProductTitle = getStrings().get("product_title");

        Assert.assertEquals(actualProductTitle, expectedProductTitle);
    }
}
