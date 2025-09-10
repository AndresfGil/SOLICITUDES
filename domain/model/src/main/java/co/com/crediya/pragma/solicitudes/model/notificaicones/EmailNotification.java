package co.com.crediya.pragma.solicitudes.model.notificaicones;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailNotification {

        Long requestId;
        String status;
        String emailClient;
        String identityDocument;
        BigDecimal loanAmount;
        String loanType;
        String customMessage;

}
