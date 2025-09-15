package co.com.crediya.pragma.solicitudes.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqsaprobacion")

public record SQSSenderAprobacionProperties(
        String region,
        String queueUrl,
        String endpoint){
}
