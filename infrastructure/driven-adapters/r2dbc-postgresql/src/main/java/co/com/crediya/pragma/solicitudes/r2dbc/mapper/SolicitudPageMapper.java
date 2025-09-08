package co.com.crediya.pragma.solicitudes.r2dbc.mapper;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.r2dbc.dto.SolicitudFieldsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolicitudPageMapper {

    SolicitudFieldsPage toModel(SolicitudFieldsDto dto);
}
