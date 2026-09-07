package rkd.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rkd.com.model.AttributeModel;
import rkd.com.request.CreateAttributeRequest;
import rkd.com.request.UpdateAttributeRequest;
import rkd.com.response.SearchAttributeResponse;

@Mapper(componentModel = "jakarta-cdi", uses = OptionMapper.class)
public interface AttributeMapper {

    @Mapping(target = "domain.id", source = "domainId")
    @Mapping(target = "option.id", source = "optionId")
    @Mapping(target = "status", ignore = true)
    AttributeModel toModel(CreateAttributeRequest request);

    @Mapping(target = "domain.id", source = "domainId")
    @Mapping(target = "option.id", source = "optionId")
    AttributeModel toModel(UpdateAttributeRequest request);

    @Mapping(target = "domainId", source = "domain.id")
    SearchAttributeResponse toResponse(AttributeModel model);
}
