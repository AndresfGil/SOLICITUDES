package co.com.crediya.pragma.solicitudes.sqs.sender.mapper;


import co.com.crediya.pragma.solicitudes.model.aprobacion.SolicitudAprobada;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SolicitudAprobadaMapper {

    public String toJson(SolicitudAprobada solicitudAprobada) {


        StringBuilder json = new StringBuilder();
        json.append("{");



        // metrica
        json.append("\"metrica\":");
        if (solicitudAprobada.getMetrica() != null) {
            json.append("\"").append(escapeJsonString(solicitudAprobada.getMetrica())).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");


        // monto
        json.append("\"monto\":");
        if (solicitudAprobada.getMonto() != null) {
            json.append(solicitudAprobada.getMonto());
        } else {
            json.append("null");
        }
        json.append(",");



        json.append("}");

        return json.toString();
    }

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
