package jovisimons.dekeet.APIGateway;

import com.google.firebase.FirebaseApp;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@TestConfiguration
public class FirebaseConfig{
    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        return  FirebaseApp.initializeApp();
    }

}