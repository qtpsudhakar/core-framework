package com.automation.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.engine.WebDriverEngine;

public class LoginPage extends WebDriverEngine {

	public LoginPage() {
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "txtUsername")
	@CacheLookup
	WebElement txtUserName;

	@FindBy(id = "txtPassword")
	@CacheLookup
	WebElement txtPassword;

	@FindBy(xpath = "//input[@id='btnLogin'] | //input[@id='btnLogin1']")
	@CacheLookup
	WebElement btnLogin;

	public boolean isUserNameTextBoxDisplayed() {
		return txtUserName.isDisplayed();
	}

	public boolean isPasswordTextBoxDisplayed() {
		return txtPassword.isDisplayed();
	}

	public void enterUserName(String userName) {
		txtUserName.sendKeys(userName);
	}

	public void enterPasssword(String password) {
		txtPassword.sendKeys(password);
	}

	public void clickLoginButton() {
		btnLogin.click();
	}

}
