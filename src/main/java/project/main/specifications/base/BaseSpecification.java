package project.main.specifications.base;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification<T> {

    public static <T> Specification<T> applyDeleteFilter(DeleteFilterOption filterOption) {
        return (root, query, criteriaBuilder) -> {
            switch (filterOption) {
                case NOT_DELETED:
                    return criteriaBuilder.isNull(root.get("deletedAt"));  // Solo los no eliminados
                case DELETED:
                    return criteriaBuilder.isNotNull(root.get("deletedAt"));  // Solo los eliminados
                case ALL:
                default:
                    return criteriaBuilder.conjunction();  // Todos los elementos
            }
        };
    }

}
