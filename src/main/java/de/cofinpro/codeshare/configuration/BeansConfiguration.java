package de.cofinpro.codeshare.configuration;

import de.cofinpro.codeshare.domain.CodeSnippet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    public CodeSnippet getInitialCode() {
        return new CodeSnippet("""
                @Configuration
                public class CodeSharingConfiguration {

                    @Bean
                    public RouterFunction<ServerResponse> getSomeBean(SomeBean bean) {
                        ParameterizedTypeReference<SomeBean> typeRef = new ParameterizedTypeReference<>(){};
                        return route().GET("/demofun", request -> ok().body(bean, typeRef)).build();
                    }
                }""");
    }
}
