package co.com.crediya.pragma.solicitudes.model.notificaicones;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationTest {

    @Test
    void builderAndToBuilder_ShouldCreateAndModifyFields() {
        EmailNotification notification = EmailNotification.builder()
                .requestId(1L)
                .status("APROBADO")
                .emailClient("user@example.com")
                .identityDocument("CC-1")
                .loanAmount(new BigDecimal("2500000"))
                .loanType("Personal")
                .customMessage("Mensaje")
                .build();

        assertEquals(1L, notification.getRequestId());
        assertEquals("APROBADO", notification.getStatus());
        assertEquals("user@example.com", notification.getEmailClient());
        assertEquals("CC-1", notification.getIdentityDocument());
        assertEquals(new BigDecimal("2500000"), notification.getLoanAmount());
        assertEquals("Personal", notification.getLoanType());
        assertEquals("Mensaje", notification.getCustomMessage());

        EmailNotification modified = notification.toBuilder()
                .status("RECHAZADO")
                .build();

        assertEquals("RECHAZADO", modified.getStatus());
        assertEquals(notification.getRequestId(), modified.getRequestId());
    }
}



