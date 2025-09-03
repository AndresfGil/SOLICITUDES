package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository  {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);


    Mono<PaginatedResult<Solicitud>> findAllSolicitudes(int page, int size, String sortBy, String sortDirection);
    
    record PaginatedResult<T>(
        Flux<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize,
        boolean hasNext,
        boolean hasPrevious
    ) {}
}
