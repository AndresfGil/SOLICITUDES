package co.com.crediya.pragma.solicitudes.consumer.dto;

import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BatchUsersResponse {
    List<UsersForPageResponse> results;
}
