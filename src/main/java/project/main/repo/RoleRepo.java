package project.main.repo;

import com.fiuni.distri.project.fiuni.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
}
