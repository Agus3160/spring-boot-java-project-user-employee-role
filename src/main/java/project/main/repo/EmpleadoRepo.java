package project.main.repo;

import com.fiuni.distri.project.fiuni.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepo extends JpaRepository<Empleado, Integer>, JpaSpecificationExecutor<Empleado> {
}
