package com.automation.web.steps;

import static com.automation.report.ExtentReportManager.reportFail;
import static com.automation.report.ExtentReportManager.reportInfo;
import static com.automation.report.ExtentReportManager.reportPass;

import org.apache.log4j.Logger;

import com.automation.engine.WebDriverEngine;
import com.automation.web.pages.HomePage;
import com.automation.web.pages.LoginPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginSteps {

	Logger logger = Logger.getLogger(LoginSteps.class);
	LoginPage loginPage = new LoginPage();
	HomePage homePage = new HomePage();

	@Given("^I am in HRM portal login Page$")
	public void i_am_in_HRM_portal_login_Page() {
		if (loginPage.isUserNameTextBoxDisplayed()) {
			logger.info("--->Orange HRM login page is displayed");
			reportPass("Orange HRM login page is displayed");
		} else {
			logger.info("--->Orange HRM login page is not displayed");
			reportFail("Orange HRM login page is not displayed");
		}
	}

	@Then("^I enter HRM username \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void i_enter_HRM_username_and_password(String userName, String password) {
		loginPage.enterUserName(userName);
		loginPage.enterPasssword(password);
		reportInfo("Login credentias entered");
	}

	@When("^I click on login button$")
	public void i_click_on_login_button() {
		loginPage.clickLoginButton();
		reportInfo("Clicked on login button");
	}

	@Then("^I should be in HRM portal dashboard page$")
	public void i_should_be_in_HRM_portal_dashboard_page() {
		if (homePage.isWelcomeUserLableDisplayed()) {
			logger.info(">>> User successfuly logged into Orange HRM application");
			reportPass("Orange HRM home page is displayed");
		} else {
			logger.info(">>> User unable logged into Orange HRM application");
			reportFail("Orange HRM home page is not displayed");
		}
	}

	@Then("^I should see the title of the page is \"([^\"]*)\"$")
	public void i_should_see_the_title_of_the_page_is(String title) throws Throwable {
		if (WebDriverEngine.driver.getTitle().contains("Sanjay")) {
			reportPass("Portal title is correct");
		} else {
			reportFail("Portal title is not correct");
		}
	}

}
