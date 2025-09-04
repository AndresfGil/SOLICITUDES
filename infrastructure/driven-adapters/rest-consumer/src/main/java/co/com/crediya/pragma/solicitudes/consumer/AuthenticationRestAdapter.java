package co.com.crediya.pragma.solicitudes.consumer;

import co.com.crediya.pragma.solicitudes.consumer.dto.UserRoleValidationResponse;
import co.com.crediya.pragma.solicitudes.consumer.dto.UserByEmailResponse;
import co.com.crediya.pragma.solicitudes.consumer.dto.BatchUsersResponse;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationRestAdapter implements AuthenticationGateway {

    private final WebClient webClient;
    
    @Value("${adapters.authentication.base-url:http://localhost:8080}")
    private String authenticationBaseUrl;

    @Override
    public Mono<UserRoleInfo> validateToken(String token) {
        log.info("Validando token con servicio de autenticación");
        
        return webClient
                .get()
                .uri(authenticationBaseUrl + "/api/v1/validate-token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(UserRoleValidationResponse.class)
                .doOnNext(response -> log.info("Token validado exitosamente para usuario: {}", response.getUser().getEmail()))
                .doOnError(error -> log.error("Error al validar token: {}", error.getMessage()))
                .map(response -> {
                    if (response == null || !response.isValid() || response.getUser() == null) {
                        throw new RuntimeException("Respuesta de validación inválida");
                    }
                    return new UserRoleInfo(response.getUser().getEmail(), response.getUser().getRolId());
                })
                .onErrorMap(throwable -> new RuntimeException("Error al validar token: " + throwable.getMessage()));
    }

    @Override
    public Mono<UserCompleteInfo> getCurrentUserInfo(String token) {
        log.info("Consultando información del usuario autenticado");
        
        return webClient
                .get()
                .uri(authenticationBaseUrl + "/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(UserByEmailResponse.class)
                .doOnNext(response -> log.info("Usuario encontrado: {}", response.getEmail()))
                .doOnError(error -> log.error("Error al consultar usuario: {}", error.getMessage()))
                .map(response -> new UserCompleteInfo(
                        response.getEmail(),
                        response.getName(),
                        response.getLastname(),
                        response.getRolId(),
                        response.getDocumentId()
                ))
                .onErrorMap(throwable -> new RuntimeException("Error al consultar usuario: " + throwable.getMessage()));
    }


    @Override
    public Flux<UserSolicitudInfo> getUsersForPageWithToken(List<String> emails, String token) {
        log.info("Consultando información de los usuarios listados con token: {}", emails.size());

        return webClient
                .post()
                .uri(authenticationBaseUrl + "/api/v1/users/batch")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("emails", emails))
                .retrieve()
                .bodyToMono(BatchUsersResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getUsers()))
                .doOnNext(user -> log.info("Usuario encontrado: {}", user.getEmail()))
                .doOnError(error -> log.error("Error al consultar usuarios: {}", error.getMessage()))
                .map(user -> new UserSolicitudInfo(
                        user.getName(),
                        user.getEmail(),
                        user.getBaseSalary()
                ))
                .onErrorMap(throwable -> new RuntimeException("Error al consultar usuarios: " + throwable.getMessage(), throwable));
    }


}
