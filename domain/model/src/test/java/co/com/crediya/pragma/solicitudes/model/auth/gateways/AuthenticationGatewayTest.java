package co.com.crediya.pragma.solicitudes.model.auth.gateways;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para AuthenticationGateway records")
class AuthenticationGatewayTest {

    @Test
    @DisplayName("Debe crear UserRoleInfo con campos correctos")
    void shouldCreateUserRoleInfoWithCorrectFields() {
        String email = "test@example.com";
        Long rolId = 3L;

        AuthenticationGateway.UserRoleInfo userRoleInfo = new AuthenticationGateway.UserRoleInfo(email, rolId);

        assertEquals(email, userRoleInfo.email());
        assertEquals(rolId, userRoleInfo.rolId());
    }

    @Test
    @DisplayName("Debe crear UserRoleInfo con valores nulos")
    void shouldCreateUserRoleInfoWithNullValues() {
        AuthenticationGateway.UserRoleInfo userRoleInfo = new AuthenticationGateway.UserRoleInfo(null, null);

        assertNull(userRoleInfo.email());
        assertNull(userRoleInfo.rolId());
    }

    @Test
    @DisplayName("Debe crear UserCompleteInfo con todos los campos")
    void shouldCreateUserCompleteInfoWithAllFields() {
        String email = "complete@example.com";
        String name = "Juan";
        String lastname = "Pérez";
        Long rolId = 3L;
        String documentId = "12345678";

        AuthenticationGateway.UserCompleteInfo userCompleteInfo = new AuthenticationGateway.UserCompleteInfo(
                email, name, lastname, rolId, documentId);

        assertEquals(email, userCompleteInfo.email());
        assertEquals(name, userCompleteInfo.name());
        assertEquals(lastname, userCompleteInfo.lastname());
        assertEquals(rolId, userCompleteInfo.rolId());
        assertEquals(documentId, userCompleteInfo.documentId());
    }


    @Test
    @DisplayName("Debe crear UserRoleInfo con email vacío")
    void shouldCreateUserRoleInfoWithEmptyEmail() {
        String email = "";
        Long rolId = 1L;

        AuthenticationGateway.UserRoleInfo userRoleInfo = new AuthenticationGateway.UserRoleInfo(email, rolId);

        assertEquals("", userRoleInfo.email());
        assertEquals(rolId, userRoleInfo.rolId());
    }

}
