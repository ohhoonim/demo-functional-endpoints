package dev.ohhoonim.user;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import dev.ohhoonim.component.Response;
import dev.ohhoonim.component.ResponseCode;

/*
 * 이 방식을 채택하려는 이유 - 컨텍스트 스위칭 감소: 특정 기능을 수정할 때 IDE 탭을 여러 개 열 필요 없이 이 파일 하나만 보면 끝난다. - 도메인 중심 응집성:
 * User와 관련된 요청 방식이 한곳에 모여 있어 코드 가시 응집성이 높다. - 패키지 가시성 활용: UserHandler와 UserRouter를 public이 아닌
 * package-private(기본값)으로 두어 외부 노출을 최소화한다.
 */

@Component
class UserHandler {
    public ServerResponse getUser(ServerRequest request) {
        String id = request.pathVariable("id");
        String userInfo = "User ID: " + id;
        return ok().body(userInfo);
    }

    ServerResponse getJsonUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(new User(id, "matthew", 12));
    }

    ServerResponse throwException(ServerRequest request) {
        throw new RuntimeException("error");
    }
}


record User(String id, String name, Integer age) {
}


@Configuration
class UserRouter {

    @Bean
    RouterFunction<ServerResponse> userRoute(UserHandler handler) {
        return route()
                .path("/users",
                        builder -> builder.GET("/{id}", handler::getUser)
                                .GET("/json/{id}", handler::getJsonUser)
                                .GET("/exception/throw", handler::throwException))
                .filter(filterFunction).build();

    }

    HandlerFilterFunction<ServerResponse, ServerResponse> filterFunction = (request, next) -> {
        try {
            ServerResponse response = next.handle(request);
            if (response instanceof EntityResponse<?> entityResponse) {
                Object body = entityResponse.entity();
                if (body instanceof Response || body instanceof Resource) {
                    return response;
                }
                // if (body instanceof String) {
                // // 필요 시 ObjectMapper로 직접 직렬화하거나 그대로 반환
                // }
                return ServerResponse.from(response).contentType(MediaType.APPLICATION_JSON)
                        .body(new Response.Success<>(ResponseCode.SUCCESS, body));
            }
            return response;
        } catch (Exception e) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(new Response.Fail<>(ResponseCode.ERROR, e.getMessage(), null));
        }
    };
}
