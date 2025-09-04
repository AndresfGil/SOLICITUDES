package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuario;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final AuthenticationGateway authenticationGateway;
    private static final Long ESTADO_SOLICITUD_PENDIENTE  = 1L;

    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        solicitud.setIdEstado(ESTADO_SOLICITUD_PENDIENTE );

        return tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo())))
                .flatMap(tipoPrestamo -> {
                    BigDecimal montoMinimo = new BigDecimal(tipoPrestamo.getMontoMinimo());
                    BigDecimal montoMaximo = new BigDecimal(tipoPrestamo.getMontoMaximo());

                    if (solicitud.getMonto().compareTo(montoMinimo) < 0 || solicitud.getMonto().compareTo(montoMaximo) > 0) {
                        return Mono.error(new MontoFueraDeRangoException(solicitud.getMonto(), montoMinimo, montoMaximo));
                    }

                    return solicitudRepository.saveSolicitud(solicitud);
                });
    }

    public Mono<SolicitudRepository.PaginatedResult<SolicitudConUsuario>> findAllSolicitudes(
            int page, int size, String sortBy, String sortDirection, String token) {
        return solicitudRepository.findAllSolicitudes(page, size, sortBy, sortDirection)
                .flatMap(paged -> {
                    return paged.content().collectList()
                            .flatMap(solicitudes -> {
                                List<String> emails = solicitudes.stream()
                                        .map(Solicitud::getEmail)
                                        .filter(email -> email != null && !email.isEmpty())
                                        .distinct()
                                        .collect(Collectors.toList());


                                // Pedir batch al MS de usuarios (solo los de la página)
                                return authenticationGateway.getUsersForPageWithToken(emails, token)
                                        .collectMap(user -> user.email(), user -> user)
                                        .onErrorReturn(java.util.Map.of())
                                        .map(usersMap -> {
                                            // combinar solicitudes con información de usuarios
                                            List<SolicitudConUsuario> solicitudesEnriquecidas = solicitudes.stream()
                                                    .map(solicitud -> {
                                                        AuthenticationGateway.UserSolicitudInfo userInfo = 
                                                                usersMap.get(solicitud.getEmail());
                                                        
                                                        // Si la solicitud tiene información enriquecida, extraerla
                                                        String nombreTipoPrestamo = null;
                                                        String estadoSolicitud = null;
                                                        Integer tasaInteres = null;
                                                        
                                                        try {
                                                            if (solicitud.getClass().getSimpleName().equals("SolicitudEnriquecida")) {
                                                                nombreTipoPrestamo = (String) solicitud.getClass()
                                                                    .getMethod("getNombreTipoPrestamo").invoke(solicitud);
                                                                estadoSolicitud = (String) solicitud.getClass()
                                                                    .getMethod("getEstadoSolicitud").invoke(solicitud);
                                                                tasaInteres = (Integer) solicitud.getClass()
                                                                    .getMethod("getTasaInteres").invoke(solicitud);
                                                            }
                                                        } catch (Exception e) {

                                                        }

                                                        return SolicitudConUsuario.fromSolicitudAndUserWithEnrichment(
                                                                solicitud, userInfo, nombreTipoPrestamo, estadoSolicitud, tasaInteres);
                                                    })
                                                    .collect(Collectors.toList());

                                            Flux<SolicitudConUsuario> enrichedContent = Flux.fromIterable(solicitudesEnriquecidas);

                                            return new SolicitudRepository.PaginatedResult<>(
                                                    enrichedContent,
                                                    paged.totalElements(),
                                                    paged.totalPages(),
                                                    paged.currentPage(),
                                                    paged.pageSize(),
                                                    paged.hasNext(),
                                                    paged.hasPrevious()
                                            );
                                        });
                            });
                });
    }



}
