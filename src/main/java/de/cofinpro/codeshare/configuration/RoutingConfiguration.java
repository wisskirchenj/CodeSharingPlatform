package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.handler.ApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;


/**
 * Configuration class setting up all routes to the code endpoints
 */
@Configuration
public class RoutingConfiguration {

    /**
     * Bean - method that routes all our (presently 6) served endpoints to their dedicated handler functions
     * - replaces the RequestMapping of the Rest-Controller.
     * @param apiHandler the handler-methods containing class
     * @return the RouterFunction used by SpringMvc 's DispatcherServlet.
     */
    @Bean
    public RouterFunction<ServerResponse> routesToCodeEndpoints(@Autowired ApiHandler apiHandler) {
        return route()
                .add(htmlRoutes(apiHandler))
                .add(apiRoutes(apiHandler))
                .build();
    }

    private RouterFunction<ServerResponse> apiRoutes(ApiHandler apiHandler) {
        return route()
                .GET("/api/code/{id}",
                        request -> request.pathVariable("id").matches("\\d+"),
                        apiHandler::getCodeAsJson)
                .POST("/api/code/new", apiHandler::saveNewCode)
                .GET("/api/code/latest", apiHandler::getLatestCodeAsJson)
                .build();
    }

    private RouterFunction<ServerResponse> htmlRoutes(ApiHandler apiHandler) {
        return route()
                .GET("/code/{id}",
                        request -> request.pathVariable("id").matches("\\d+"),
                        apiHandler::getCodeAsHtml)
                .GET("/code/new", apiHandler::getNewCode)
                .GET("/code/latest", apiHandler::getLatestCodeAsHtml)
                .build();
    }
}
