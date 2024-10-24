package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * This class represents the Products Page of the application and provides methods
 * to interact with the elements on the page.
 */
public class ProductsPage extends MenuPage {
    TestUtils utils = new TestUtils();

    //	@AndroidFindBy (xpath = "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.widget.TextView")
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='PRODUCTS']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
    private WebElement productTitleTxt;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
    private WebElement SLBTitle;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
    private WebElement SLBPrice;

    /**
     * Initializes the Page Factory with the Appium driver.
     */
    public ProductsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    /**
     * Retrieves the title of the Products Page.
     *
     * @return The title of the Products Page as a String.
     */
    public String getTitle() {
        String title = getText(productTitleTxt, "product page title is - ");
        return title;
    }

    /**
     * Retrieves the title of the Sauce Labs Backpack.
     *
     * @return The title of the Sauce Labs Backpack as a String.
     */
    public String getSLBTitle() {
        String title = getText(SLBTitle, "title is - ");
        return title;
    }

    /**
     * Retrieves the price of the Sauce Labs Backpack.
     *
     * @return The price of the Sauce Labs Backpack as a String.
     */
    public String getSLBPrice() {
        String price = getText(SLBPrice, "price is - ");
        return price;
    }

    /**
     * Clicks the Sauce Labs Backpack title to navigate to the Product Details Page.
     *
     * @return The ProductDetailsPage instance.
     */
    public ProductDetailsPage pressSLBTitle() {
        click(SLBTitle, "press SLB tile link");
        return new ProductDetailsPage();
    }

}
