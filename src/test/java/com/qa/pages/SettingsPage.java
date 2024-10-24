package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * This class represents the Settings Page of the application and provides methods
 * to interact with the elements on the page.
 */
public class SettingsPage extends BaseTest {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(id = "test-LOGOUT")
    private WebElement logoutBtn;

    /**
     * Initializes the Page Factory with the Appium driver.
     */
    public SettingsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    /**
     * Clicks the Logout button.
     *
     * @return The LoginPage instance.
     */
    public LoginPage pressLogoutBtn() {
        click(logoutBtn, "press Logout button");
        return new LoginPage();
    }

}
