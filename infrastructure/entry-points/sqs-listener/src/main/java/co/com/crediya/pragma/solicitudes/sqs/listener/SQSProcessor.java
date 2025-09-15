package co.com.crediya.pragma.solicitudes.sqs.listener;

import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.estados.EstadoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final EstadoUseCase estadoUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Procesando mensaje SQS: {}", message.body());
        
        return Mono.fromCallable(() -> {
                    try {
                        return objectMapper.readValue(message.body(), EstadoValidacion.class);
                    } catch (Exception e) {
                        log.error("Error al parsear el mensaje JSON: {}", e.getMessage(), e);
                        throw new RuntimeException("Error al parsear el mensaje JSON");
                    }
                })
                .flatMap(estadoValidacion -> {
                    log.info("EstadoValidacion parseado: idSolicitud={}, status={}", 
                            estadoValidacion.getIdSolicitud(), estadoValidacion.getStatus());
                    return estadoUseCase.updateEstadoValidacion(estadoValidacion);
                })
                .doOnSuccess(result -> log.info("EstadoValidacion procesado exitosamente"))
                .doOnError(error -> log.error("Error al procesar EstadoValidacion: {}", error.getMessage(), error))
                .then();
    }
}
