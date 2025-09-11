package co.com.crediya.pragma.solicitudes.sqs.sender.mapper;

import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.solicitud.PrestamoAprobado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacionCapacidadMapper {

    public String toJson(ValidacionCapacidad data) {
        if (data == null) return "null";

        StringBuilder json = new StringBuilder();
        json.append("{");

        // salarioBase
        json.append("\"salarioBase\":");
        if (data.getSalarioBase() != null) {
            json.append(data.getSalarioBase().toPlainString());
        } else {
            json.append("null");
        }
        json.append(",");

        // monto
        json.append("\"monto\":");
        if (data.getMonto() != null) {
            json.append(data.getMonto().toPlainString());
        } else {
            json.append("null");
        }
        json.append(",");

        // tasaInteres
        json.append("\"tasaInteres\":");
        if (data.getTasaInteres() != null) {
            json.append(data.getTasaInteres());
        } else {
            json.append("null");
        }
        json.append(",");

        // plazo
        json.append("\"plazo\":");
        if (data.getPlazo() != null) {
            json.append(data.getPlazo());
        } else {
            json.append("null");
        }
        json.append(",");

        // prestamosAprobados
        json.append("\"prestamosAprobados\":");
        List<PrestamoAprobado> list = data.getPrestamosAprobados();
        if (list == null) {
            json.append("null");
        } else {
            json.append("[");
            for (int i = 0; i < list.size(); i++) {
                PrestamoAprobado p = list.get(i);
                json.append("{");
                json.append("\"monto\":");
                if (p.getMonto() != null) {
                    json.append(p.getMonto().toPlainString());
                } else {
                    json.append("null");
                }
                json.append(",");
                json.append("\"plazo\":");
                if (p.getPlazo() != null) {
                    json.append(p.getPlazo());
                } else {
                    json.append("null");
                }
                json.append(",");
                json.append("\"tasaInteres\":");
                if (p.getTasaInteres() != null) {
                    json.append(p.getTasaInteres());
                } else {
                    json.append("null");
                }
                json.append("}");
                if (i < list.size() - 1) json.append(",");
            }
            json.append("]");
        }

        json.append("}");
        return json.toString();
    }
}


