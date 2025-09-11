package co.com.crediya.pragma.solicitudes.model.estado;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoEnum {
    
    PENDIENTE(1L, "PENDIENTE", "PENDIENTE"),
    APROBADO(2L, "APROBADO", "APROBADA"),
    RECHAZADO(3L, "RECHAZADO", "RECHAZADA");
    
    private final Long id;
    private final String statusJson;
    private final String nombreBd;
    

    public static EstadoEnum fromStatusJson(String status) {
        if (status == null) {
            return PENDIENTE;
        }
        
        for (EstadoEnum estado : values()) {
            if (estado.statusJson.equalsIgnoreCase(status)) {
                return estado;
            }
        }
        return PENDIENTE;
    }
}
