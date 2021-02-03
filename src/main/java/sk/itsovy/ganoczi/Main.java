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


        String amountErrorMsg = "Amount must be a number between 0 and 1000000 !";
        String interestErrorMsg = "Interest must be a number between 0 and 100 !";
        String agreementErrorMsg = "You must agree to the processing !";

        String red = "rgba(255, 0, 0, 1)";
        String green = "rgba(0, 100, 0, 1)";

        Random rnd = new Random();
        //default state
        //entry(errorMessage, amount, interest, periodRng, periodLbl, taxYes, taxNo, agreement, result);

        // Success case
        amount.sendKeys("1000");
        interest.sendKeys("10");
        for (int i=2; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxYes.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 1259.71 , net profit : 259.71");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Success case
        amount.sendKeys("600000");
        interest.sendKeys("15");
        for (int i=4; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxYes.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 1057405.01 , net profit : 457405.01");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Success case
        amount.sendKeys("35000");
        interest.sendKeys("1");

        periodRng.sendKeys(Keys.RIGHT);

        taxYes.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 35562.24 , net profit : 562.24");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Success case
        amount.sendKeys("1000000");
        interest.sendKeys("99");
        for (int i=3; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxYes.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 10312216.48 , net profit : 9312216.48");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Success case without taxes
        amount.sendKeys("98752");
        interest.sendKeys("7");
        for (int i=2; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxNo.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 120975.44 , net profit : 22223.44");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Success case without taxes
        amount.sendKeys("123456");
        interest.sendKeys("13");
        for (int i=4; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxNo.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertEquals(result.getText(), "Total amount : 227459.69 , net profit : 104003.69");
        Assert.assertEquals(result.getCssValue("color"), green);
        Thread.sleep(2000);
        resetBtn.click();

        // Failure case
        amount.sendKeys("-12000");
        interest.sendKeys("013");
        for (int i=4; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxNo.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        // Failure case
        amount.sendKeys("-12sde");
        interest.sendKeys("-10");
        for (int i=4; i>0;i--){
            periodRng.sendKeys(Keys.RIGHT);
        }
        taxNo.click();
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n"+interestErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();



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
        Thread.sleep(2000);
        resetBtn.click();


        //empty amount
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        //empty interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        //nesúhlasí sa
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), agreementErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        //negative amount
        amount.sendKeys("-" + rnd.nextInt(1000000));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        //negative interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys("-" + rnd.nextInt(100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();

        //interest above hundred
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(900)+100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        Thread.sleep(2000);
        resetBtn.click();


        //tax radio button
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        taxNo.click();
        Assert.assertFalse(taxYes.isSelected());
        Assert.assertTrue(taxNo.isSelected());
        Thread.sleep(2000);
        resetBtn.click();

        //reset changes values to default
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Thread.sleep(2000);
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