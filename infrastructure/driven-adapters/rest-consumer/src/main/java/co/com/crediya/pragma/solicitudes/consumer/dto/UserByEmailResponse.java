package co.com.crediya.pragma.solicitudes.consumer.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserByEmailResponse {
    private String email;
    private String name;
    private String lastname;
    private Long rolId;
    private String documentId;
}
