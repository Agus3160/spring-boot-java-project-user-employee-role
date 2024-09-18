package project.main.config;

import com.fiuni.distri.project.fiuni.domain.Empleado;
import com.fiuni.distri.project.fiuni.domain.User;
import com.fiuni.distri.project.fiuni.dto.EmpleadoDto;
import com.fiuni.distri.project.fiuni.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map(source.getRole().getId(), destination.getRol_id());
            }
        });

        modelMapper.addMappings(new PropertyMap<Empleado, EmpleadoDto>() {
            @Override
            protected void configure() {
                map(source.getPuesto().getId(), destination.getPuesto_id());
                map(source.getUser().getId(), destination.getUser_id());
            }
        });

        return modelMapper;
    }
}
