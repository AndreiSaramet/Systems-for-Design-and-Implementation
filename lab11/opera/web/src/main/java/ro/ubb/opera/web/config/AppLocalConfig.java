package ro.ubb.opera.web.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ro.ubb.opera.core.config.JPAConfig;

@Configuration
@ComponentScan({"ro.ubb.opera.core"})
@Import({JPAConfig.class})
@PropertySources({@PropertySource(value = "classpath:local/db.properties")})
public class AppLocalConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
