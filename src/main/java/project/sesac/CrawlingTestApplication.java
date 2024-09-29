package project.sesac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import project.sesac.domain.Information;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CrawlingTestApplication {

    private static WebDriver driver; //셀리니움 사용을 위한 webDriver 주입

    private static String url = "https://www.bigkinds.or.kr/" ;

    //Chrome Options;
    private static ChromeOptions options;

    //Properties
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "C:\\chromedriver.exe";
    //나중에 경로 수정해야함 (서버에서 할때)


    private static List<Information> infoList = new ArrayList<>();


    public static void main(String[] args) {
        //webdriver 위치에 대한 property 설정 추가
        //(intellij 에서는 파일경로를 resources/static/부터 찾아준다)
        System.setProperty(WEB_DRIVER_ID,WEB_DRIVER_PATH);

        options = new ChromeOptions();
//        options.addArguments("--headless"); // 브라우저를 열지 않고 실행하는 옵션 (필요 시 제거 가능)
//        options.addArguments("--disable-gpu"); // GPU 비활성화 (헤드리스 모드에서 권장)
        options.addArguments("--window-size=1920,1080"); // 브라우저 창 크기 설정
//        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);


        try {
            System.out.println("페이지 접속");
            driver.get(url);
            System.out.println("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)
            driver.findElement(By.xpath("//*[@id=\"popup-dialog-101\"]/div[2]/div/div[1]/label/input")).click();
            driver.findElement(By.xpath("//*[@id=\"total-search-key\"]")).sendKeys("고립은둔청년");
            driver.findElement(By.xpath("//*[@id=\"news-search-form\"]/div/div[1]/div[1]/button")).click();

            // 검색 페이지 진입
            System.out.println("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            Select select = new Select(driver.findElement(By.xpath("//*[@id=\"select2\"]")));
            select.selectByValue("100");

            System.out.println("페이지 접속후 3초 로딩");
            Thread.sleep(3000);//잠시 페이지 로딩할시간이 필요(없으면 데이터 사용시 오류남)

            int count = Integer.parseInt(driver.findElement(By.className("total-news-cnt")).getText());
            if (count > 100){
                count = 100;
            }

            for (int i = 1; i <= count; i++) {
                try {
                    String title = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/a/div/strong/span")).getText();
                    String url = driver.findElement(By.xpath("//*[@id=\"news-results\"]/div[" + i + "]/div/div[2]/div/div/a")).getAttribute("href");

                    Information information = new Information(title, url, 0);
                    infoList.add(information);
                    System.out.println(i + "번 추가 성공");
                }catch (Exception e){
                    System.out.println(i+"번 "+e.getMessage());
                }
            }

            for (int i = 0; i < infoList.size(); i++){
                Information information = infoList.get(i);
                System.out.println("information = " + information);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //driver.close();
        }

    }

}
