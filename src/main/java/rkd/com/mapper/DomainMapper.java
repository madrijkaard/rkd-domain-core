package rkd.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rkd.com.model.DomainModel;
import rkd.com.request.CreateDomainRequest;
import rkd.com.request.UpdateDomainRequest;
import rkd.com.response.SearchDomainResponse;
import rkd.com.response.SearchSubdomainResponse;

@Mapper(componentModel = "cdi", uses = AttributeMapper.class)
public interface DomainMapper {

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "parent.id", source = "parentDomainId")
    @Mapping(target = "subdomains", ignore = true)
    @Mapping(target = "status", ignore = true)
    DomainModel toModel(CreateDomainRequest request);

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "parent.id", source = "parentDomainId")
    @Mapping(target = "subdomains", ignore = true)
    DomainModel toModel(UpdateDomainRequest request);

    @Mapping(target = "parentDomainId", source = "parent.id")
    SearchDomainResponse toResponse(DomainModel model);

    SearchSubdomainResponse toSubdomainResponse(DomainModel model);
}
