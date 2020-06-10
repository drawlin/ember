package me.drawlin.ember;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NameChangeTask {

    private WebDriver webDriver;

    public NameChangeTask(WebDriver webDriver) {
        this.webDriver = webDriver;
        try {
            startTask();
        } catch (Exception e) {
            Logger.log(Level.SEVERE, "Issue when running startTask().");
        }
    }

    public void startTask() throws Exception {
        // Get the Minecraft login page
        webDriver.get("https://my.minecraft.net/en-us/login/");

        // Wait 3 seconds, just to make sure everything is loaded (could use WebDriverWait)
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        // Get the email field and enter in our email
        webDriver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(Main.USERNAME);

        // Get the password field and enter in our password
        webDriver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(Main.PASSWORD);

        // Wait 1 second before entering the credentials
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        // Enter login credentials
        webDriver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(Keys.ENTER);

        // There will be a captcha here so we must wait until user completes captcha
        boolean captchaComplete = false;

        while (!captchaComplete) {
            // Check every 5 seconds if the captcha has been completed by the user.
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            Logger.log(Level.SEVERE, "You must complete the captcha");

            // This will check if the URL has changed to the profile URL, letting us know that the captcha has been completed.
            if (webDriver.getCurrentUrl().toLowerCase().contains("profile")) {
                captchaComplete = true;
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                afterCaptchaComplete();
            }
        }
    }

    public void afterCaptchaComplete() {
        Logger.log(Level.INFO, "Captcha complete recognized, continuing the task.");
        String currentUserName = webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[1]/dl/dd/span")).getText();
        Logger.log(Level.INFO, "User's current name is: " + currentUserName);

        // Get the change name button and click
        webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[1]/dl/dd/button")).click();

        // Get the new username field and enter it
        webDriver.findElement(By.xpath("//*[@id=\"newName\"]")).sendKeys(Main.NEW_USERNAME);

        WebElement errorElement = webDriver.findElement(By.xpath("//*[@id=\"newNameChangeError\"]"));
        if (errorElement != null && errorElement.getText() != null) {
            Logger.log(Level.SEVERE, errorElement.getText());
            // TODO: Refresh and retry after 'x' seconds if it fails
            return;
        }

        // Get the password verification button and enter our password
        webDriver.findElement(By.xpath("//*[@id=\"newNamePassword\"]")).sendKeys(Main.PASSWORD);

        // Get the confirm button and click it
        webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[2]/div/form/button")).click();

    }

}
