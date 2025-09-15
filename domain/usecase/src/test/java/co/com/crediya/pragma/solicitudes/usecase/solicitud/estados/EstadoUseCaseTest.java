package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import co.com.crediya.pragma.solicitudes.model.notificaicones.PaymentPlan;
import co.com.crediya.pragma.solicitudes.model.notificaicones.Totales;

class EstadoUseCaseTest {

    private SolicitudRepository solicitudRepository;
    private CambioEstadoRepository cambioEstadoRepository;
    private EmailNotificationRepository emailNotificationRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private EstadoRepository estadoRepository;
    private EstadoUseCase useCase;

    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        cambioEstadoRepository = mock(CambioEstadoRepository.class);
        emailNotificationRepository = mock(EmailNotificationRepository.class);
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        estadoRepository = mock(EstadoRepository.class);
        useCase = new EstadoUseCase(solicitudRepository, cambioEstadoRepository, emailNotificationRepository, tipoPrestamoRepository, estadoRepository);
    }

    @Test
    void updateEstado_success_sendsEmail() {
        var cambio = CambioEstado.builder().idSolicitud(1L).idEstado(2L).build();
        var solicitud = Solicitud.builder()
                .idSolicitud(1L).idTipoPrestamo(10L).email("e@test.com").documentoIdentidad("123").monto(new BigDecimal("1000")).idEstado(1L)
                .build();
        var estado = Estado.builder().idEstado(2L).nombreEstado("APROBADO").build();
        var tipo = TipoPrestamo.builder().idTipoPrestamo(10L).nombre("Libre Inversión").build();

        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenAnswer(inv -> Mono.just((EmailNotification) inv.getArgument(0)));

        StepVerifier.create(useCase.updateEstado(cambio))
                .assertNext(updated -> assertThat(updated.getIdEstado()).isEqualTo(2L))
                .verifyComplete();
    }

    @Test
    void updateEstado_solicitudNotFound() {
        var cambio = CambioEstado.builder().idSolicitud(9L).idEstado(2L).build();
        when(solicitudRepository.findSolicitudById(9L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateEstado(cambio))
                .expectError(SolicitudNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_estadoNotFound() {
        var cambio = CambioEstado.builder().idSolicitud(1L).idEstado(99L).build();
        var solicitud = Solicitud.builder().idSolicitud(1L).idTipoPrestamo(10L).email("e@test.com").documentoIdentidad("123").monto(new BigDecimal("1000")).idEstado(1L).build();
        when(solicitudRepository.findSolicitudById(1L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateEstado(cambio))
                .expectError(EstadoNotFoundException.class)
                .verify();
    }



    @Test
    void updateEstadoValidacion_solicitudNotFound() {
        var valid = EstadoValidacion.builder().idSolicitud(77L).status("RECHAZADA").build();
        when(solicitudRepository.findSolicitudById(77L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateEstadoValidacion(valid))
                .expectError(SolicitudNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_emailBuilder_tipoPrestamoNotFound() {
        var cambio = CambioEstado.builder().idSolicitud(2L).idEstado(2L).build();
        var solicitud = Solicitud.builder()
                .idSolicitud(2L).idTipoPrestamo(20L).email("e2@test.com").documentoIdentidad("456").monto(new BigDecimal("2000")).idEstado(1L)
                .build();
        var estado = Estado.builder().idEstado(2L).nombreEstado("APROBADO").build();

        when(solicitudRepository.findSolicitudById(2L)).thenReturn(Mono.just(solicitud));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(20L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateEstado(cambio))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstado_emailBuilder_estadoNotFoundAfterUpdate() {
        var cambio = CambioEstado.builder().idSolicitud(3L).idEstado(2L).build();
        var solicitud = Solicitud.builder()
                .idSolicitud(3L).idTipoPrestamo(30L).email("e3@test.com").documentoIdentidad("789").monto(new BigDecimal("3000")).idEstado(1L)
                .build();
        var estado = Estado.builder().idEstado(2L).nombreEstado("APROBADO").build();
        var tipo = TipoPrestamo.builder().idTipoPrestamo(30L).nombre("Educativo").build();

        when(solicitudRepository.findSolicitudById(3L)).thenReturn(Mono.just(solicitud));
        // First call (pre-update check) returns estado, second call (email builder) returns empty
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado), Mono.<Estado>empty());
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(30L)).thenReturn(Mono.just(tipo));

        StepVerifier.create(useCase.updateEstado(cambio))
                .expectError(EstadoNotFoundException.class)
                .verify();
    }

    @Test
    void updateEstadoValidacion_success_aprobada_includesPlanAndTotales() {
        var valid = EstadoValidacion.builder()
                .idSolicitud(10L)
                .status("APROBADO")
                .email("cliente@test.com")
                .cuotaNueva(new BigDecimal("150.50"))
                .capacidadDisponible(new BigDecimal("800.00"))
                .interesMensual(new BigDecimal("0.015"))
                .plazoMeses(24)
                .paymentPlan(buildPaymentPlan(7))
                .totales(Totales.builder().totalIntereses(new BigDecimal("500.00")).totalPagado(new BigDecimal("3650.00")).build())
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(10L).idTipoPrestamo(40L).email("cliente@test.com").documentoIdentidad("CC9988").monto(new BigDecimal("3000"))
                .idEstado(1L)
                .build();
        var tipo = TipoPrestamo.builder().idTipoPrestamo(40L).nombre("Libre Inversión").build();
        var estado = Estado.builder().idEstado(2L).nombreEstado("APROBADA").build();

        when(solicitudRepository.findSolicitudById(10L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(40L)).thenReturn(Mono.just(tipo));
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenAnswer(inv -> Mono.just((EmailNotification) inv.getArgument(0)));

        StepVerifier.create(useCase.updateEstadoValidacion(valid))
                .expectNext(valid)
                .verifyComplete();

        ArgumentCaptor<EmailNotification> captor = ArgumentCaptor.forClass(EmailNotification.class);
        verify(emailNotificationRepository, times(1)).sendNotification(captor.capture());
        String msg = captor.getValue().getCustomMessage();
        assertThat(msg).contains("DETALLES DEL PRÉSTAMO");
        assertThat(msg).contains("PLAN DE PAGOS");
        assertThat(msg).contains("... y 2 cuotas más");
    }

    @Test
    void updateEstadoValidacion_success_rechazada_message() {
        var valid = EstadoValidacion.builder()
                .idSolicitud(11L)
                .status("RECHAZADO")
                .email("cliente2@test.com")
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(11L).idTipoPrestamo(41L).email("cliente2@test.com").documentoIdentidad("CC1122").monto(new BigDecimal("4000"))
                .idEstado(1L)
                .build();
        var tipo = TipoPrestamo.builder().idTipoPrestamo(41L).nombre("Educativo").build();
        var estado = Estado.builder().idEstado(3L).nombreEstado("RECHAZADA").build();

        when(solicitudRepository.findSolicitudById(11L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(41L)).thenReturn(Mono.just(tipo));
        when(estadoRepository.findById(3L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenAnswer(inv -> Mono.just((EmailNotification) inv.getArgument(0)));

        StepVerifier.create(useCase.updateEstadoValidacion(valid))
                .expectNext(valid)
                .verifyComplete();

        ArgumentCaptor<EmailNotification> captor = ArgumentCaptor.forClass(EmailNotification.class);
        verify(emailNotificationRepository, times(1)).sendNotification(captor.capture());
        String msg = captor.getValue().getCustomMessage();
        assertThat(msg).contains("no ha sido aprobada");
    }

    @Test
    void updateEstadoValidacion_default_message_branch() {
        var valid = EstadoValidacion.builder()
                .idSolicitud(12L)
                .status("DESCONOCIDO")
                .email("cliente3@test.com")
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(12L).idTipoPrestamo(42L).email("cliente3@test.com").documentoIdentidad("CC3344").monto(new BigDecimal("5000"))
                .idEstado(1L)
                .build();
        var tipo = TipoPrestamo.builder().idTipoPrestamo(42L).nombre("Vehículo").build();
        var estado = Estado.builder().idEstado(1L).nombreEstado("EN REVISION").build();

        when(solicitudRepository.findSolicitudById(12L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(42L)).thenReturn(Mono.just(tipo));
        when(estadoRepository.findById(1L)).thenReturn(Mono.just(estado));
        when(emailNotificationRepository.sendNotification(any(EmailNotification.class))).thenAnswer(inv -> Mono.just((EmailNotification) inv.getArgument(0)));

        StepVerifier.create(useCase.updateEstadoValidacion(valid))
                .expectNext(valid)
                .verifyComplete();

        ArgumentCaptor<EmailNotification> captor = ArgumentCaptor.forClass(EmailNotification.class);
        verify(emailNotificationRepository, times(1)).sendNotification(captor.capture());
        String msg = captor.getValue().getCustomMessage();
        assertThat(msg).contains("ha cambiado de estado a: EN REVISION");
    }

    @Test
    void updateEstadoValidacion_emailBuilder_tipoPrestamoNotFound() {
        var valid = EstadoValidacion.builder()
                .idSolicitud(13L)
                .status("APROBADO")
                .email("cliente4@test.com")
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(13L).idTipoPrestamo(43L).email("cliente4@test.com").documentoIdentidad("CC5566").monto(new BigDecimal("6000"))
                .idEstado(1L)
                .build();
        var estado = Estado.builder().idEstado(2L).nombreEstado("APROBADA").build();

        when(solicitudRepository.findSolicitudById(13L)).thenReturn(Mono.just(solicitud));
        when(cambioEstadoRepository.updateEstado(any(Solicitud.class))).thenAnswer(inv -> Mono.just((Solicitud) inv.getArgument(0)));
        when(tipoPrestamoRepository.findById(43L)).thenReturn(Mono.empty());
        when(estadoRepository.findById(2L)).thenReturn(Mono.just(estado));

        StepVerifier.create(useCase.updateEstadoValidacion(valid))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    private List<PaymentPlan> buildPaymentPlan(int n) {
        List<PaymentPlan> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(PaymentPlan.builder()
                    .n(i)
                    .cuota(new BigDecimal("100.00").add(new BigDecimal(i)))
                    .interes(new BigDecimal("10.00"))
                    .abonoCapital(new BigDecimal("90.00"))
                    .saldo(new BigDecimal("1000.00").subtract(new BigDecimal(i * 10)))
                    .build());
        }
        return list;
    }

}
