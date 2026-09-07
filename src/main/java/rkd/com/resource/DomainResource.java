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
import jakarta.transaction.Transactional;
import rkd.com.mapper.DomainMapper;
import rkd.com.model.DomainModel;
import rkd.com.request.CreateDomainRequest;
import rkd.com.request.UpdateDomainRequest;
import rkd.com.service.DomainService;

@Path("/domain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DomainResource {

    private final DomainService domainService;
    private final DomainMapper domainMapper;

    public DomainResource(DomainService domainService, DomainMapper domainMapper) {
        this.domainService = domainService;
        this.domainMapper = domainMapper;
    }

    @GET
    @Transactional
    public Response findAll(@QueryParam("status") Boolean status) {
        return Response.ok(domainService.findAll(status).stream().map(domainMapper::toResponse).toList()).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(domainMapper.toResponse(domainService.findById(id))).build();
    }

    @POST
    @Transactional
    public Response create(CreateDomainRequest request) {
        DomainModel domain = domainMapper.toModel(request);
        return Response.status(Response.Status.CREATED)
                .entity(domainMapper.toResponse(domainService.create(domain)))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UpdateDomainRequest request) {
        DomainModel domain = domainMapper.toModel(request);
        return Response.ok(domainMapper.toResponse(domainService.update(id, domain))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return domainService.delete(id)
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

}
