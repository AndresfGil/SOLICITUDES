package co.com.crediya.pragma.solicitudes.sqs.sender;

import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.pragma.solicitudes.sqs.sender.config.SQSSenderCapacidadProperties;
import co.com.crediya.pragma.solicitudes.sqs.sender.mapper.ValidacionCapacidadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.UUID;


@Service
@Log4j2
@RequiredArgsConstructor
public class SQSValidation implements CapacidadEndeudamientoRepository {
    private final SQSSenderCapacidadProperties properties;
    private final SqsAsyncClient client;
    private final ValidacionCapacidadMapper validacionCapacidadMapper;


    public Mono<ValidacionCapacidad> sendInfoValidation(ValidacionCapacidad validacionCapacidad) {
        return Mono.defer(() -> {
                    String json = validacionCapacidadMapper.toJson(validacionCapacidad);
                    log.info("[SQSValidation] Payload: {}", json);
                    return Mono.just(buildRequest(json));
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(response -> validacionCapacidad);
    }

    private SendMessageRequest buildRequest(String messageJson) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(messageJson)
                .build();
    }
}
