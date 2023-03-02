import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class findBrokenLinks {
    private static WebDriver driver;

    @BeforeMethod
    public static void setUp () {
        System.setProperty("webdriver.chrome.driver", "D:\\Gala\\Browser drivers\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://scand.com/");
        driver.manage().window().maximize();
    }

    @Test
    public static void getLinks() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> listOfLinks = driver.findElements(By.tagName("a"));
        for (int i = 0; i <= listOfLinks.size(); i++) {

            try {
                WebElement element = listOfLinks.get(i);
                String urlLinks = element.getAttribute("href");
                URL url = new URL(urlLinks);
                HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
                httpURLConnect.setConnectTimeout(1000);
                httpURLConnect.connect();
                if (httpURLConnect.getResponseCode() >= 400) {
                    System.out.println("Broken link: " + urlLinks + " - " + httpURLConnect.getResponseMessage());
                    softAssert.assertFalse(httpURLConnect.getResponseCode() >= 400, "A broken link was found");
                }
                else {
                    System.out.println("OK: " + urlLinks + " - " + httpURLConnect.getResponseMessage());

                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        softAssert.assertAll();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}