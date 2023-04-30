package jovisimons.dekeet.APIGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Value("${destinations.authurl}")
    String authUrl;

    @Value("${destinations.userurl}")
    String userUrl;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route(r -> r.path("/")
                        .and().method(HttpMethod.GET)
                        .uri("no://op"))
                .route(r -> r.path("/api/user/**")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(filter))
                        .uri(userUrl))
                .route(r -> r.path("/api/user")
                        .and().method(HttpMethod.POST)
                        .uri(userUrl))
                .route(r -> r.path("/api/user/**")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(filter))
                        .uri(userUrl))
                .route(r -> r.path("/api/user/**")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(filter))
                        .uri(userUrl))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri(authUrl))
                .build();
    }

}