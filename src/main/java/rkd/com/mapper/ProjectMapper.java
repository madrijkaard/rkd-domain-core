package rkd.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rkd.com.model.ProjectModel;
import rkd.com.request.CreateProjectRequest;
import rkd.com.request.UpdateProjectRequest;
import rkd.com.response.SearchProjectResponse;

@Mapper(componentModel = "jakarta-cdi", uses = DomainMapper.class)
public interface ProjectMapper {
    @Mapping(target = "domains", ignore = true)
    @Mapping(target = "status", ignore = true)
    ProjectModel toModel(CreateProjectRequest request);
    @Mapping(target = "domains", ignore = true)
    ProjectModel toModel(UpdateProjectRequest request);
    SearchProjectResponse toResponse(ProjectModel model);
}
