package co.com.crediya.pragma.solicitudes.sqs.sender;

import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.EmailNotificationRepository;
import co.com.crediya.pragma.solicitudes.sqs.sender.config.SQSSenderProperties;
import co.com.crediya.pragma.solicitudes.sqs.sender.mapper.EmailNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements EmailNotificationRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final EmailNotificationMapper emailNotificationMapper;

    public Mono<EmailNotification> sendNotification(EmailNotification message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(response -> message);
    }


    private SendMessageRequest buildRequest(EmailNotification message) {
        String messageJson = emailNotificationMapper.toJson(message);
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(messageJson)
                .build();
    }
}
