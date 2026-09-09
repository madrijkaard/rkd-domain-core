package rkd.com.mapper;

import org.mapstruct.Mapper;
import rkd.com.model.OptionModel;
import rkd.com.request.CreateOptionRequest;
import rkd.com.request.UpdateOptionRequest;
import rkd.com.response.SearchOptionResponse;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta-cdi")
public interface OptionMapper {

    @Mapping(target = "attribute.id", source = "attributeId")
    OptionModel toModel(CreateOptionRequest request);

    @Mapping(target = "attribute.id", source = "attributeId")
    OptionModel toModel(UpdateOptionRequest request);

    SearchOptionResponse toResponse(OptionModel model);
}
