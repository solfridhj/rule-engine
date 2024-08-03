package no.shj.payment.ruleengine.database.implementation;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Very simple caching which will evict new entries based on time. Based on an in-memory cache, so
 * when the Function is not called and is restarted, it will be empty. Since its in-memory, its also
 * not shared between instances. An option of a shared cache is Azure Cache for Redis. <br>
 * It assumes that rules will not change that often, but if immediate update based on rules updates
 * is needed, other solutions would be needed. Some option could be:
 * <li>Have an Azure function which subscribes to a change feed, triggering upon changes to a rule
 *     config and invalidating the cache for that rule (or all rules).
 */
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager(
      @Value("${rules.infrastructure.cache.evictiontimeinseconds}") Integer evictionTimeInSeconds) {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("rule-config");
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(evictionTimeInSeconds, TimeUnit.SECONDS));
    return cacheManager;
  }
}
