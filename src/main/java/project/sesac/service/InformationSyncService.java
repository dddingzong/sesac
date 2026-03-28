package project.sesac.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.sesac.config.InformationCrawlerProperties;
import project.sesac.domain.Information;
import project.sesac.infrastructure.crawler.CollectedInformation;
import project.sesac.infrastructure.crawler.InformationSourceCrawler;
import project.sesac.infrastructure.crawler.InformationWebDriverFactory;
import project.sesac.repository.InformationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationSyncService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final InformationRepository informationRepository;
    private final InformationWebDriverFactory webDriverFactory;
    private final InformationCrawlerProperties crawlerProperties;
    private final List<InformationSourceCrawler> sourceCrawlers;

    @Transactional
    @Scheduled(cron = "${sesac.crawler.refresh-cron:0 50 23 * * *}", zone = "Asia/Seoul")
    public int refreshInformation() {
        if (!crawlerProperties.isEnabled()) {
            logger.info("정보 크롤링이 비활성화되어 있어 새로고침을 건너뜁니다.");
            return 0;
        }

        List<CollectedInformation> collected = collectFromSources();
        if (collected.isEmpty()) {
            logger.warn("크롤링 결과가 비어 있어 기존 정보를 유지합니다.");
            return 0;
        }

        informationRepository.deleteAll();
        informationRepository.saveAll(collected.stream()
                .map(item -> new Information(item.title(), item.url(), item.informationType()))
                .toList());
        logger.info("정보 {}건을 새로 반영했습니다.", collected.size());
        return collected.size();
    }

    private List<CollectedInformation> collectFromSources() {
        List<CollectedInformation> collected = new ArrayList<>();

        for (InformationSourceCrawler sourceCrawler : sourceCrawlers) {
            WebDriver driver = null;
            try {
                driver = webDriverFactory.createDriver();
                WebDriverWait wait = webDriverFactory.createWait(driver);
                List<CollectedInformation> items = sourceCrawler.crawl(driver, wait);
                logger.info("{} 수집 완료: {}건", sourceCrawler.sourceName(), items.size());
                collected.addAll(items);
            } catch (Exception exception) {
                logger.error("{} 수집 중 오류가 발생했습니다.", sourceCrawler.sourceName(), exception);
            } finally {
                if (driver != null) {
                    driver.quit();
                }
            }
        }

        return collected;
    }
}
