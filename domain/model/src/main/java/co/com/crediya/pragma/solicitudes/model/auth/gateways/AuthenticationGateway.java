package co.com.crediya.pragma.solicitudes.model.auth.gateways;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthenticationGateway {


    Mono<UserValidateInfo> validateUser(String email, String documentoIdentidad);

    Flux<UsersForPageResponse> getUsersForPage(List<String> emails);
}
