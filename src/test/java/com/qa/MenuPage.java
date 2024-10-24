package com.qa;

import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Represents the Menu page in the mobile application.
 */
public class MenuPage extends BaseTest {
    TestUtils utils = new TestUtils();

    /**
     * The settings button element.
     * Located by:
     * - **Android:** XPath "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView"
     * - **iOS:** XPath "//XCUIElementTypeOther[@name=\"test-Menu\"]/XCUIElementTypeOther"
     */
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView\n" +
            "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Menu\"]/XCUIElementTypeOther")
    private WebElement settingsBtn;

    /**
     * Initializes the MenuPage with the Appium driver.
     */
    public MenuPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    /**
     * Presses the Settings button.
     *
     * @return An instance of the SettingsPage.
     */
    public SettingsPage pressSettingsBtn() {
        click(settingsBtn, "press Settings button");
        return new SettingsPage();
    }

}
