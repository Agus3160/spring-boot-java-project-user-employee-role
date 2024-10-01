package project.main.controller;

import com.fiuni.distri.project.fiuni.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import project.main.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import project.main.specifications.base.DeleteFilterOption;
import project.main.utils.response.SuccessResponseDto;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping({"", "/"})
    public Page<RoleDto> getAll(
            @RequestParam(name = "rol", required = false) String rol,
            @RequestParam(name = "delete_option", required = false, defaultValue = "NOT_DELETED") DeleteFilterOption deleteFilterOption,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return this.roleService.getAllFilter(rol, deleteFilterOption, page, size);
    }

    @GetMapping({"/{id}"})
    public RoleDto getById(@PathVariable(name = "id") int id) {
        return this.roleService.getById(id);
    }

    @PostMapping("")
    public RoleDto createUser(@RequestBody RoleDto v) {
        return this.roleService.create(v);
    }

    @PutMapping("/{id}")
    public RoleDto updateById(@PathVariable(name = "id") int id, @RequestBody RoleDto rDto) {
        return this.roleService.updateById(id, rDto);
    }
    @DeleteMapping("/soft/{id}")
    public RoleDto softDeleteById(@PathVariable(name = "id") int id) {
        return this.roleService.softDeleteById(id);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto deleteById(@PathVariable(name = "id") int id) {
        return this.roleService.deleteById(id);
    }

}
