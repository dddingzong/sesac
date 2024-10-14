package project.sesac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);    // 기본 스레드 풀 크기
        executor.setMaxPoolSize(30);     // 최대 스레드 풀 크기
        executor.setQueueCapacity(500);  // 큐 용량
        executor.setThreadNamePrefix("Async-Thread-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}
