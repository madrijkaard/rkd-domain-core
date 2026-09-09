package rkd.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rkd.com.model.ProjectModel;

@ApplicationScoped
public class ProjectRepository implements PanacheRepository<ProjectModel> { }
