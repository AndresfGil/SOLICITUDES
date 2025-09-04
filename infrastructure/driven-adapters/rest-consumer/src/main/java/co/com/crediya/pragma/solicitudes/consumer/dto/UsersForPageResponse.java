package co.com.crediya.pragma.solicitudes.consumer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsersForPageResponse {
    private String email;
    private String name;
    private Long baseSalary;


}
