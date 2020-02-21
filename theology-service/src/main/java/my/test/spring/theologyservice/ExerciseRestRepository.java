package my.test.spring.theologyservice;

import my.test.spring.theologyservice.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "exercise", path = "exercise")
public interface ExerciseRestRepository extends JpaRepository<Exercise, String> {
}
