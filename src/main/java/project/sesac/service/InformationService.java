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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());




    @Transactional
    @Scheduled(cron = "0 50 23 * * *", zone = "Asia/Seoul") // 매일 11시 50분에 갱신
    public void informationDataCrawling(){

        WebDriver driver; //셀리니움 사용을 위한 webDriver 주입

        //Chrome Options;
        ChromeOptions options;

        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        String WEB_DRIVER_PATH = "C:\\chromedriver.exe";

        //webdriver 위치에 대한 property 설정 추가
        //(intellij 에서는 파일경로를 resources/static/부터 찾아준다)
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        options = new ChromeOptions();
//        options.addArguments("--headless"); // 브라우저를 열지 않고 실행하는 옵션 (필요 시 제거 가능)
//        options.addArguments("--disable-gpu"); // GPU 비활성화 (헤드리스 모드에서 권장)
        options.addArguments("--window-size=1920,1080"); // 브라우저 창 크기 설정
//        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);

        List<Information> infoList = new ArrayList<>();

        try {
            String crawlingUrl = "https://www.bigkinds.or.kr/";
            driver.get(crawlingUrl);
            logger.info("페이지 접속");
            logger.info("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)
            driver.findElement(By.xpath("//*[@id=\"popup-dialog-101\"]/div[2]/div/div[1]/label/input")).click();
            driver.findElement(By.xpath("//*[@id=\"total-search-key\"]")).sendKeys("고립은둔청년");
            driver.findElement(By.xpath("//*[@id=\"news-search-form\"]/div/div[1]/div[1]/button")).click();

            // 검색 페이지 진입
            logger.info("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            Select select = new Select(driver.findElement(By.xpath("//*[@id=\"select2\"]")));
            select.selectByValue("100");

            logger.info("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            int count = Integer.parseInt(driver.findElement(By.className("total-news-cnt")).getText());
            if (count > 100) {
                count = 100;
            }

            for (int i = 1; i <= count; i++) {
                try {
                    String title = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/a/div/strong/span")).getText();
                    String url = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/div/div/a")).getAttribute("href");

                    Information information = new Information(title, url, 0);
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
            logger.info("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            Select selectForJob = new Select(driver.findElement(By.xpath("//*[@id=\"page_count\"]")));
            selectForJob.selectByValue("100");

            logger.info("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

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

                    Information information = new Information("[" + company + "] " + title, url, 1);
                    infoList.add(information);
                    logger.info(i + "번 추가 성공");
                } catch (Exception e){
                    logger.info(i + "번 에러로 인해 추가 안함");
                }
            }

            for(int i=0; i< infoList.size();i++ ) {
                Information information = infoList.get(i);
                informationRepository.save(information);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }
}