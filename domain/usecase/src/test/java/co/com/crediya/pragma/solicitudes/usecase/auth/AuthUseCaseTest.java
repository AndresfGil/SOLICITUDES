package co.com.crediya.pragma.solicitudes.usecase.auth;

import co.com.crediya.pragma.solicitudes.model.auth.exception.UnauthorizedUserException;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserRoleInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserCompleteInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para AuthUseCase")
class AuthUseCaseTest {

    @Mock
    private AuthenticationGateway authenticationGateway;

    @InjectMocks
    private AuthUseCase authUseCase;

    private UserRoleInfo clientUserInfo;
    private UserRoleInfo adminUserInfo;
    private UserRoleInfo advisorUserInfo;
    private UserCompleteInfo completeUserInfo;

    @BeforeEach
    void setUp() {
        clientUserInfo = new UserRoleInfo("client@test.com", 3L);
        adminUserInfo = new UserRoleInfo("admin@test.com", 1L);
        advisorUserInfo = new UserRoleInfo("advisor@test.com", 2L);
        completeUserInfo = new UserCompleteInfo("client@test.com", "Juan", "Pérez", 3L, "12345678");
    }

    @Test
    @DisplayName("Debe validar usuario CLIENTE exitosamente")
    void shouldValidateClientUserSuccessfully() {
        String token = "valid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(clientUserInfo));

        StepVerifier.create(authUseCase.validateClientUser(token))
                .expectNext(clientUserInfo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no es CLIENTE")
    void shouldThrowExceptionWhenUserIsNotClient() {
        String token = "valid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(adminUserInfo));

        StepVerifier.create(authUseCase.validateClientUser(token))
                .expectError(UnauthorizedUserException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe validar usuario CLIENTE para sí mismo exitosamente")
    void shouldValidateClientUserForSelfSuccessfully() {
        String token = "valid.token";
        String documentId = "12345678";
        
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(clientUserInfo));
        when(authenticationGateway.getCurrentUserInfo(token)).thenReturn(Mono.just(completeUserInfo));

        StepVerifier.create(authUseCase.validateClientUserForSelf(token, documentId))
                .expectNext(completeUserInfo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no es CLIENTE para validación personal")
    void shouldThrowExceptionWhenUserIsNotClientForSelf() {
        String token = "valid.token";
        String documentId = "12345678";
        
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(adminUserInfo));

        StepVerifier.create(authUseCase.validateClientUserForSelf(token, documentId))
                .expectError(UnauthorizedUserException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando documento no coincide para usuario CLIENTE")
    void shouldThrowExceptionWhenDocumentDoesNotMatchForClient() {
        String token = "valid.token";
        String documentId = "87654321";
        
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(clientUserInfo));
        when(authenticationGateway.getCurrentUserInfo(token)).thenReturn(Mono.just(completeUserInfo));

        StepVerifier.create(authUseCase.validateClientUserForSelf(token, documentId))
                .expectError(UnauthorizedUserException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe validar usuario ADMIN exitosamente")
    void shouldValidateAdminUserSuccessfully() {
        String token = "valid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(adminUserInfo));

        StepVerifier.create(authUseCase.validateAdminAdvisor(token))
                .expectNext(adminUserInfo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe validar usuario ASESOR exitosamente")
    void shouldValidateAdvisorUserSuccessfully() {
        String token = "valid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(advisorUserInfo));

        StepVerifier.create(authUseCase.validateAdminAdvisor(token))
                .expectNext(advisorUserInfo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no es ADMIN ni ASESOR")
    void shouldThrowExceptionWhenUserIsNotAdminNorAdvisor() {
        String token = "valid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(clientUserInfo));

        StepVerifier.create(authUseCase.validateAdminAdvisor(token))
                .expectError(UnauthorizedUserException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe manejar error de validación de token")
    void shouldHandleTokenValidationError() {
        String token = "invalid.token";
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.error(new RuntimeException("Token inválido")));

        StepVerifier.create(authUseCase.validateClientUser(token))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe manejar error al obtener información completa del usuario")
    void shouldHandleGetCurrentUserInfoError() {
        String token = "valid.token";
        String documentId = "12345678";
        
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(clientUserInfo));
        when(authenticationGateway.getCurrentUserInfo(token)).thenReturn(Mono.error(new RuntimeException("Error de conexión")));

        StepVerifier.create(authUseCase.validateClientUserForSelf(token, documentId))
                .expectError(RuntimeException.class)
                .verify();
    }



    @Test
    @DisplayName("Debe manejar rolId cero en validación de ADMIN/ASESOR")
    void shouldHandleZeroRolIdInAdminAdvisorValidation() {
        String token = "valid.token";
        UserRoleInfo userWithZeroRole = new UserRoleInfo("test@test.com", 0L);
        
        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(userWithZeroRole));

        StepVerifier.create(authUseCase.validateAdminAdvisor(token))
                .expectError(UnauthorizedUserException.class)
                .verify();
    }
}
