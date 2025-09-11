package co.com.crediya.pragma.solicitudes.consumer;

import co.com.crediya.pragma.solicitudes.consumer.dto.*;
import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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
    public Mono<UserValidateInfo> validateUser(String email, String documentoIdentidad) {
        log.info("Validando token con servicio de autenticación");

        return bearer().flatMap(tok ->
                webClient.post()
                        .uri(authenticationBaseUrl + "/api/v1/validate-user")
                        .headers(h -> h.setBearerAuth(tok))
                        .bodyValue(new UserExistsRequest(email, documentoIdentidad))
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(response -> {
                            Boolean exists = (Boolean) response.get("exists");
                            Object baseSalaryObj = response.get("baseSalary");
                            BigDecimal baseSalary = null;
                            if (baseSalaryObj != null) {
                                if (baseSalaryObj instanceof Number num) {
                                    baseSalary = BigDecimal.valueOf(num.doubleValue());
                                } else {
                                    baseSalary = new BigDecimal(baseSalaryObj.toString());
                                }
                            }
                            return UserValidateInfo.builder()
                                    .exists(exists != null && exists)
                                    .baseSalary(baseSalary)
                                    .build();
                        })
        );
    }

    @Override
    public Flux<UsersForPageResponse> getUsersForPage(List<String> emails) {
        log.info("Consultando información de los usuarios listados con token: {}", emails.size());
        return bearer().flatMapMany(tok->
                webClient.post()
                        .uri(authenticationBaseUrl + "/api/v1/users/batch")
                        .headers(h -> h.setBearerAuth(tok))
                        .bodyValue(Map.of("emails", emails))
                        .retrieve()
                        .bodyToMono(Map.class)
                        .doOnNext(r -> log.warn("Request emails: " + emails))
                        .doOnNext(r -> log.warn("Response: " + r))
                        .doOnError(e -> log.error("Error: " + e.getMessage()))
                        .flatMapMany(response -> {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> users = (List<Map<String, Object>>) response.get("users");
                            if (users == null) {
                                return Flux.empty();
                            }
                            return Flux.fromIterable(users)
                                    .map(userMap -> UsersForPageResponse.builder()
                                            .name((String) userMap.get("name"))
                                            .email((String) userMap.get("email"))
                                            .baseSalary(userMap.get("baseSalary") != null ? 
                                                    Long.valueOf(userMap.get("baseSalary").toString()) : null)
                                            .build());
                        })
        );
    }

    private Mono<String> bearer(){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(a -> a instanceof JwtAuthenticationToken)
                .map(a -> ((JwtAuthenticationToken) a).getToken().getTokenValue());
    }



}
