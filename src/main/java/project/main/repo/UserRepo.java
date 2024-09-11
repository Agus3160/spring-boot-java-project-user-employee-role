package project.main.repo;

import com.fiuni.distri.project.fiuni.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
}
