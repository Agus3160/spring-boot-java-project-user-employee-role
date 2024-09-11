package project.main.service.base;

import org.springframework.data.domain.Page;
import project.main.utils.response.SuccessResponseDto;

public interface IBaseService<DTO, ENTITY> {

    ENTITY dtoToEntity(DTO dto);
    DTO entityToDto(ENTITY entity);

    DTO create(DTO dto);
    DTO updateById(int id, DTO dto);
    DTO getById(int id);

    //DELETING SERVICE
    SuccessResponseDto deleteById(int id);
    DTO softDeleteById(int id);

    //PAGINATION WITHOUT FILTERS
    Page<DTO> getAll(int page, int size);

}
