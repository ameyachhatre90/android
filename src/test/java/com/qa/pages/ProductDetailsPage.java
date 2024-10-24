package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * This class represents the Product Details Page of the application and provides
 * methods to interact with the elements on the page.
 */
public class ProductDetailsPage extends MenuPage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]\n" +
            "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
    private WebElement SLBTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]"
            + "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
    private WebElement SLBTxt;

//	@AndroidFindBy (accessibility = "test-Price") private MobileElement SLBPrice;

    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy(id = "test-BACK TO PRODUCTS")
    private WebElement backToProductsBtn;

    @iOSXCUITFindBy(id = "test-ADD TO CART")
    private WebElement addToCartBtn;

    /**
     * Initializes the Page Factory with the Appium driver.
     */
    public ProductDetailsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
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
     * Retrieves the text description of the Sauce Labs Backpack.
     *
     * @return The text description of the Sauce Labs Backpack as a String.
     */
    public String getSLBTxt() {
        String txt = getText(SLBTxt, "txt is - ");
        return txt;
    }

    /*
     * public String getSLBPrice() { String price = getText(SLBPrice);
     * utils.log("price is - " + price); return price; }
     */

    /**
     * Scrolls to the price element and retrieves the price of the Sauce Labs Backpack.
     *
     * @return The price of the Sauce Labs Backpack as a String.
     */
    public String scrollToSLBPriceAndGetSLBPrice() {
        return getText(scrollToElement(), "");
    }

    /**
     * Scrolls the page on iOS devices.
     */
    public void scrollPage() {
        iOSScrollToElement();
    }

    /**
     * Checks if the "Add To Cart" button is displayed.
     *
     * @return True if the button is displayed, false otherwise.
     */
    public Boolean isAddToCartBtnDisplayed() {
        return addToCartBtn.isDisplayed();
    }

    /**
     * Clicks the "Back To Products" button.
     *
     * @return The ProductsPage instance.
     */
    public ProductsPage pressBackToProductsBtn() {
        click(backToProductsBtn, "navigate back to products page");
        return new ProductsPage();
    }

}
