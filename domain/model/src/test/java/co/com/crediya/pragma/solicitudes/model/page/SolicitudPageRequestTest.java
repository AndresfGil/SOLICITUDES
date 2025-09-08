package co.com.crediya.pragma.solicitudes.model.page;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudPageRequestTest {

    @Test
    void testSolicitudPageRequestDefaultValues() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        
        assertEquals(0, request.getPage());
        assertEquals(50, request.getSize());
        assertEquals("ASC", request.getSort());
        assertEquals("", request.getColumnSort());
        assertEquals("%", request.getQuery());
        assertNull(request.getStatus());
    }

    @Test
    void testSolicitudPageRequestSettersAndGetters() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        
        request.setPage(2);
        request.setSize(20);
        request.setSort("DESC");
        request.setColumnSort("monto");
        request.setQuery("personal");
        request.setStatus(Arrays.asList("1", "2", "3"));

        assertEquals(2, request.getPage());
        assertEquals(20, request.getSize());
        assertEquals("DESC", request.getSort());
        assertEquals("monto", request.getColumnSort());
        assertEquals("personal", request.getQuery());
        assertEquals(Arrays.asList("1", "2", "3"), request.getStatus());
    }

    @Test
    void testSolicitudPageRequestWithAllFields() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        
        request.setPage(1);
        request.setSize(10);
        request.setSort("ASC");
        request.setColumnSort("plazo");
        request.setQuery("hipotecario");
        List<String> statusList = Arrays.asList("1", "2");
        request.setStatus(statusList);

        assertEquals(1, request.getPage());
        assertEquals(10, request.getSize());
        assertEquals("ASC", request.getSort());
        assertEquals("plazo", request.getColumnSort());
        assertEquals("hipotecario", request.getQuery());
        assertEquals(statusList, request.getStatus());
    }

    @Test
    void testSolicitudPageRequestWithEmptyStatus() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        
        request.setStatus(Arrays.asList());

        assertNotNull(request.getStatus());
        assertTrue(request.getStatus().isEmpty());
    }

    @Test
    void testSolicitudPageRequestWithNullValues() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        
        request.setPage(null);
        request.setSize(null);
        request.setSort(null);
        request.setColumnSort(null);
        request.setQuery(null);
        request.setStatus(null);

        assertNull(request.getPage());
        assertNull(request.getSize());
        assertNull(request.getSort());
        assertNull(request.getColumnSort());
        assertNull(request.getQuery());
        assertNull(request.getStatus());
    }
}
