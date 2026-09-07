package rkd.com.mapper;

import org.mapstruct.Mapper;
import rkd.com.model.OptionModel;
import rkd.com.request.CreateOptionRequest;
import rkd.com.request.UpdateOptionRequest;
import rkd.com.response.SearchOptionResponse;

@Mapper(componentModel = "jakarta-cdi")
public interface OptionMapper {

    OptionModel toModel(CreateOptionRequest request);

    OptionModel toModel(UpdateOptionRequest request);

    SearchOptionResponse toResponse(OptionModel model);
}
