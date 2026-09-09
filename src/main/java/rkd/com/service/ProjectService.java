package rkd.com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.ProjectNotFoundException;
import rkd.com.model.ProjectModel;
import rkd.com.repository.ProjectRepository;
import static rkd.com.message.ProjectMessage.*;
import java.util.List;

@ApplicationScoped
public class ProjectService {
    private final ProjectRepository repository;
    public ProjectService(ProjectRepository repository) { this.repository = repository; }
    public List<ProjectModel> findAll() { return repository.listAll(); }
    public List<ProjectModel> findAll(Boolean status) { return status == null ? findAll() : repository.list("status", status); }
    public ProjectModel findById(Long id) {
        ProjectModel project = repository.findById(id);
        if (project == null) throw new ProjectNotFoundException(PROJECT_NOT_FOUND);
        return project;
    }
    @Transactional public ProjectModel create(ProjectModel project) { project.setStatus(true); repository.persist(project); return project; }
    @Transactional public ProjectModel update(Long id, ProjectModel input) {
        ProjectModel project = findById(id);
        project.setCode(input.getCode()); project.setDescription(input.getDescription()); project.setStatus(input.getStatus());
        return project;
    }
    @Transactional public boolean delete(Long id) {
        ProjectModel project = findById(id);
        if (project.getDomains() != null && !project.getDomains().isEmpty()) throw new rkd.com.exception.InvalidActionException(PROJECT_HAS_DOMAINS);
        return repository.deleteById(id);
    }
}
