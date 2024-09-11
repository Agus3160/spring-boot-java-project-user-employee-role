package project.main.service;

import com.fiuni.distri.project.fiuni.domain.Role;
import com.fiuni.distri.project.fiuni.dto.UserDto;
import com.fiuni.distri.project.fiuni.domain.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.main.exception.ApiException;
import project.main.repo.RoleRepo;
import project.main.repo.UserRepo;
import project.main.service.base.IBaseService;
import project.main.specifications.UserSpecification;
import project.main.utils.response.SuccessResponseDto;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Transactional
public class UserService implements IBaseService<UserDto, User> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    public Page<UserDto> getAllFilter(String username, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<User> specification = Specification
                .where(UserSpecification.hasUsername(username))
                .and(UserSpecification.hasEmail(email));

        Page<User> userPage = userRepo.findAll(specification, pageable);
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public UserDto entityToDto(User userDomain) {
        return modelMapper.map(userDomain, UserDto.class);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User u = dtoToEntity(userDto);
        Optional<Role> r = roleRepo.findById(userDto.getRol_id());
        if (r.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "El rol seleccionado para el usuario, no existe");
        u.setRole(r.get());
        userRepo.save(u);
        return entityToDto(u);
    }

    @Override
    public UserDto updateById(int id, UserDto userDto) {
        Optional<User> u = userRepo.findById(id);
        if (u.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        Optional<Role> r = roleRepo.findById(u.get().getId());
        if (r.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "El rol seleccionado para el usuario, no existe");
        User userEntity = u.get();

        if (userDto.getEmail() != null) userEntity.setEmail(userDto.getEmail());
        if (userDto.getUsername() != null) userEntity.setUsername(userDto.getUsername());

        userEntity.setRole(r.get());
        userRepo.save(userEntity);
        return entityToDto(userEntity);
    }

    @Override
    public UserDto getById(int id) {
        Optional<User> u = userRepo.findById(id);
        if (u.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        return entityToDto(u.get());
    }

    @Override
    public SuccessResponseDto deleteById(int id) {
        userRepo.deleteById(id);
        return new SuccessResponseDto(200, "Registro eliminado correctamente", null);
    }

    @Override
    public UserDto softDeleteById(int id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        User userEntity = user.get();
        userEntity.setDeletedAt(LocalDateTime.now());
        userRepo.save(userEntity);
        return entityToDto(userEntity);
    }

    @Override
    public Page<UserDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> usersDomain = this.userRepo.findAll(pageRequest);
        return usersDomain.map(user -> entityToDto(user));
    }
}
