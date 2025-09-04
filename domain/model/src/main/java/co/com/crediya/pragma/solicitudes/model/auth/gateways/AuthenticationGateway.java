package co.com.crediya.pragma.solicitudes.model.auth.gateways;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthenticationGateway {


    Mono<UserRoleInfo> validateToken(String token);

    Mono<UserCompleteInfo> getCurrentUserInfo(String token);

    Flux<UserSolicitudInfo> getUsersForPageWithToken(List<String> emails, String token);

    record UserRoleInfo(String email, Long rolId) {}

    record UserSolicitudInfo(String name, String email, Long baseSalary) {}

    record UserCompleteInfo(String email, String name, String lastname, Long rolId, String documentId) {}
}
