package co.com.crediya.pragma.solicitudes.model.page;

import lombok.Data;
import java.util.List;

@Data
public class SolicitudPageRequest {

    // Paginado
    Integer page = 0;
    Integer size = 50;

    // Filtros
    String sort = "ASC";
    String columnSort = ""; // Organizar por Columna
    String query = "%";  // Filtrar por tipo de prestamo.
    List<String> status;


    // String query que ayuda a buscar segun el patron indicado al hacer la consulta
    public String fixQueryFL() {
        return ("*".equals(query)) ? "%" : "%" + query + "%";
    }
}