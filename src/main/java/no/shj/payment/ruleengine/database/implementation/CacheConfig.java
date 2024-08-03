package no.shj.payment.ruleengine.database.implementation;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Very simple caching which will evict new entries based on time. Based on an in-memory cache, so
 * when the Function is not called and is restarted, it will be empty. <br>
 * It assumes that rules will not change that often, but if immediate update based on rules updates
 * are changes, other solutions would be needed. Some option could be:
 * <li>Have an Azure function which subscribes to a change feed, triggering upon changes to a rule
 *     config and invalidating the cache for that rule (or all rules).
 */
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("rule-config");
    // TODO - make configurable
    cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES));
    return cacheManager;
  }
}
