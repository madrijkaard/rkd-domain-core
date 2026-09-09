package rkd.com.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import rkd.com.mapper.ProjectMapper;
import rkd.com.request.CreateProjectRequest;
import rkd.com.request.UpdateProjectRequest;
import rkd.com.service.ProjectService;

@Path("/project")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {
    private final ProjectService service;
    private final ProjectMapper mapper;
    public ProjectResource(ProjectService service, ProjectMapper mapper) { this.service = service; this.mapper = mapper; }
    @GET @Transactional public Response findAll(@QueryParam("status") Boolean status) { return Response.ok(service.findAll(status).stream().map(mapper::toResponse).toList()).build(); }
    @GET @Path("/{id}") @Transactional public Response findById(@PathParam("id") Long id) { return Response.ok(mapper.toResponse(service.findById(id))).build(); }
    @POST @Transactional public Response create(CreateProjectRequest request) { return Response.status(Response.Status.CREATED).entity(mapper.toResponse(service.create(mapper.toModel(request)))).build(); }
    @PUT @Path("/{id}") @Transactional public Response update(@PathParam("id") Long id, UpdateProjectRequest request) { return Response.ok(mapper.toResponse(service.update(id, mapper.toModel(request)))).build(); }
    @DELETE @Path("/{id}") @Transactional public Response delete(@PathParam("id") Long id) { return service.delete(id) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build(); }
}
