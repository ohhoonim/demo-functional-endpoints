package dev.ohhoonim.component.infra;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // 1. Ehcache 캐시 설정 정의
        org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration =
                org.ehcache.config.builders.CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(Object.class, Object.class,
                                org.ehcache.config.builders.ResourcePoolsBuilder.heap(100) // 힙 메모리에 100개까지 보관
                        )
                        .withExpiry(org.ehcache.config.builders.ExpiryPolicyBuilder
                                .timeToLiveExpiration(java.time.Duration.ofMinutes(10))) // TTL 10분
                        .build();

        // 2. JSR-107 javax.cache.CacheManager 프로바이더 조회 및 생성
        javax.cache.spi.CachingProvider cachingProvider = javax.cache.Caching.getCachingProvider();
        javax.cache.CacheManager jCacheManager = cachingProvider.getCacheManager();

        // 3. Ehcache 설정을 JSR-107 매니저에 등록 (사용할 캐시 빈 이름 지정)
        jCacheManager.createCache("myCache", org.ehcache.jsr107.Eh107Configuration
                .fromEhcacheCacheConfiguration(cacheConfiguration));
        jCacheManager.createCache("userCache", org.ehcache.jsr107.Eh107Configuration
                .fromEhcacheCacheConfiguration(cacheConfiguration));

        // 4. Spring의 CacheManager 인터페이스 구현체로 래핑하여 반환
        return new org.springframework.cache.jcache.JCacheCacheManager(jCacheManager);
    }
}
