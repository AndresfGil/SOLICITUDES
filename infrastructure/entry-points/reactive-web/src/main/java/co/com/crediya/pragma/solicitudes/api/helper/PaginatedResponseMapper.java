package co.com.crediya.pragma.solicitudes.api.helper;

import co.com.crediya.pragma.solicitudes.api.dto.PaginatedResponseDTO;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaginatedResponseMapper {
    
    public <T> Mono<PaginatedResponseDTO<T>> toPaginatedResponse(
            SolicitudRepository.PaginatedResult<T> result) {
        
        return result.content().collectList()
            .map(content -> PaginatedResponseDTO.<T>builder()
                .content(content)
                .pageInfo(PaginatedResponseDTO.PageInfoDTO.builder()
                    .page(result.currentPage())
                    .size(result.pageSize())
                    .totalElements(result.totalElements())
                    .totalPages(result.totalPages())
                    .hasNext(result.hasNext())
                    .hasPrevious(result.hasPrevious())
                    .build())
                .build());
    }
}
