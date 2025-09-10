package co.com.crediya.pragma.solicitudes.sqs.sender.mapper;

import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EmailNotificationMapper {

    /**
     * Convierte un objeto EmailNotification a JSON string de forma manual
     * @param emailNotification objeto a convertir
     * @return JSON string del objeto
     */
    public String toJson(EmailNotification emailNotification) {
        if (emailNotification == null) {
            return "null";
        }

        StringBuilder json = new StringBuilder();
        json.append("{");

        // requestId
        json.append("\"requestId\":");
        if (emailNotification.getRequestId() != null) {
            json.append(emailNotification.getRequestId());
        } else {
            json.append("null");
        }
        json.append(",");

        // status
        json.append("\"status\":");
        if (emailNotification.getStatus() != null) {
            json.append("\"").append(escapeJsonString(emailNotification.getStatus())).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        // emailClient
        json.append("\"emailClient\":");
        if (emailNotification.getEmailClient() != null) {
            json.append("\"").append(escapeJsonString(emailNotification.getEmailClient())).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        // identityDocument
        json.append("\"identityDocument\":");
        if (emailNotification.getIdentityDocument() != null) {
            json.append("\"").append(escapeJsonString(emailNotification.getIdentityDocument())).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        // loanAmount
        json.append("\"loanAmount\":");
        if (emailNotification.getLoanAmount() != null) {
            json.append(emailNotification.getLoanAmount());
        } else {
            json.append("null");
        }
        json.append(",");

        // loanType
        json.append("\"loanType\":");
        if (emailNotification.getLoanType() != null) {
            json.append("\"").append(escapeJsonString(emailNotification.getLoanType())).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        // customMessage
        json.append("\"customMessage\":");
        if (emailNotification.getCustomMessage() != null) {
            json.append("\"").append(escapeJsonString(emailNotification.getCustomMessage())).append("\"");
        } else {
            json.append("null");
        }

        json.append("}");

        return json.toString();
    }

    /**
     * Escapa caracteres especiales en strings para JSON
     * @param input string a escapar
     * @return string escapado
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
