package rkd.com.definition;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import rkd.com.dto.ExceptionDto;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.type.ExceptionType;

import java.time.LocalDateTime;

@Provider
public class DomainNotFoundDefinition implements ExceptionMapper<DomainNotFoundException> {

    @Override
    public Response toResponse(DomainNotFoundException exception) {
        ExceptionDto response = new ExceptionDto(
                exception.getMessage(),
                ExceptionType.DOMAIN_NOT_FOUND,
                LocalDateTime.now()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
