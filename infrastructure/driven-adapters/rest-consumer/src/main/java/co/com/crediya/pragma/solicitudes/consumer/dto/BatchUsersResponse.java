package co.com.crediya.pragma.solicitudes.consumer.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BatchUsersResponse {
    private List<UsersForPageResponse> users;
    private Integer totalFound;
    private Integer totalRequested;
}
