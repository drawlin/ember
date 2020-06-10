package me.drawlin.ember;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NameChangeTask {

    private WebDriver webDriver;

    private String username, password, desiredName;

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public NameChangeTask(String username, String password, String desiredName) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        chromeOptions.addArguments("--start-maximized");

        this.webDriver = new ChromeDriver(chromeOptions);

        this.username = username;
        this.password = password;
        this.desiredName = desiredName;
        try {
            startTask();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(Level.SEVERE, "Issue when running startTask().");
        }
    }

    public void startTask() throws Exception {
        // Get the Minecraft page
        webDriver.get("https://minecraft.net");

        Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(5)));

        // Get the Minecraft login page
        webDriver.navigate().to("https://my.minecraft.net/en-us/login/");

        // Wait 3 seconds, just to make sure everything is loaded (could use WebDriverWait)
        Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(3)));

        // Get the email field and enter in our email
        webDriver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(this.username);

        Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(4)));

        // Get the password field and enter in our password
        webDriver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(this.password);

        // Wait 2 second before entering the credentials
        Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(2)));

        // Enter login credentials
        webDriver.findElement(By.xpath("//*[@id=\"app-login\"]/div[1]/form/button")).click();

        // There will be a captcha here so we must wait until user completes captcha
        boolean captchaComplete = false;

        while (!captchaComplete) {
            // Check every 5 seconds if the captcha has been completed by the user.
            Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(5)));

            Logger.log(Level.SEVERE, "You must complete the captcha");

            // This will check if the URL has changed to the profile URL, letting us know that the captcha has been completed.
            if (webDriver.getCurrentUrl().toLowerCase().contains("profile")) {
                captchaComplete = true;
                Thread.sleep(TimeUnit.SECONDS.toMillis(random.nextInt(3)));
                afterCaptchaComplete();
            }
        }
    }

    public void afterCaptchaComplete() throws Exception {
        Logger.log(Level.INFO, "Captcha complete recognized, continuing the task.");
        String currentUserName = webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[1]/dl/dd/span")).getText();
        Logger.log(Level.INFO, "User's current name is: " + currentUserName);

        // Get the change name button and click
        webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[1]/dl/dd/button")).click();

        Thread.sleep(TimeUnit.SECONDS.toMillis(7));

        // Get the new username field and enter it
        webDriver.findElement(By.xpath("//*[@id=\"newName\"]")).sendKeys(this.desiredName);

        WebElement errorElement = webDriver.findElement(By.xpath("//*[@id=\"newNameChangeError\"]"));
        if (errorElement != null && errorElement.getText() != null) {
            Logger.log(Level.SEVERE, errorElement.getText());
            if (errorElement.getText().contains("available")) {
            // TODO: Refresh and retry after 'x' seconds if it fails
                webDriver.navigate().refresh();
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                afterCaptchaComplete();
                return;
            }
        }

        // Get the password verification button and enter our password
        webDriver.findElement(By.xpath("//*[@id=\"newNamePassword\"]")).sendKeys(this.password);

        // Get the confirm button and click it
        webDriver.findElement(By.xpath("//*[@id=\"app-profile-admin\"]/div/div[2]/div/div[5]/div[2]/div/form/button")).click();

        Logger.log(Level.INFO, "Congratulations! You successfully renamed your account.");
    }

}
