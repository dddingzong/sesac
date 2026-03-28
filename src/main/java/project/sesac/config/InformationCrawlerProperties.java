package project.sesac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sesac.crawler")
public class InformationCrawlerProperties {

    private boolean enabled = true;
    private boolean headless = true;
    private int waitTimeoutSeconds = 15;
    private int maxCareItems = 100;
    private int maxJobItems = 100;
    private String searchKeyword = "고립은둔청년";
    private String bigKindsUrl = "https://www.bigkinds.or.kr/";
    private String saraminUrl = "https://www.saramin.co.kr/zf_user/jobs/public/list";
}
