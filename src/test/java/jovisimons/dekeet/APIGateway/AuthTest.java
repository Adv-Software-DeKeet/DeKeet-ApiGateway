package jovisimons.dekeet.APIGateway;

import com.google.firebase.auth.FirebaseAuthException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void unauthorized_authorization_is_missing() {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();
        client.get().uri("/api/user").exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void unauthorized_invalid_token() throws FirebaseAuthException {
        String uid = "643f039b4cfb80328538472d";
        String customToken = FirebaseAuth.getInstance().createCustomToken(uid)+"s";

        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();
        client.get().uri("/api/user").header("authorization", "Bearer "+customToken).exchange().expectStatus().isUnauthorized();
    }
}