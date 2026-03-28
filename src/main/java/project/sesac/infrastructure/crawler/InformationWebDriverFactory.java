package project.sesac.infrastructure.crawler;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import project.sesac.config.InformationCrawlerProperties;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class InformationWebDriverFactory {

    private final InformationCrawlerProperties crawlerProperties;

    public WebDriver createDriver() {
        String configuredDriverPath = System.getProperty("webdriver.chrome.driver");
        if (configuredDriverPath == null || configuredDriverPath.isBlank()) {
            configuredDriverPath = System.getenv("CHROME_DRIVER_PATH");
        }
        if (configuredDriverPath != null && !configuredDriverPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", configuredDriverPath);
        }

        ChromeOptions options = new ChromeOptions();
        if (crawlerProperties.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1440,1600");
        return new ChromeDriver(options);
    }

    public WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(crawlerProperties.getWaitTimeoutSeconds()));
    }
}
