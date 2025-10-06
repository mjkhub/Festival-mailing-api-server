package kori.tour.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> spec = Caffeine.newBuilder()
                .initialCapacity(512)
                .maximumSize(1000)                 // 트래픽/DTO 크기에 맞춰 조절
                .expireAfterWrite(Duration.ofMinutes(10))
                .recordStats();

        CaffeineCacheManager manager = new CaffeineCacheManager("tourAll");
        manager.setCaffeine(spec);
        return manager;
    }
}