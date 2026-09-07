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
import rkd.com.mapper.OptionMapper;
import rkd.com.model.OptionModel;
import rkd.com.request.CreateOptionRequest;
import rkd.com.request.UpdateOptionRequest;
import rkd.com.service.OptionService;

@Path("/option")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OptionResource {

    private final OptionService optionService;
    private final OptionMapper optionMapper;

    public OptionResource(OptionService optionService, OptionMapper optionMapper) {
        this.optionService = optionService;
        this.optionMapper = optionMapper;
    }

    @GET
    public Response findAll(@QueryParam("status") Boolean status) {
        return Response.ok(optionService.findAll(status).stream().map(optionMapper::toResponse).toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(optionMapper.toResponse(optionService.findById(id))).build();
    }

    @POST
    public Response create(CreateOptionRequest request) {
        OptionModel option = optionMapper.toModel(request);
        return Response.status(Response.Status.CREATED)
                .entity(optionMapper.toResponse(optionService.create(option)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UpdateOptionRequest request) {
        OptionModel option = optionMapper.toModel(request);
        return Response.ok(optionMapper.toResponse(optionService.update(id, option))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return optionService.delete(id)
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

}
