package jovisimons.dekeet.APIGateway.config;

import com.google.firebase.FirebaseApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FirebaseConfig{
    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        return  FirebaseApp.initializeApp();
    }

}
