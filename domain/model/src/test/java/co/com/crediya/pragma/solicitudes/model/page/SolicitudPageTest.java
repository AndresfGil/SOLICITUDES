package co.com.crediya.pragma.solicitudes.model.page;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudPageTest {

    @Test
    void constructorAndGetters_work() {
        var data = List.of("a", "b");
        var page = new SolicitudPage<>(data, 2L, 10, 0, true, "monto,asc");

        assertEquals(data, page.getData());
        assertEquals(2L, page.getTotalRows());
        assertEquals(10, page.getPageSize());
        assertEquals(0, page.getPageNum());
        assertTrue(page.getHasNext());
        assertEquals("monto,asc", page.getSort());
    }

    @Test
    void addAndIterator_work() {
        var page = new SolicitudPage<String>(List.of(), 0L, 10, 0, false, "");
        assertFalse(page.iterator().hasNext());
        assertEquals("x", page.iterator().next());
    }
}


