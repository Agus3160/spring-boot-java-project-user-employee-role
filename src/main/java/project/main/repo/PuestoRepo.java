package project.main.repo;

import com.fiuni.distri.project.fiuni.domain.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoRepo extends JpaRepository<Puesto, Integer> { }
