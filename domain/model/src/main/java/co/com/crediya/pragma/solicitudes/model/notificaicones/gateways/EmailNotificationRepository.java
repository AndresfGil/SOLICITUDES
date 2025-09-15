package co.com.crediya.pragma.solicitudes.model.notificaicones.gateways;

import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface EmailNotificationRepository {

    Mono<EmailNotification> sendNotification(EmailNotification emailNotification);

}
