package de.cofinpro.reactive;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class Routes {

    private final PostHandler postHandler;

    public Routes(PostHandler postHandler) {
        this.postHandler = postHandler;
    }

    public RouterFunction<ServerResponse> routes() {
        return route(GET("/posts"), this.postHandler::all)
                .andRoute(POST("/posts").and(contentType(APPLICATION_JSON)), this.postHandler::create)
                .andRoute(GET("/posts/{id}"), this.postHandler::get);
    }

}
