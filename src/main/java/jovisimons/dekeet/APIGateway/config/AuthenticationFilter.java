package jovisimons.dekeet.APIGateway.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Slf4j
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    @Autowired
    private RouterValidator routerValidator;//custom route validator

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                log.info("Authorization is missing");
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }
            final String token = this.getAuthHeader(request);
            try {
                log.info("Decoding token: "+ token);
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                this.populateRequestWithHeaders(exchange, decodedToken);
            }catch (FirebaseAuthException e) {
                log.info("Authorization invalid: "+ e.getMessage());
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            log.info("Auth succeeded");

        }
        return chain.filter(exchange);
    }
    /*PRIVATE*/
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    private String getAuthHeader(ServerHttpRequest request) {
        String Header = request.getHeaders().getOrEmpty("Authorization").get(0);
        String[] HeaderParts = Header.split(" ");
        return HeaderParts[1];
    }
    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, FirebaseToken token) {
        log.info("Populating request headers");

        String role;

        if(Boolean.TRUE.equals(token.getClaims().get("admin")))
            role = "admin";
        else if(Boolean.TRUE.equals(token.getClaims().get("host")))
            role = "host";
        else
            role = "default";

        exchange.getRequest().mutate()
                .header("id", token.getUid())
                .header("role", role)
                .build();
    }
}
