package com.transys.core.spring.config.http;

import org.apache.commons.lang3.StringUtils;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;

import org.apache.http.client.config.RequestConfig;

import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.apache.http.message.BasicHeaderElementIterator;

import org.apache.http.protocol.HTTP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.TaskScheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.transys.core.spring.config.http.HttpClientConfigConstants.*;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ApacheHttpClientConfig {
	protected static Logger log = LogManager.getLogger("com.transys.core.spring.config");

	@Bean
	public PoolingHttpClientConnectionManager poolingConnectionManager() {
		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();

		// Set total amount of connections across all HTTP routes
		poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

		// Set maximum amount of connections for each http route in pool
		poolingConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		// Increase the amounts of connections if host is localhost
		HttpHost localhost = new HttpHost("http://localhost", 8080);
		poolingConnectionManager.setMaxPerRoute(new HttpRoute(localhost), MAX_LOCALHOST_CONNECTIONS);

		return poolingConnectionManager;
	}

	@Bean
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return (httpResponse, httpContext) -> {
			HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
			HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

			while (elementIterator.hasNext()) {
				HeaderElement element = elementIterator.nextElement();
				String param = element.getName();
				String value = element.getValue();
				if (StringUtils.isNotEmpty(value) && StringUtils.equalsIgnoreCase("timeout", param)) {
					return Long.parseLong(value) * 1000; // Convert to ms
				}
			}

			return DEFAULT_KEEP_ALIVE_TIME;
		};
	}

	@Bean
	public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
		return new Runnable() {
			@Override
			@Scheduled(fixedDelay = IDLE_CONNECTION_MONITOR_INTERVAL)
			public void run() {
				// Only if connection pool is initialised
				if (pool != null) {
					pool.closeExpiredConnections();
					pool.closeIdleConnections(IDLE_CONNECTION_WAIT_TIME, TimeUnit.MILLISECONDS);

					log.info("Idle connection monitor: Closed expired and idle connections");
				}
			}
		};
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("idleMonitor");
		scheduler.setPoolSize(IDLE_CONNECTION_MONITOR_THREAD_POOL_SIZE);
		return scheduler;
	}

	@Bean
	public CloseableHttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT)
				.setConnectionRequestTimeout(REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

		return HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setConnectionManager(poolingConnectionManager()).setKeepAliveStrategy(connectionKeepAliveStrategy())
				.build();
	}
}
