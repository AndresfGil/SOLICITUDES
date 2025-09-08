package co.com.crediya.pragma.solicitudes.model.page;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersForPageResponseTest {

    @Test
    void testUsersForPageResponseConstructor() {
        UsersForPageResponse response = new UsersForPageResponse(
                "Juan Pérez",
                "juan@example.com",
                3000000L
        );

        assertEquals("Juan Pérez", response.name());
        assertEquals("juan@example.com", response.email());
        assertEquals(3000000L, response.baseSalary());
    }

    @Test
    void testUsersForPageResponseWithDifferentValues() {
        UsersForPageResponse response = new UsersForPageResponse(
                "María García",
                "maria@example.com",
                5000000L
        );

        assertEquals("María García", response.name());
        assertEquals("maria@example.com", response.email());
        assertEquals(5000000L, response.baseSalary());
    }

    @Test
    void testUsersForPageResponseWithNullValues() {
        UsersForPageResponse response = new UsersForPageResponse(
                null,
                null,
                null
        );

        assertNull(response.name());
        assertNull(response.email());
        assertNull(response.baseSalary());
    }

    @Test
    void testUsersForPageResponseWithEmptyStrings() {
        UsersForPageResponse response = new UsersForPageResponse(
                "",
                "",
                0L
        );

        assertEquals("", response.name());
        assertEquals("", response.email());
        assertEquals(0L, response.baseSalary());
    }

    @Test
    void testUsersForPageResponseWithZeroSalary() {
        UsersForPageResponse response = new UsersForPageResponse(
                "Test User",
                "test@example.com",
                0L
        );

        assertEquals("Test User", response.name());
        assertEquals("test@example.com", response.email());
        assertEquals(0L, response.baseSalary());
    }

    @Test
    void testUsersForPageResponseWithLargeValues() {
        UsersForPageResponse response = new UsersForPageResponse(
                "Very Long User Name With Many Characters",
                "very.long.email.address@very.long.domain.name.com",
                999999999L
        );

        assertEquals("Very Long User Name With Many Characters", response.name());
        assertEquals("very.long.email.address@very.long.domain.name.com", response.email());
        assertEquals(999999999L, response.baseSalary());
    }
}
