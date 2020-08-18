package scalableweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Simple configuration to load messages.properties file at Runtime.
 */
@Configuration
@PropertySource("classpath:messages.properties")
public class PropertySourceConfig {
}
