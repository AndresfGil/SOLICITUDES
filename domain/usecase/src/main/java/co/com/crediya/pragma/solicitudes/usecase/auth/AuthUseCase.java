package co.com.crediya.pragma.solicitudes.usecase.auth;

import co.com.crediya.pragma.solicitudes.model.auth.exception.UnauthorizedUserException;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserRoleInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserCompleteInfo;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class AuthUseCase {

    private static final Logger LOGGER = Logger.getLogger(AuthUseCase.class.getName());

    private static final Long ADMIN_ROLE = 1L;
    private static final Long ADVISOR_ROLE = 2L;
    private static final Long CLIENT_ROLE = 3L;

    private final AuthenticationGateway authenticationGateway;

    public AuthUseCase(AuthenticationGateway authenticationGateway) {
        this.authenticationGateway = authenticationGateway;
    }


    public Mono<UserCompleteInfo> validateClientUserForSelf(String token, String documentId) {
        return authenticationGateway.validateToken(token)
                .doOnNext(userInfo -> LOGGER.info(() ->
                        String.format("Usuario validado: %s - Rol: %s", userInfo.email(), userInfo.rolId())))
                .flatMap(userInfo -> {
                    if (!isClient(userInfo.rolId())) {
                        LOGGER.warning(() ->
                                String.format("Usuario %s no es CLIENTE (rol: %s), no autorizado", userInfo.email(), userInfo.rolId()));
                        return Mono.error(UnauthorizedUserException.notClientUser());
                    }
                    
                    LOGGER.info(() ->
                            String.format("Usuario %s es CLIENTE, validando documento de identidad", userInfo.email()));
                    
                    return authenticationGateway.getCurrentUserInfo(token)
                            .flatMap(completeUser -> {
                                if (completeUser.documentId().equals(documentId)) {
                                    LOGGER.info(() ->
                                            String.format("Documento validado para usuario %s", userInfo.email()));
                                    return Mono.just(completeUser);
                                } else {
                                    LOGGER.warning(() ->
                                            String.format("Documento no coincide para usuario %s. Esperado: %s, Recibido: %s", 
                                                    userInfo.email(), completeUser.documentId(), documentId));
                                    return Mono.error(UnauthorizedUserException.documentMismatch());
                                }
                            });
                })
                .doOnError(error -> LOGGER.severe(() ->
                        "Error en validación de usuario CLIENTE para sí mismo: " + error.getMessage()));
    }


    public Mono<UserRoleInfo> validateClientUser(String token) {
        return authenticationGateway.validateToken(token)
                .doOnNext(userInfo -> LOGGER.info(() ->
                        String.format("Usuario validado: %s - Rol: %s", userInfo.email(), userInfo.rolId())))
                .flatMap(userInfo -> {
                    if (isClient(userInfo.rolId())) {
                        LOGGER.info(() ->
                                String.format("Usuario %s es CLIENTE, autorizado para crear solicitudes", userInfo.email()));
                        return Mono.just(userInfo);
                    } else {
                        LOGGER.warning(() ->
                                String.format("Usuario %s no es CLIENTE (rol: %s), no autorizado", userInfo.email(), userInfo.rolId()));
                        return Mono.error(UnauthorizedUserException.notClientUser());
                    }
                })
                .doOnError(error -> LOGGER.severe(() ->
                        "Error en validación de usuario CLIENTE: " + error.getMessage()));
    }



    public Mono<UserRoleInfo> validateAdminAdvisor(String token) {
        LOGGER.info("Validando token de admin o asesor");

        return authenticationGateway.validateToken(token)
                .doOnNext(userInfo -> LOGGER.info(() ->
                        String.format("Token validado para usuario: %s", userInfo.email())))
                .flatMap(userInfo -> {
                    if (isAdmin(userInfo.rolId()) || isAdvisor(userInfo.rolId())) {
                        LOGGER.info(() ->
                                String.format("Usuario %s es Administrativo autorizado", userInfo.email()));
                        return Mono.just(userInfo);
                    } else {
                        LOGGER.warning(() ->
                                String.format("Usuario %s no es Administrativo (rol: %s)", userInfo.email(), userInfo.rolId()));
                        return Mono.error(UnauthorizedUserException.notAdminUser());
                    }
                })
                .doOnError(error -> LOGGER.severe(() ->
                        "Error al validar token: " + error.getMessage()));
    }

    private boolean isClient(Long rolId) {
        return rolId != null && rolId.equals(CLIENT_ROLE);
    }

    private boolean isAdmin(Long rolId) {
        return rolId != null && rolId.equals(ADMIN_ROLE);
    }

    private boolean isAdvisor(Long rolId) {
        return rolId != null && rolId.equals(ADVISOR_ROLE);
    }
}
