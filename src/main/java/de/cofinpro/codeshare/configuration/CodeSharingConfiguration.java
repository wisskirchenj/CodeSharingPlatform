package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.controller.SomeBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;


@Configuration
public class CodeSharingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> getSomeBean(SomeBean bean) {
        ParameterizedTypeReference<SomeBean> typeRef = new ParameterizedTypeReference<>(){};
        return route().GET("/demofun", request -> ok().body(bean, typeRef)).build();
    }
}
