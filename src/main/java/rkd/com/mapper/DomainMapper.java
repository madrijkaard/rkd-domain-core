package rkd.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rkd.com.model.DomainModel;
import rkd.com.request.CreateDomainRequest;
import rkd.com.request.UpdateDomainRequest;
import rkd.com.response.SearchDomainResponse;
import rkd.com.response.SearchSubdomainResponse;

@Mapper(componentModel = "jakarta-cdi", uses = AttributeMapper.class)
public interface DomainMapper {

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "relatedDomains", source = "relatedDomainIds")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "project.id", source = "projectId")
    DomainModel toModel(CreateDomainRequest request);

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "relatedDomains", source = "relatedDomainIds")
    @Mapping(target = "project.id", source = "projectId")
    DomainModel toModel(UpdateDomainRequest request);

    @Mapping(target = "relatedDomains", source = "relatedDomains")
    @Mapping(target = "projectId", source = "project.id")
    SearchDomainResponse toResponse(DomainModel model);

    SearchSubdomainResponse toSubdomainResponse(DomainModel model);

    default DomainModel map(Long id) {
        if (id == null) return null;
        DomainModel domain = new DomainModel();
        domain.setId(id);
        return domain;
    }
}
