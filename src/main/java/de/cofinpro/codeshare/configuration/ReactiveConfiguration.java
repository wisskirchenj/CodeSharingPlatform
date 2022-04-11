package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.controller.SomeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class ReactiveConfiguration {

    @Bean
    public RouterFunction<ServerResponse> getReactiveBean(@Autowired SomeBean bean) {
        return route().GET("/reactive",
                request -> ok().body(Mono.just(bean), SomeBean.class)).build();
    }
}
