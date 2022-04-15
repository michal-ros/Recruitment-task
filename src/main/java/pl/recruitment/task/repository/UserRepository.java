package pl.recruitment.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.recruitment.task.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
