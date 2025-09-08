package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationUserException;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuario;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuarioResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final AuthenticationGateway authenticationGateway;
    private static final Long ESTADO_SOLICITUD_PENDIENTE  = 1L;

    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        solicitud.setIdEstado(ESTADO_SOLICITUD_PENDIENTE);

        return tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo())))
                .flatMap(tipoPrestamo -> {
                    BigDecimal montoMinimo = new BigDecimal(tipoPrestamo.getMontoMinimo());
                    BigDecimal montoMaximo = new BigDecimal(tipoPrestamo.getMontoMaximo());

                    if (solicitud.getMonto().compareTo(montoMinimo) < 0 || solicitud.getMonto().compareTo(montoMaximo) > 0) {
                        return Mono.error(new MontoFueraDeRangoException(solicitud.getMonto(), montoMinimo, montoMaximo));
                    }

                    // Validar que el usuario existe y es válido
                    return authenticationGateway.validateUser(solicitud.getEmail(), solicitud.getDocumentoIdentidad())
                            .flatMap(isValid -> {
                                if (!isValid) {
                                    return Mono.error(new ValidationUserException(solicitud.getEmail(), solicitud.getDocumentoIdentidad()));
                                }
                                return Mono.just(solicitud);
                            })
                            .flatMap(solicitudRepository::saveSolicitud);
                });
    }

    public Mono<SolicitudPage<SolicitudConUsuarioResponse>> findAllSolicitudes(
            SolicitudPageRequest req) {

        return solicitudRepository.page(req)
                .flatMap(paged -> {
                    // Extraer emails únicos de las solicitudes
                    List<String> emails = paged.getData().stream()
                            .map(SolicitudFieldsPage::email)
                            .filter(email -> email != null && !email.isBlank())
                            .distinct()
                            .toList();


                    // Llamar autenticación
                    return authenticationGateway.getUsersForPage(emails)
                            .collectMap(UsersForPageResponse::email, u -> u)
                            .onErrorReturn(Map.of())
                            .map(usersMap -> {
                                // Enriquecer cada solicitud y convertir a DTO de respuesta
                                List<SolicitudConUsuarioResponse> enrichedItems = paged.getData().stream()
                                        .map(s -> {
                                            var userInfo = usersMap.get(s.email());
                                            SolicitudConUsuario solicitudConUsuario = SolicitudConUsuario.fromSolicitudFieldsAndUser(s, userInfo);
                                            return SolicitudConUsuarioResponse.fromSolicitudConUsuario(solicitudConUsuario);
                                        })
                                        .toList();

                                //  Retornar nueva página con usuarios
                                return new SolicitudPage<>(
                                        enrichedItems,
                                        paged.getTotalRows(),
                                        paged.getPageSize(),
                                        paged.getPageNum(),
                                        paged.getHasNext(),
                                        paged.getSort()
                                );
                            });
                });
    }
}
