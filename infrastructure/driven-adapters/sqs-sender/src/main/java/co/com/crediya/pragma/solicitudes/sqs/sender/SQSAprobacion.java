package co.com.crediya.pragma.solicitudes.sqs.sender;

import co.com.crediya.pragma.solicitudes.model.aprobacion.SolicitudAprobada;
import co.com.crediya.pragma.solicitudes.model.aprobacion.gateway.SolicitudAprobadaRepository;
import co.com.crediya.pragma.solicitudes.sqs.sender.config.SQSSenderAprobacionProperties;
import co.com.crediya.pragma.solicitudes.sqs.sender.mapper.SolicitudAprobadaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSAprobacion implements SolicitudAprobadaRepository {
    private final SQSSenderAprobacionProperties properties;
    private final SqsAsyncClient client;
    private final SolicitudAprobadaMapper solicitudAprobadaMapper;


    @Override
    public Mono<SolicitudAprobada> saveSolicitudAprobada(SolicitudAprobada solicitudAprobada) {
        return Mono.fromCallable(() -> buildRequest(solicitudAprobada))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(response -> solicitudAprobada);
    }


    private SendMessageRequest buildRequest(SolicitudAprobada solicitudAprobada) {
        String messageJson = solicitudAprobadaMapper.toJson(solicitudAprobada);
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(messageJson)
                .build();
    }


}
