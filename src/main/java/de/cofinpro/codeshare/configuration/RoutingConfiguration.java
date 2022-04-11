package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.handler.ApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routesToGetCode(ApiHandler apiHandler) {
        return route()
                .GET("/api/code", apiHandler::getCodeAsJson)
                .GET("/code", apiHandler::getCodeAsHtml)
                .build();
    }
}
