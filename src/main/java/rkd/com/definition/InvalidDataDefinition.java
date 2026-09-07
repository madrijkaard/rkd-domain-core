package rkd.com.definition;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import rkd.com.dto.ExceptionDto;
import rkd.com.exception.InvalidDataException;
import rkd.com.type.ExceptionType;

import java.time.LocalDateTime;

@Provider
public class InvalidDataDefinition implements ExceptionMapper<InvalidDataException> {

    @Override
    public Response toResponse(InvalidDataException exception) {
        ExceptionDto response = new ExceptionDto(
                exception.getMessage(),
                ExceptionType.INVALID_DATA,
                LocalDateTime.now()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
