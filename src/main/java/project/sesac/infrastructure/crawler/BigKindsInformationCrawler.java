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
@Order(1)
@RequiredArgsConstructor
public class BigKindsInformationCrawler implements InformationSourceCrawler {

    private final InformationCrawlerProperties crawlerProperties;

    @Override
    public String sourceName() {
        return "BIGKINDS";
    }

    @Override
    public List<CollectedInformation> crawl(WebDriver driver, WebDriverWait wait) {
        driver.get(crawlerProperties.getBigKindsUrl());

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("total-search-key")));
        searchInput.clear();
        searchInput.sendKeys(crawlerProperties.getSearchKeyword());
        driver.findElement(By.cssSelector("#news-search-form button")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("news-results")));
        WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("select2")));
        selectIfPresent(selectElement, String.valueOf(crawlerProperties.getMaxCareItems()));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#news-results > div")));

        List<WebElement> cards = driver.findElements(By.cssSelector("#news-results > div"));
        return parseCards(cards, crawlerProperties.getMaxCareItems());
    }

    private List<CollectedInformation> parseCards(List<WebElement> cards, int maxItems) {
        List<CollectedInformation> results = new ArrayList<>();
        int limit = Math.min(cards.size(), maxItems);

        for (int i = 0; i < limit; i++) {
            WebElement card = cards.get(i);
            String title = safeText(card, By.xpath(".//strong/span"));
            String url = safeAttribute(card, By.xpath(".//div/div/a"), "href");
            if (isBlank(title) || isBlank(url)) {
                continue;
            }
            results.add(new CollectedInformation(title, url, InformationType.CARE));
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
