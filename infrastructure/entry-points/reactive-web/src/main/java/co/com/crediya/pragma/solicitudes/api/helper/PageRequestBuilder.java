package co.com.crediya.pragma.solicitudes.api.helper;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;

@Component
public class PageRequestBuilder {

    public SolicitudPageRequest buildFromQuery(ServerRequest serverRequest) {
        SolicitudPageRequest pageRequest = new SolicitudPageRequest();

        // Paginación
        serverRequest.queryParam("page")
                .map(Integer::parseInt)
                .ifPresent(pageRequest::setPage);

        serverRequest.queryParam("size")
                .map(Integer::parseInt)
                .ifPresent(pageRequest::setSize);

        // Ordenamiento
        serverRequest.queryParam("sort")
                .ifPresent(pageRequest::setSort);

        serverRequest.queryParam("columnSort")
                .ifPresent(pageRequest::setColumnSort);

        // Filtro
        serverRequest.queryParam("query")
                .ifPresent(pageRequest::setQuery);

        // Estado (puede venir como string separado por comas)
        serverRequest.queryParam("status")
                .ifPresent(statusParam -> {
                    if (!statusParam.trim().isEmpty()) {
                        String[] statusArray = statusParam.split(",");
                        pageRequest.setStatus(Arrays.asList(statusArray));
                    }
                });

        return pageRequest;
    }
}
