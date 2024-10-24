package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * This class represents the Login Page of the application and provides methods to interact
 * with the elements on the page.
 */
public class LoginPage extends BaseTest {
    TestUtils utils = new TestUtils();
    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "test-Username")
    private WebElement usernameTxtFld;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(id = "test-Password")
    private WebElement passwordTxtFld;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(id = "test-LOGIN")
    private WebElement loginBtn;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
    private WebElement errTxt;

    /**
     * Initializes the Page Factory with the Appium driver.
     */
    public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    /**
     * Enters the username in the username text field.
     *
     * @param username The username to be entered.
     * @return The LoginPage instance.
     */
    public LoginPage enterUserName(String username) {
        clear(usernameTxtFld);
        sendKeys(usernameTxtFld, username, "login with " + username);
        return this;
    }

    /**
     * Enters the password in the password text field.
     *
     * @param password The password to be entered.
     * @return The LoginPage instance.
     */
    public LoginPage enterPassword(String password) {
        clear(passwordTxtFld);
        sendKeys(passwordTxtFld, password, "password is " + password);
        return this;
    }

    /**
     * Clicks the login button.
     *
     * @return The ProductsPage instance.
     */
    public ProductsPage pressLoginBtn() {
        click(loginBtn, "press login button");
        return new ProductsPage();
    }

    /**
     * Performs a login action by entering the username, password, and clicking the login button.
     *
     * @param username The username to be entered.
     * @param password The password to be entered.
     * @return The ProductsPage instance.
     */
    public ProductsPage login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        return pressLoginBtn();
    }

    /**
     * Retrieves the error text displayed on the page.
     *
     * @return The error text as a String.
     */
    public String getErrTxt() {
        String err = getText(errTxt, "error text is - ");
        return err;
    }

}
