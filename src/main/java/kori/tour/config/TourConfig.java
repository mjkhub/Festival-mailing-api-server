package kori.tour.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class TourConfig {

	@Bean
	public RestTemplate restTemplateForTourClient() {

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(100);
		connectionManager.setDefaultMaxPerRoute(10);

		CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
			.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(10_000);
//		factory.setReadTimeout(10_000);

		RestTemplate restTemplate = new RestTemplate(factory);
		DefaultUriBuilderFactory d = (DefaultUriBuilderFactory) restTemplate.getUriTemplateHandler();
		d.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

		return restTemplate;
	}

	@Bean
	public ThreadPoolTaskExecutor tourUpdaterThreadTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(15);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setQueueCapacity(50);
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setThreadNamePrefix("Tour-Update-thread-");
		taskExecutor.initialize();
		return taskExecutor;
	}

}
