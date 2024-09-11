package project.main.specifications;

import com.fiuni.distri.project.fiuni.domain.Empleado;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.main.specifications.base.BaseSpecification;

@Service
public class EmpleadoSpecification extends BaseSpecification<Empleado> {

    public static Specification<Empleado> hasCi(String ci) {
        return (root, query, criteriaBuilder) -> {
            if (ci == null || ci.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("ci"), "%" + ci + "%");
        };
    }

    public static Specification<Empleado> hasNombre(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre == null || nombre.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%");
        };
    }

    public static Specification<Empleado> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if ( userId == null ) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Empleado> hasPuestoId(Integer puestoId) {
        return (root, query, criteriaBuilder) -> {
            if ( puestoId == null ) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("puesto").get("id"), puestoId);
        };
    }

}
