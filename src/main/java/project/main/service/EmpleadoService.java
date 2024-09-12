package project.main.service;

import com.fiuni.distri.project.fiuni.domain.Empleado;
import com.fiuni.distri.project.fiuni.domain.Puesto;
import com.fiuni.distri.project.fiuni.domain.User;
import com.fiuni.distri.project.fiuni.dto.EmpleadoDto;
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
import project.main.repo.EmpleadoRepo;
import project.main.repo.PuestoRepo;
import project.main.repo.UserRepo;
import project.main.service.base.IBaseService;
import project.main.specifications.EmpleadoSpecification;
import project.main.utils.response.SuccessResponseDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService implements IBaseService<EmpleadoDto, Empleado> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmpleadoRepo empleadoRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PuestoRepo puestoRepo;

    @Override
    public Empleado dtoToEntity(EmpleadoDto empleadoDto) {
        return modelMapper.map(empleadoDto, Empleado.class);
    }

    @Override
    public EmpleadoDto entityToDto(Empleado empleado) {
        return modelMapper.map(empleado, EmpleadoDto.class);
    }


    @Override
    public EmpleadoDto create(EmpleadoDto empleadoDto) {
        Empleado empleadoEntity = dtoToEntity(empleadoDto);

        //Obtener el usuario y el puesto de los id del empleado
        Optional<User> user = userRepo.findById(empleadoDto.getUser_id());
        Optional<Puesto> puesto = puestoRepo.findById(empleadoDto.getPuesto_id());

        //Verificar su existencia en la db
        if(user.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
        if(puesto.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "No se ha encontrado el puesto");

        //Asignar el puesto y usuario al empleado
        empleadoEntity.setUser(user.get());
        empleadoEntity.setPuesto(puesto.get());

        empleadoRepo.save(empleadoEntity);
        return entityToDto(empleadoEntity);
    }

    @Override
    public EmpleadoDto updateById(int id, EmpleadoDto empleadoDto) {
        Optional<Empleado> empleadoOptional = empleadoRepo.findById(id);

        if(empleadoOptional.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Empleado no encontrado");

        Empleado empleadoEntity = empleadoOptional.get();

        if(empleadoDto.getPuesto_id() != 0) {
            Optional<Puesto> puesto = puestoRepo.findById(empleadoDto.getPuesto_id());
            if(puesto.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "No se ha encontrado el puesto");
            empleadoEntity.setPuesto(puesto.get());
        }

        if(empleadoDto.getUser_id() != 0) {
            Optional<User> user = userRepo.findById(empleadoDto.getUser_id());
            if(user.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
            empleadoEntity.setUser(user.get());
        }

        if(empleadoDto.getCi() != null) empleadoEntity.setCi(empleadoDto.getCi());
        if(empleadoDto.getNombre() != null) empleadoEntity.setNombre(empleadoDto.getNombre());

        empleadoRepo.save(empleadoEntity);
        return entityToDto(empleadoEntity);
    }

    @Override
    public EmpleadoDto getById(int id) {
        Optional<Empleado> empleadoEntity = empleadoRepo.findById(id);
        if(empleadoEntity.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        return entityToDto(empleadoEntity.get());
    }

    @Override
    public SuccessResponseDto deleteById(int id) {
        empleadoRepo.deleteById(id);
        return new SuccessResponseDto(200, "Registro Eliminado correctamente", null);
    }

    @Override
    public EmpleadoDto softDeleteById(int id) {
        Optional<Empleado> empleadoOptional = empleadoRepo.findById(id);
        if(empleadoOptional.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        Empleado empleadoEntity = empleadoOptional.get();
        empleadoEntity.setDeletedAt(LocalDateTime.now());
        empleadoRepo.save(empleadoEntity);
        return entityToDto(empleadoEntity);
    }

    @Override
    public Page<EmpleadoDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Empleado> empleadoPage = empleadoRepo.findAll(pageRequest);
        return empleadoPage.map(empleado -> entityToDto(empleado));
    }

    public Page<EmpleadoDto> getAllFilter(Integer userId, Integer puestoId, String nombre, String ci, int page, int size ){
        Pageable pageable = PageRequest.of(page, size);

        Specification<Empleado> specification = Specification
                .where(EmpleadoSpecification.hasUserId(userId))
                .and(EmpleadoSpecification.hasPuestoId(puestoId))
                .and(EmpleadoSpecification.hasNombre(nombre))
                .and(EmpleadoSpecification.hasCi(ci));

        Page<Empleado> empleadoPage = empleadoRepo.findAll(specification, pageable);
        return empleadoPage.map(empleado -> entityToDto(empleado));
    }

}
