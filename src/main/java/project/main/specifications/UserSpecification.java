package project.main.specifications;

import com.fiuni.distri.project.fiuni.domain.User;
import org.springframework.data.jpa.domain.Specification;
import project.main.specifications.base.BaseSpecification;

public class UserSpecification  extends BaseSpecification<User> {

    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("username"), "%" + username + "%");
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }


}
