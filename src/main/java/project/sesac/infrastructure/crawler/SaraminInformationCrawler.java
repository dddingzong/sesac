package project.sesac.infrastructure.crawler;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.sesac.config.InformationCrawlerProperties;
import project.sesac.domain.type.InformationType;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class SaraminInformationCrawler implements InformationSourceCrawler {

    private static final By JOB_PAGE_COUNT = By.id("page_count");
    private static final By JOB_LIST = By.xpath("/html/body/div[3]/div[1]/div/div[4]/div/div[3]/section/div/div");

    private final InformationCrawlerProperties crawlerProperties;

    @Override
    public String sourceName() {
        return "SARAMIN";
    }

    @Override
    public List<CollectedInformation> crawl(WebDriver driver, WebDriverWait wait) {
        driver.get(crawlerProperties.getSaraminUrl());

        WebElement pageCount = wait.until(ExpectedConditions.presenceOfElementLocated(JOB_PAGE_COUNT));
        selectIfPresent(pageCount, String.valueOf(crawlerProperties.getMaxJobItems()));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(JOB_LIST));

        List<WebElement> cards = driver.findElements(JOB_LIST);
        return parseCards(cards, crawlerProperties.getMaxJobItems());
    }

    private List<CollectedInformation> parseCards(List<WebElement> cards, int maxItems) {
        List<CollectedInformation> results = new ArrayList<>();
        int limit = Math.min(cards.size(), maxItems);

        for (int i = 0; i < limit; i++) {
            WebElement card = cards.get(i);
            String company = firstNonBlank(
                    safeText(card, By.xpath(".//div[1]/div[1]/a")),
                    safeText(card, By.xpath(".//div[1]/div[1]/span[1]"))
            );
            String title = safeText(card, By.xpath(".//div[1]/div[2]/div[1]/a/span"));
            String url = safeAttribute(card, By.xpath(".//div[1]/div[2]/div[1]/a"), "href");

            if (isBlank(company) || isBlank(title) || isBlank(url)) {
                continue;
            }

            results.add(new CollectedInformation("[" + company + "] " + title, url, InformationType.JOB));
        }

        return results;
    }

    private void selectIfPresent(WebElement element, String value) {
        Select select = new Select(element);
        boolean present = select.getOptions().stream()
                .anyMatch(option -> value.equals(option.getAttribute("value")));
        if (present) {
            select.selectByValue(value);
        }
    }

    private String safeText(WebElement root, By locator) {
        List<WebElement> elements = root.findElements(locator);
        if (elements.isEmpty()) {
            return null;
        }
        return normalize(elements.get(0).getText());
    }

    private String safeAttribute(WebElement root, By locator, String attribute) {
        List<WebElement> elements = root.findElements(locator);
        if (elements.isEmpty()) {
            return null;
        }
        return normalize(elements.get(0).getAttribute(attribute));
    }

    private String firstNonBlank(String first, String second) {
        return isBlank(first) ? second : first;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
