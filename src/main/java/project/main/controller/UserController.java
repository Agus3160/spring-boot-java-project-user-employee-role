package project.main.controller;

import project.main.service.UserService;
import com.fiuni.distri.project.fiuni.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import project.main.utils.response.SuccessResponseDto;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping({"", "/"})
    public Page<UserDto> getAll(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return this.userService.getAllFilter(username, email, page, size);
    }

    @GetMapping({"/{id}"})
    public UserDto getById(@PathVariable(name = "id") int id) {
        return this.userService.getById(id);
    }

    @PostMapping("")
    public UserDto createUser(@RequestBody UserDto v) {
        return this.userService.create(v);
    }

    @PutMapping("/{id}")
    public UserDto updateById(@PathVariable(name = "id") int id, UserDto userDto) {
        return this.userService.updateById(id, userDto);
    }

    @DeleteMapping("/soft/{id}")
    public UserDto deleteSoftById(@PathVariable(name = "id") int id) {
        return this.userService.softDeleteById(id);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto deleteById(@PathVariable(name = "id") int id) {
        return this.userService.deleteById(id);
    }

}
