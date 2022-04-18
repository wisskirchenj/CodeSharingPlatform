package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.handler.ApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routesToCodeEndpoints(@Autowired ApiHandler apiHandler) {
        return route()
                .GET("/api/code", apiHandler::getCodeAsJson)
                .GET("/code", apiHandler::getCodeAsHtml)
                .GET("/code/new", apiHandler::getNewCode)
                .POST("api/code/new", apiHandler::saveNewCode)
                .build();
    }
}
