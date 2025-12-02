package kori.tour.config.cache;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    //L1 Caffeine
    // API DTO 하나당 약 1KB -> 1000개 1MB
    // DB에 존재하는 tour 개수는 약 200개
    // 100, 150으로 설정
    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> spec = Caffeine.newBuilder()
                .initialCapacity(300)
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10))
                .recordStats();

        CaffeineCacheManager manager = new CaffeineCacheManager("tourAll");
        manager.setCaffeine(spec);
        return manager;
    }

    //L2 Redis
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory cf) {
//        var defaultConfig =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(30)) // L2 TTL
//                        .disableCachingNullValues()
//                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return RedisCacheManager.builder(cf)
//                .cacheDefaults(defaultConfig)
//                .transactionAware()
//                .build();
//    }

//    @Bean
//    public CacheManager cacheManager(CaffeineCacheManager l1, RedisCacheManager l2) {
//        return new CacheManager() {
//            private final java.util.Set<String> names = java.util.Set.of("tourAll");
//
//            @Override
//            public Cache getCache(String name) {
//                Cache c1 = l1.getCache(name);
//                Cache c2 = l2.getCache(name);
//                if (c1 == null || c2 == null) return null;
//                return new TwoLevelCache(name, c1, c2);
//            }
//
//            @Override
//            public java.util.Collection<String> getCacheNames() {
//                return names;
//            }
//        };
//    }



}
