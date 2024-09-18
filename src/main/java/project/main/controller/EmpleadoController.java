package project.main.controller;

import com.fiuni.distri.project.fiuni.dto.EmpleadoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import project.main.service.EmpleadoService;
import project.main.utils.response.SuccessResponseDto;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;

    @GetMapping({"", "/"})
    public Page<EmpleadoDto> getAll(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "puestoId", required = false) Integer puestoId,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "ci", required = false) String ci,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return this.empleadoService.getAllFilter(userId, puestoId, nombre, ci, page, size);
    }

    @GetMapping({"/{id}"})
    public EmpleadoDto getById(@PathVariable(name = "id") int id) {
        return this.empleadoService.getById(id);
    }

    @PostMapping("")
    public EmpleadoDto createUser(@RequestBody EmpleadoDto empleadoDto) {
        return this.empleadoService.create(empleadoDto);
    }

    @PutMapping("/{id}")
    public EmpleadoDto updateById(@PathVariable(name = "id") int id, @RequestBody EmpleadoDto empleadoDto) {
        return this.empleadoService.updateById(id, empleadoDto);
    }

    @DeleteMapping("/soft/{id}")
    public EmpleadoDto deleteSoftById(@PathVariable(name = "id") int id) {
        return this.empleadoService.softDeleteById(id);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto deleteById(@PathVariable(name = "id") int id) {
        return this.empleadoService.deleteById(id);
    }

}