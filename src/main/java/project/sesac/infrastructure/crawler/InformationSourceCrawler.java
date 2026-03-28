package project.sesac.infrastructure.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public interface InformationSourceCrawler {

    String sourceName();

    List<CollectedInformation> crawl(WebDriver driver, WebDriverWait wait);
}
