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

    @Test
    void testEmailNotificationNoArgsConstructor() {
        EmailNotification notification = new EmailNotification();
        
        assertNull(notification.getRequestId());
        assertNull(notification.getStatus());
        assertNull(notification.getEmailClient());
        assertNull(notification.getIdentityDocument());
        assertNull(notification.getLoanAmount());
        assertNull(notification.getLoanType());
        assertNull(notification.getCustomMessage());
    }

    @Test
    void testEmailNotificationAllArgsConstructor() {
        EmailNotification notification = new EmailNotification(
                2L,
                "PENDIENTE",
                "test@example.com",
                "CC-12345678",
                new BigDecimal("5000000"),
                "Hipotecario",
                "Mensaje personalizado"
        );

        assertEquals(2L, notification.getRequestId());
        assertEquals("PENDIENTE", notification.getStatus());
        assertEquals("test@example.com", notification.getEmailClient());
        assertEquals("CC-12345678", notification.getIdentityDocument());
        assertEquals(new BigDecimal("5000000"), notification.getLoanAmount());
        assertEquals("Hipotecario", notification.getLoanType());
        assertEquals("Mensaje personalizado", notification.getCustomMessage());
    }

    @Test
    void testEmailNotificationSettersAndGetters() {
        EmailNotification notification = new EmailNotification();
        
        notification.setRequestId(3L);
        notification.setStatus("RECHAZADO");
        notification.setEmailClient("newuser@example.com");
        notification.setIdentityDocument("TI-87654321");
        notification.setLoanAmount(new BigDecimal("3000000"));
        notification.setLoanType("Vehicular");
        notification.setCustomMessage("Nuevo mensaje");

        assertEquals(3L, notification.getRequestId());
        assertEquals("RECHAZADO", notification.getStatus());
        assertEquals("newuser@example.com", notification.getEmailClient());
        assertEquals("TI-87654321", notification.getIdentityDocument());
        assertEquals(new BigDecimal("3000000"), notification.getLoanAmount());
        assertEquals("Vehicular", notification.getLoanType());
        assertEquals("Nuevo mensaje", notification.getCustomMessage());
    }
}



