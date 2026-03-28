package project.sesac.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.sesac.domain.Information;
import project.sesac.repository.InformationRepository;
import project.sesac.domain.type.InformationPreference;
import project.sesac.domain.type.InformationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public List<Information> findByPreference(InformationPreference preference){
        if (preference == InformationPreference.ALL) {
            return informationRepository.findAll();
        }
        return informationRepository.findByInformationType(
                preference == InformationPreference.CARE ? InformationType.CARE : InformationType.JOB
        );
    }

    @Transactional
    public List<Information> getCareInfo(){
        return informationRepository.findByInformationType(InformationType.CARE);
    }

    @Transactional
    public List<Information> getJobInfo(){
        return informationRepository.findByInformationType(InformationType.JOB);
    }

    @Transactional
    public List<Information> getSearchInfo(String keyword){
        return informationRepository.getSearchInfo(keyword);
    }

    public List<Information> findAll(){
        return informationRepository.findAll();
    }

    @Transactional
    @Scheduled(cron = "0 50 23 * * *", zone = "Asia/Seoul") // 매일 11시 50분에 갱신
    public int informationDataCrawling(){
        List<Information> infoList = crawlInformations();
        if (infoList.isEmpty()) {
            logger.warn("크롤링 결과가 비어 있어 기존 정보를 유지합니다.");
            return 0;
        }

        informationRepository.deleteAll();
        informationRepository.saveAll(infoList);
        return infoList.size();
    }

    private List<Information> crawlInformations() {
        WebDriver driver = null;
        List<Information> infoList = new ArrayList<>();
        try {
            driver = createWebDriver();
            String crawlingUrl = "https://www.bigkinds.or.kr/";
            driver.get(crawlingUrl);
            logger.info("페이지 접속");
            logger.info("페이지 접속후 10초 로딩");
            Thread.sleep(10000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)
            driver.findElement(By.xpath("//*[@id=\"total-search-key\"]")).sendKeys("고립은둔청년");
            driver.findElement(By.xpath("//*[@id=\"news-search-form\"]/div/div[1]/div[1]/button")).click();

            // 검색 페이지 진입
            logger.info("페이지 접속후 10초 로딩");
            Thread.sleep(10000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            Select select = new Select(driver.findElement(By.xpath("//*[@id=\"select2\"]")));
            select.selectByValue("100");

            logger.info("페이지 접속후 10초 로딩");
            Thread.sleep(10000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            int count = Integer.parseInt(driver.findElement(By.className("total-news-cnt")).getText());
            if (count > 100) {
                count = 100;
            }

            for (int i = 1; i <= count; i++) {
                try {
                    String title = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/a/div/strong/span")).getText();
                    String url = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/div/div/a")).getAttribute("href");

                    Information information = new Information(title, url, InformationType.CARE);
                    infoList.add(information);
                    logger.info(i + "번 추가 성공");
                } catch (Exception e) {
                    logger.info(i + "번 에러로 인해 추가 안함");
                }
            }

            //////////////////////////////////////직업 정보//////////////////////////////////////

            String crawlingUrlForJob = "https://www.saramin.co.kr/zf_user/jobs/public/list";
            driver.get(crawlingUrlForJob);
            logger.info("페이지 접속");
            logger.info("페이지 접속후 10초 로딩");
            Thread.sleep(10000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            Select selectForJob = new Select(driver.findElement(By.xpath("//*[@id=\"page_count\"]")));
            selectForJob.selectByValue("100");

            logger.info("페이지 접속후 10초 로딩");
            Thread.sleep(10000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            int countForJob = 100;

            String company = "default";

            for (int i=1; i<=countForJob; i++) {
                try {
                    try {
                        company = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[4]/div/div[3]/section/div/div[" + i + "]/div[1]/div[1]/a")).getText();
                    } catch (NotFoundException e){
                        company = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[4]/div/div[3]/section/div/div[" + i + "]/div[1]/div[1]/span[1]")).getText();
                    }
                    String title = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[4]/div/div[3]/section/div/div[" + i + "]/div[1]/div[2]/div[1]/a/span")).getText();
                    String url = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[4]/div/div[3]/section/div/div[" + i + "]/div[1]/div[2]/div[1]/a")).getAttribute("href");

                    Information information = new Information("[" + company + "] " + title, url, InformationType.JOB);
                    infoList.add(information);
                    logger.info(i + "번 추가 성공");
                } catch (Exception e){
                    logger.info(i + "번 에러로 인해 추가 안함");
                }
            }

            return infoList;
        } catch (Exception e) {
            logger.error("정보 크롤링 중 오류가 발생했습니다.", e);
            return Collections.emptyList();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private WebDriver createWebDriver() {
        String configuredDriverPath = System.getProperty("webdriver.chrome.driver");
        if (configuredDriverPath == null || configuredDriverPath.isBlank()) {
            configuredDriverPath = System.getenv("CHROME_DRIVER_PATH");
        }
        if (configuredDriverPath != null && !configuredDriverPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", configuredDriverPath);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1440,1600");
        return new ChromeDriver(options);
    }

    @Transactional
    public List<Information> keywordFilter(List<Information> informationList, String keyword) {
        List<Information> realList = new ArrayList<>();

        for (int i=0;i<informationList.size();i++) {
            Information information = informationList.get(i);
            if (information.getTitle().contains(keyword)){
                realList.add(informationList.get(i));
            }
        }
        return realList;
    }

    public void shuffleLogic(List<Information> informationList) {

        ArrayList<Information> careInformationList = new ArrayList<>();
        ArrayList<Information> jobInformationList = new ArrayList<>();

        for(int i=0; i<informationList.size();i++) {
            Information information = informationList.get(i);
            if (information.getInformationType() == InformationType.CARE){ // 복지
                careInformationList.add(information);
            } else if(information.getInformationType() == InformationType.JOB) { // 취업
                jobInformationList.add(information);
            }
        }

        for (int i = informationList.size()-1; i >= 0; i--) {
            informationList.remove(i);
        }

        int smallListSize = 0;
        if (careInformationList.size()==jobInformationList.size()){
            smallListSize = careInformationList.size();
        } else {
            smallListSize = Math.min(jobInformationList.size(), careInformationList.size());
        }

        for (int i=0; i<smallListSize; i++){
            informationList.add(careInformationList.get(i));
            informationList.add(jobInformationList.get(i));
        }

        if (careInformationList.size() > jobInformationList.size()){
            for (int i = smallListSize; i<careInformationList.size();i++){
                informationList.add(careInformationList.get(i));
            }
        } else {
            for (int i = smallListSize; i<jobInformationList.size();i++){
                informationList.add(jobInformationList.get(i));
            }
        }

    }


}
