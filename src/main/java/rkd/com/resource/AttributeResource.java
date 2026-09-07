package rkd.com.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import rkd.com.mapper.AttributeMapper;
import rkd.com.model.AttributeModel;
import rkd.com.request.CreateAttributeRequest;
import rkd.com.request.UpdateAttributeRequest;
import rkd.com.service.AttributeService;

@Path("/attribute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributeResource {

    private final AttributeService attributeService;
    private final AttributeMapper attributeMapper;

    public AttributeResource(AttributeService attributeService, AttributeMapper attributeMapper) {
        this.attributeService = attributeService;
        this.attributeMapper = attributeMapper;
    }

    @GET
    public Response findAll(@QueryParam("status") Boolean status) {
        return Response.ok(attributeService.findAll(status).stream().map(attributeMapper::toResponse).toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(attributeMapper.toResponse(attributeService.findById(id))).build();
    }

    @POST
    public Response create(CreateAttributeRequest request) {
        AttributeModel attribute = attributeMapper.toModel(request);
        return Response.status(Response.Status.CREATED)
                .entity(attributeMapper.toResponse(attributeService.create(attribute)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UpdateAttributeRequest request) {
        AttributeModel attribute = attributeMapper.toModel(request);
        return Response.ok(attributeMapper.toResponse(attributeService.update(id, attribute))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return attributeService.delete(id)
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

}
