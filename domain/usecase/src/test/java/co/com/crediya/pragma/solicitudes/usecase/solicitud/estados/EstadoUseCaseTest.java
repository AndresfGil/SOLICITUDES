package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.aprobacion.SolicitudAprobada;
import co.com.crediya.pragma.solicitudes.model.aprobacion.gateway.SolicitudAprobadaRepository;
import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.estado.EstadoEnum;
import co.com.crediya.pragma.solicitudes.model.estado.gateways.EstadoRepository;
import co.com.crediya.pragma.solicitudes.model.exception.EstadoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.SolicitudNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstado;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.EmailNotificationRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private CambioEstadoRepository cambioEstadoRepository;

    @Mock
    private SolicitudAprobadaRepository solicitudAprobadaRepository;

    @Mock
    private EmailNotificationRepository emailNotificationRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    private EstadoUseCase estadoUseCase;

    @BeforeEach
    void setUp() {
        estadoUseCase = new EstadoUseCase(
                solicitudRepository,
                cambioEstadoRepository,
                solicitudAprobadaRepository,
                emailNotificationRepository,
                tipoPrestamoRepository,
                estadoRepository
        );
    }

    @Test
    void updateEstado_ValidCambioEstado_ReturnsUpdatedSolicitud() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(1L)
                .idEstado(2L)
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        Estado estado = Estado.builder()
                .idEstado(2L)
                .nombreEstado("Aprobado")
                .descripcionEstado("Solicitud aprobada")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(2L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(solicitudAprobadaRepository.saveSolicitudAprobada(any(SolicitudAprobada.class))).thenReturn(Mono.just(new SolicitudAprobada()));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectNext(updatedSolicitud)
                .verifyComplete();
    }

    @Test
    void updateEstado_SolicitudNotFound_ThrowsException() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(999L)
                .idEstado(2L)
                .build();

        when(solicitudRepository.findSolicitudById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectError(SolicitudNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_EstadoNotFound_ThrowsException() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(1L)
                .idEstado(999L)
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectError(EstadoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_EstadoNoAprobado_DoesNotSaveAprobada() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(1L)
                .idEstado(3L)
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        Estado estado = Estado.builder()
                .idEstado(3L)
                .nombreEstado("Rechazado")
                .descripcionEstado("Solicitud rechazada")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(3L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(3L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.findById(3L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectNext(updatedSolicitud)
                .verifyComplete();
    }

    @Test
    void updateEstadoValidacion_ValidEstadoValidacion_ReturnsEstadoValidacion() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .idSolicitud(1L)
                .status("APROBADA")
                .statusCode("200")
                .email("test@example.com")
                .cuotaNueva(new BigDecimal("250000"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .interesMensual(new BigDecimal("0.02"))
                .plazoMeses(24)
                .paymentPlan(List.of())
                .totales(null)
                .decidedAt(LocalDateTime.now())
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(2L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(solicitudAprobadaRepository.saveSolicitudAprobada(any(SolicitudAprobada.class))).thenReturn(Mono.just(new SolicitudAprobada()));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(new TipoPrestamo()));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(new Estado()));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstadoValidacion(estadoValidacion))
                .expectNext(estadoValidacion)
                .verifyComplete();
    }

    @Test
    void updateEstadoValidacion_SolicitudNotFound_ThrowsException() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .idSolicitud(999L)
                .status("APROBADA")
                .statusCode("200")
                .email("test@example.com")
                .build();

        when(solicitudRepository.findSolicitudById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstadoValidacion(estadoValidacion))
                .expectError(SolicitudNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstadoValidacion_EstadoRechazado_DoesNotSaveAprobada() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .idSolicitud(1L)
                .status("RECHAZADA")
                .statusCode("400")
                .email("test@example.com")
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(3L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(new TipoPrestamo()));
        when(estadoRepository.findById(3L)).thenReturn(Mono.just(new Estado()));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstadoValidacion(estadoValidacion))
                .expectNext(estadoValidacion)
                .verifyComplete();
    }

    @Test
    void updateEstado_TipoPrestamoNotFound_ThrowsException() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(1L)
                .idEstado(2L)
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(999L)
                .build();

        Estado estado = Estado.builder()
                .idEstado(2L)
                .nombreEstado("Aprobado")
                .descripcionEstado("Solicitud aprobada")
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(2L)
                .idTipoPrestamo(999L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(solicitudAprobadaRepository.saveSolicitudAprobada(any(SolicitudAprobada.class))).thenReturn(Mono.just(new SolicitudAprobada()));
        when(tipoPrestamoRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_EstadoNotFoundInBuildEmail_ThrowsException() {
        CambioEstado cambioEstado = CambioEstado.builder()
                .idSolicitud(1L)
                .idEstado(2L)
                .build();

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        Estado estado = Estado.builder()
                .idEstado(2L)
                .nombreEstado("Aprobado")
                .descripcionEstado("Solicitud aprobada")
                .build();

        Solicitud updatedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(2L)
                .idTipoPrestamo(1L)
                .build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(updatedSolicitud));
        when(solicitudAprobadaRepository.saveSolicitudAprobada(any(SolicitudAprobada.class))).thenReturn(Mono.just(new SolicitudAprobada()));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(new TipoPrestamo()));
        when(estadoRepository.findById(2L)).thenReturn(Mono.empty());

        StepVerifier.create(estadoUseCase.updateEstado(cambioEstado))
                .expectError(EstadoNotFoundException.class)
                .verify();
    }
}
