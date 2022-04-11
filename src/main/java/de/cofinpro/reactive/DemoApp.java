package de.cofinpro.reactive;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class DemoApp {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        PostRepository posts = new PostRepository();
        PostHandler postHandler = new PostHandler(posts);
        Routes routesBean = new Routes(postHandler);

        context.registerBean(PostRepository.class, () -> posts);
        context.registerBean(PostHandler.class, () -> postHandler);
        context.registerBean(Routes.class, () -> routesBean);
        context.registerBean(WebHandler.class, () -> RouterFunctions.toWebHandler(routesBean.routes(), HandlerStrategies.builder().build()));
        context.refresh();

        nettyServer(context).onDispose().block();
    }

    public static DisposableServer nettyServer(ApplicationContext context) {
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer httpServer = HttpServer.create().host("localhost").port(8080);
        return httpServer.handle(adapter).bindNow();
    }

    @Bean
    public RouterFunction<ServerResponse> routes(PostHandler postController) {
        return route(GET("/posts"), postController::all)
                .andRoute(POST("/posts"), postController::create)
                .andRoute(GET("/posts/{id}"), postController::get);
//            .andRoute(PUT("/posts/{id}"), postController::update)
//            .andRoute(DELETE("/posts/{id}"), postController::delete);
    }
}

@Component
class PostHandler {

    private final PostRepository posts;

    public PostHandler(PostRepository posts) {
        this.posts = posts;
    }

    public Mono<ServerResponse> all(ServerRequest req) {
        return ServerResponse.ok().body(this.posts.findAll(), Post.class);
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(Post.class)
                .flatMap(this.posts::save)
                .flatMap(p -> ServerResponse.created(URI.create("/posts/" + p.getId())).build());
    }

    public Mono<ServerResponse> get(ServerRequest req) {
        return this.posts.findById(UUID.fromString(req.pathVariable("id")))
                .flatMap(post -> ServerResponse.ok().body(Mono.just(post), Post.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}

@Component
@Slf4j
class PostRepository {

    private static final List<Post> DATA = new ArrayList<>();

    static {
        DATA.add(Post.builder().id(UUID.randomUUID()).title("post one").content("content of post one").build());
        DATA.add(Post.builder().id(UUID.randomUUID()).title("post two").content("content of post two").build());
    }

    Flux<Post> findAll() {
        return Flux.fromIterable(DATA);
    }

    Mono<Post> findById(UUID id) {
        return findAll().filter(p -> p.getId().equals(id)).last();
    }

    Mono<Post> save(Post post) {
        Post saved = Post.builder().id(UUID.randomUUID()).title(post.getTitle()).content(post.getContent()).build();
        log.debug("saved post: {}", saved);
        DATA.add(saved);
        return Mono.just(saved);
    }
}

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Post {

    private UUID id;
    private String title;
    private String content;
}
