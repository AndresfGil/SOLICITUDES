package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.estado.gateways.EstadoRepository;
import co.com.crediya.pragma.solicitudes.model.exception.EstadoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.SolicitudNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstado;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.EmailNotificationRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private CambioEstadoRepository cambioEstadoRepository;

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
                emailNotificationRepository,
                tipoPrestamoRepository,
                estadoRepository
        );
    }

    @Test
    void updateEstado_Success_AprobadoMessageAndReturnUpdatedSolicitud() {
        Long idSolicitud = 10L;
        Long idEstadoNuevo = 2L;

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .idTipoPrestamo(1L)
                .email("cliente@example.com")
                .documentoIdentidad("CC-123")
                .monto(new BigDecimal("5000000"))
                .idEstado(1L)
                .build();

        Solicitud solicitudActualizada = solicitud.toBuilder()
                .idEstado(idEstadoNuevo)
                .build();

        Estado estado = Estado.builder()
                .idEstado(idEstadoNuevo)
                .nombreEstado("APROBADO")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(12)
                .validacionAutomatica(true)
                .build();

        when(solicitudRepository.findSolicitudById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(anyLong())).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(solicitudActualizada));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        CambioEstado cambio = CambioEstado.builder().idSolicitud(idSolicitud).idEstado(idEstadoNuevo).build();

        StepVerifier.create(estadoUseCase.updateEstado(cambio))
                .expectNext(solicitudActualizada)
                .verifyComplete();

        ArgumentCaptor<EmailNotification> captor = ArgumentCaptor.forClass(EmailNotification.class);
        verify(emailNotificationRepository, times(1)).sendNotification(captor.capture());
        EmailNotification sent = captor.getValue();
        assertNotNull(sent);
        assertEquals(idSolicitud, sent.getRequestId());
        assertEquals("APROBADO", sent.getStatus());
        assertEquals("cliente@example.com", sent.getEmailClient());
        assertEquals("Personal", sent.getLoanType());
        assertEquals(new BigDecimal("5000000"), sent.getLoanAmount());
        assertNotNull(sent.getCustomMessage());
    }

    @Test
    void updateEstado_WhenSolicitudNotFound_ThrowsSolicitudNotFoundException() {
        Long idSolicitud = 99L;
        when(solicitudRepository.findSolicitudById(idSolicitud)).thenReturn(Mono.empty());

        CambioEstado cambio = CambioEstado.builder().idSolicitud(idSolicitud).idEstado(1L).build();

        StepVerifier.create(estadoUseCase.updateEstado(cambio))
                .expectError(SolicitudNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_WhenEstadoNotFound_ThrowsEstadoNotFoundException() {
        Long idSolicitud = 10L;
        Long idEstadoNuevo = 888L;

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .idTipoPrestamo(1L)
                .email("cliente@example.com")
                .documentoIdentidad("CC-123")
                .monto(new BigDecimal("5000000"))
                .idEstado(1L)
                .build();

        when(solicitudRepository.findSolicitudById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(idEstadoNuevo)).thenReturn(Mono.empty());

        CambioEstado cambio = CambioEstado.builder().idSolicitud(idSolicitud).idEstado(idEstadoNuevo).build();

        StepVerifier.create(estadoUseCase.updateEstado(cambio))
                .expectError(EstadoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_WhenTipoPrestamoNotFound_ThrowsTipoPrestamoNotFoundException() {
        Long idSolicitud = 10L;
        Long idEstadoNuevo = 2L;

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .idTipoPrestamo(999L)
                .email("cliente@example.com")
                .documentoIdentidad("CC-123")
                .monto(new BigDecimal("5000000"))
                .idEstado(1L)
                .build();

        Solicitud solicitudActualizada = solicitud.toBuilder().idEstado(idEstadoNuevo).build();
        Estado estado = Estado.builder().idEstado(idEstadoNuevo).nombreEstado("PENDIENTE").build();

        when(solicitudRepository.findSolicitudById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(anyLong())).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(solicitudActualizada));
        when(tipoPrestamoRepository.findById(999L)).thenReturn(Mono.empty());

        CambioEstado cambio = CambioEstado.builder().idSolicitud(idSolicitud).idEstado(idEstadoNuevo).build();

        StepVerifier.create(estadoUseCase.updateEstado(cambio))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_BuildsCustomMessage_Rechazado() {
        assertCustomMessageForEstado("RECHAZADO");
    }

    @Test
    void updateEstado_BuildsCustomMessage_Pendiente() {
        assertCustomMessageForEstado("PENDIENTE");
    }

    @Test
    void updateEstado_BuildsCustomMessage_Default() {
        assertCustomMessageForEstado("EN_REVISION");
    }

    private void assertCustomMessageForEstado(String nombreEstado) {
        Long idSolicitud = 20L;
        Long idEstadoNuevo = 3L;

        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .idTipoPrestamo(1L)
                .email("cliente2@example.com")
                .documentoIdentidad("CC-456")
                .monto(new BigDecimal("3000000"))
                .idEstado(1L)
                .build();

        Solicitud solicitudActualizada = solicitud.toBuilder().idEstado(idEstadoNuevo).build();

        Estado estado = Estado.builder().idEstado(idEstadoNuevo).nombreEstado(nombreEstado).build();
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Libre Inversión")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(14)
                .validacionAutomatica(true)
                .build();

        when(solicitudRepository.findSolicitudById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(anyLong())).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenReturn(Mono.just(solicitudActualizada));
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        CambioEstado cambio = CambioEstado.builder().idSolicitud(idSolicitud).idEstado(idEstadoNuevo).build();

        StepVerifier.create(estadoUseCase.updateEstado(cambio))
                .expectNext(solicitudActualizada)
                .verifyComplete();

        ArgumentCaptor<EmailNotification> captor = ArgumentCaptor.forClass(EmailNotification.class);
        verify(emailNotificationRepository, times(1)).sendNotification(captor.capture());
        EmailNotification sent = captor.getValue();
        assertNotNull(sent.getCustomMessage());
    }
}


