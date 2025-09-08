package co.com.crediya.pragma.solicitudes.model.page;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudFieldsPageTest {

    @Test
    void testSolicitudFieldsPageConstructor() {
        SolicitudFieldsPage page = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test@example.com",
                "Personal",
                "Pendiente"
        );

        assertEquals(new BigDecimal("5000000"), page.monto());
        assertEquals(24, page.plazo());
        assertEquals("test@example.com", page.email());
        assertEquals("Personal", page.tipoPrestamo());
        assertEquals("Pendiente", page.estado());
    }

    @Test
    void testSolicitudFieldsPageWithDifferentValues() {
        SolicitudFieldsPage page = new SolicitudFieldsPage(
                new BigDecimal("3000000"),
                12,
                "user@example.com",
                "Hipotecario",
                "Aprobado"
        );

        assertEquals(new BigDecimal("3000000"), page.monto());
        assertEquals(12, page.plazo());
        assertEquals("user@example.com", page.email());
        assertEquals("Hipotecario", page.tipoPrestamo());
        assertEquals("Aprobado", page.estado());
    }

    @Test
    void testSolicitudFieldsPageWithNullValues() {
        SolicitudFieldsPage page = new SolicitudFieldsPage(
                null,
                null,
                null,
                null,
                null
        );

        assertNull(page.monto());
        assertNull(page.plazo());
        assertNull(page.email());
        assertNull(page.tipoPrestamo());
        assertNull(page.estado());
    }

    @Test
    void testSolicitudFieldsPageWithZeroValues() {
        SolicitudFieldsPage page = new SolicitudFieldsPage(
                BigDecimal.ZERO,
                0,
                "",
                "",
                ""
        );

        assertEquals(BigDecimal.ZERO, page.monto());
        assertEquals(0, page.plazo());
        assertEquals("", page.email());
        assertEquals("", page.tipoPrestamo());
        assertEquals("", page.estado());
    }

    @Test
    void testSolicitudFieldsPageWithLargeValues() {
        SolicitudFieldsPage page = new SolicitudFieldsPage(
                new BigDecimal("999999999"),
                120,
                "very.long.email.address@very.long.domain.name.com",
                "Very Long Type of Loan Name",
                "Very Long Status Description"
        );

        assertEquals(new BigDecimal("999999999"), page.monto());
        assertEquals(120, page.plazo());
        assertEquals("very.long.email.address@very.long.domain.name.com", page.email());
        assertEquals("Very Long Type of Loan Name", page.tipoPrestamo());
        assertEquals("Very Long Status Description", page.estado());
    }
}
