package project.main.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import project.main.exception.ApiException;
import project.main.repo.RoleRepo;
import com.fiuni.distri.project.fiuni.domain.Role;
import com.fiuni.distri.project.fiuni.dto.RoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.main.service.base.IBaseService;
import project.main.specifications.RoleSpecification;
import project.main.specifications.base.DeleteFilterOption;
import project.main.utils.response.SuccessResponseDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RoleService implements IBaseService<RoleDto, Role> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoleRepo roleRepo;

    public Page<RoleDto> getAllFilter(String rol, DeleteFilterOption deleteFilterOption, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Role> specification = Specification
                .where(RoleSpecification.hasRole(rol))
                .and(RoleSpecification.applyDeleteFilter(deleteFilterOption));

        Page<Role> rolePage = roleRepo.findAll(specification, pageable);
        return rolePage.map(role -> modelMapper.map(role, RoleDto.class));
    }

    @Override
    public Role dtoToEntity(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

    @Override
    public RoleDto entityToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role role = dtoToEntity(roleDto);
        roleRepo.save(role);
        return entityToDto(role);
    }

    @Override
    public RoleDto softDeleteById(int id) {
        Optional<Role> r = roleRepo.findById(id);
        if(r.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Rol no encontrado");
        Role roleEntity = r.get();
        roleEntity.setDeletedAt(LocalDateTime.now());
        roleRepo.save(roleEntity);
        return entityToDto(roleEntity);
    }

    @Override
    public RoleDto updateById(int id, RoleDto roleDto) {
        Optional<Role> role = roleRepo.findById(id);
        if (role.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Rol no encontrado");
        Role roleEntity = role.get();
        if(roleDto.getRol() != null) roleEntity.setRol(roleDto.getRol());
        roleRepo.save(role.get());
        return entityToDto(role.get());
    }

    @Override
    public RoleDto getById(int id) {
        Optional<Role> role = roleRepo.findById(id);
        if(role.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Rol no encontrado");
        return entityToDto(role.get());
    }

    @Override
    public SuccessResponseDto deleteById(int id) {
        roleRepo.deleteById(id);
        return new SuccessResponseDto(200, "Registro Eliminado correctamente", null);
    }

    @Override
    public Page<RoleDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Role> roleDomains = this.roleRepo.findAll(pageRequest);
        return roleDomains.map(role -> modelMapper.map(role, RoleDto.class));
    }
}
