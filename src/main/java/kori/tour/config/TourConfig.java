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

	/**
	 * Creates a RestTemplate bean configured with a pooled Apache HTTP client and custom timeouts for use in tour-related HTTP operations.
	 *
	 * The RestTemplate uses a connection pool with a maximum of 100 total connections and 10 per route, a default keep-alive strategy, and both connection and read timeouts set to 10 seconds. URI encoding is disabled for template handling.
	 *
	 * @return a RestTemplate instance with custom HTTP client and request factory settings
	 */
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
		factory.setReadTimeout(10_000);

		RestTemplate restTemplate = new RestTemplate(factory);
		DefaultUriBuilderFactory d = (DefaultUriBuilderFactory) restTemplate.getUriTemplateHandler();
		d.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

		return restTemplate;
	}

	/**
	 * Creates and configures a ThreadPoolTaskExecutor for managing tour update tasks.
	 *
	 * The executor is set with a core pool size of 15, a maximum pool size of 20, and a queue capacity of 50.
	 * It uses the CallerRunsPolicy for rejected tasks, waits for tasks to complete on shutdown, and assigns the thread name prefix "Tour-Update-thread-".
	 *
	 * @return a configured ThreadPoolTaskExecutor instance for tour update operations
	 */
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
