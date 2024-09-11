package project.main.specifications;

import com.fiuni.distri.project.fiuni.domain.Role;
import org.springframework.data.jpa.domain.Specification;
import project.main.specifications.base.BaseSpecification;

public class RoleSpecification  extends BaseSpecification<Role> {

    public static Specification<Role> hasRole(String rol) {
        return (root, query, criteriaBuilder) -> {
            if (rol == null || rol.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("rol"), "%" + rol + "%");
        };
    }

}
