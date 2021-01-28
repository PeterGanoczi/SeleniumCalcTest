package sk.itsovy.ganoczi;


import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


public class Main {


    @Test
    public static void main(String[] args) throws Exception {

        String url = "http://itsovy.sk/testing";

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ledgo\\Desktop\\Sovy\\testing\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get(url);

        WebElement errorMessage = driver.findElement(By.id("error"));
        WebElement amount = driver.findElement(By.id("amount"));
        WebElement interest = driver.findElement(By.id("interest"));
        WebElement periodRng = driver.findElement(By.id("period"));
        WebElement periodLbl = driver.findElement(By.id("lblPeriod"));
        WebElement taxYes = driver.findElement(By.cssSelector("input[value=\"y\"]"));
        WebElement taxNo = driver.findElement(By.cssSelector("input[value=\"n\"]"));
        WebElement agreement = driver.findElement(By.id("confirm"));
        WebElement resetBtn = driver.findElement(By.id("btnreset"));
        WebElement calculateBtn = driver.findElement(By.id("btnsubmit"));
        WebElement result = driver.findElement(By.id("result"));

        Random rnd = new Random();
        String amountErrorMsg = "Amount must be a number between 0 and 1000000 !";
        String interestErrorMsg = "Interest must be a number between 0 and 100 !";
        String agreementErrorMsg = "You must agree to the processing !";
        String periodString;
        String red = "rgba(255, 0, 0, 1)";
        String darkGreen = "rgba(0, 100, 0, 1)";

        //default state
        entry(errorMessage, amount, interest, periodRng, periodLbl, taxYes, taxNo, agreement, result);

        //calculation with right result
        int tax = 1;
        for (int i = 10; i > 0; i--) {
            double amountDbl = rnd.nextInt(1000000), interestDbl = rnd.nextInt(100), t = 0, expectedAmount = amountDbl, expectedProfit = 0;
            int periodInt = rnd.nextInt(4)+1;
            amount.sendKeys(String.valueOf(amountDbl));
            interest.sendKeys(String.valueOf(interestDbl));
            for (int x = 1; x < periodInt; x++) periodRng.sendKeys(Keys.RIGHT);
            if (tax == 1) {
                taxYes.click();
                tax = 0;
                t = 0.2;
            } else {
                taxNo.click();
                tax = 1;
                t = 0;
            }
            agreement.click();
            calculateBtn.click();
            for (int x = 0; x < periodInt; x++) expectedAmount *= (1 + (1 - t) * interestDbl / 100);
            expectedProfit = expectedAmount - amountDbl;
            BigDecimal roundA = new BigDecimal(expectedAmount);
            BigDecimal roundP = new BigDecimal(expectedProfit);
            roundA = roundA.setScale(2, RoundingMode.HALF_UP);
            roundP = roundP.setScale(2, RoundingMode.HALF_UP);
            Assert.assertTrue(result.isDisplayed());
            Assert.assertEquals(result.getCssValue("color"), darkGreen);
            resetBtn.click();
        }

        //empty form
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg + "\n" + agreementErrorMsg);

        //amount and interest empty
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg);
        resetBtn.click();


        //empty amount
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        resetBtn.click();

        //empty interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();

        //nesúhlasí sa
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), agreementErrorMsg);
        resetBtn.click();

        //negative amount
        amount.sendKeys("-" + rnd.nextInt(1000000));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        resetBtn.click();

        //negative interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys("-" + rnd.nextInt(100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();

        //interest above hundred
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(900)+100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();


        //tax radio button
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        taxNo.click();
        Assert.assertFalse(taxYes.isSelected());
        Assert.assertTrue(taxNo.isSelected());
        resetBtn.click();

        //reset changes values to default
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        resetBtn.click();
        entry(errorMessage, amount, interest, periodRng, periodLbl, taxYes, taxNo, agreement, result);


        System.out.println("Test were successful !");

        driver.quit();
    }

    private static void entry(WebElement errorMessage, WebElement amount, WebElement interest, WebElement periodRng, WebElement periodLbl, WebElement taxYes, WebElement taxNo, WebElement agreement, WebElement result) {
        String periodString;
        Assert.assertFalse(errorMessage.isDisplayed());
        Assert.assertEquals(amount.getText(), "");
        Assert.assertEquals(interest.getText(), "");
        Assert.assertEquals(periodRng.getAttribute("value"), "1");
        periodString = periodLbl.getText();
        Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), "1");
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        Assert.assertFalse(agreement.isSelected());
        Assert.assertFalse(result.isDisplayed());
    }
}