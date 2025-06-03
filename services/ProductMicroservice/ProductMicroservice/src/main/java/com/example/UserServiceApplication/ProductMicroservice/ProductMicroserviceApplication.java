package com.example.UserServiceApplication.ProductMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
public class ProductMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMicroserviceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplateWithTimeout() {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory=new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setConnectTimeout(5000); //5sec connection timeout
		httpComponentsClientHttpRequestFactory.setReadTimeout(5000); //5 sec read timeout
		return new RestTemplate(httpComponentsClientHttpRequestFactory);
	}
	@Bean(name = "taskExecutor")
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10); // Core threads
		executor.setMaxPoolSize(20); // Maximum threads
		executor.setQueueCapacity(50); // Queue size
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}
}
