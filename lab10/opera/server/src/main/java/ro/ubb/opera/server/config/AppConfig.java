package ro.ubb.opera.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.ubb.opera.server.repository.*;

@Configuration
public class AppConfig {
    @Bean
    public ComposerRepository composerRepository() {
        return new ComposerRepository();
    }

    @Bean
    public OperaRepository operaRepository() {
        return new OperaRepository();
    }

    @Bean
    public OrchestraRepository orchestraRepository() {
        return new OrchestraRepository();
    }

    @Bean
    public VenueRepository venueRepository() {
        return new VenueRepository();
    }

    @Bean
    public RehearsalRepository rehearsalRepository() {
        return new RehearsalRepository();
    }
}
