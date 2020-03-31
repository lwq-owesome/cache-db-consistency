package cn.jorden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-31 13:28
 */
@Configuration
public class JedisConfig {

  @Bean
  public Jedis jedis() {

    return new Jedis("127.0.0.1",6379);
  }

}
