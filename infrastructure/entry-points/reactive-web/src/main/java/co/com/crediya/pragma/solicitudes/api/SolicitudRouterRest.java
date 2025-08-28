package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.docs.SolicitudControllerDocs;
import co.com.crediya.pragma.solicitudes.api.exception.GlobalExceptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SolicitudRouterRest implements SolicitudControllerDocs {
    
    @Bean
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/solicitudes"), handler::listenSaveSolicitud)
                .andRoute(GET("/api/v1/solicitudes"), handler::listenGetAllSolicitudes)
                .andRoute(GET("/api/v1/solicitudes/{id}"), handler::listenGetSolicitudById)
                .filter(filter);
    }

    @Override
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
        return route(POST("/api/v1/solicitudes"), handler::listenSaveSolicitud);
    }
}

