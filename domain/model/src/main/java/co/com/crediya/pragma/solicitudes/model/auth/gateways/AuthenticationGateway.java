package co.com.crediya.pragma.solicitudes.model.auth.gateways;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {


    Mono<UserRoleInfo> validateToken(String token);

    Mono<UserCompleteInfo> getCurrentUserInfo(String token);

    record UserRoleInfo(String email, Long rolId) {}

    record UserCompleteInfo(String email, String name, String lastname, Long rolId, String documentId) {}
}
