package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.docs.SolicitudControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SolicitudRouterRest implements SolicitudControllerDocs {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
        return route(POST("/api/v1/solicitudes"), handler::listenSaveSolicitud)
                .andRoute(GET("/api/v1/solicitudes"), handler::listenGetSolicitudes)
                .andRoute(PUT("/api/v1/solicitud"), handler::listenUpdateSolicitud);
    }

}

