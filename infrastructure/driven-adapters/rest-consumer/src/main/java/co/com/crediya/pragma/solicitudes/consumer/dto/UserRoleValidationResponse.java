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
public class UserRoleValidationResponse {
    private boolean valid;
    private UserInfo user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class UserInfo {
        private String email;
        private String name;
        private String lastname;
        private Long rolId;
    }
}
