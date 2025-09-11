package co.com.crediya.pragma.solicitudes.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqscapacidad")
public record SQSSenderCapacidadProperties(
     String region,
     String queueUrl,
     String endpoint){
}

