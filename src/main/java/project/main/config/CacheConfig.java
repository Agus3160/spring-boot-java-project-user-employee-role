package project.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.cache.ttl:8}")
    private long defaultTTL;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        if(redisConnectionFactory == null) return new NoOpCacheManager();
        try {

            //Se inicializa una variable de configuracion de Redis con la configuracion por default
            RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

            //Se crea un has map para manejar que configuracion sera aplicada para cada key
            HashMap<String, RedisCacheConfiguration> redisCacheConfigurationHashMap = new HashMap<>();

            //Se setean los ttls para los modelos en especifico
            redisCacheConfigurationHashMap.put("role", defaultCacheConfig.entryTtl(Duration.ZERO));
            redisCacheConfigurationHashMap.put("user", defaultCacheConfig.entryTtl(Duration.ofHours(this.defaultTTL)));
            redisCacheConfigurationHashMap.put("empleado", defaultCacheConfig.entryTtl(Duration.ofHours(this.defaultTTL)));

            //Se inicializa redis con las configuraciones
            return RedisCacheManager.builder(redisConnectionFactory).withInitialCacheConfigurations(redisCacheConfigurationHashMap).build();

        } catch (Exception e) {
            // Fallback to a NoOpCacheManager if Redis is not available
            System.out.println("Redis is down, falling back to NoOpCacheManager");
            return new NoOpCacheManager();
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}