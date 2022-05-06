package com.example.tokenauthserver.rest;

import com.example.tokenauthserver.dto.AdminUserDto;
import com.example.tokenauthserver.model.User;
import com.example.tokenauthserver.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admins/")
public class AdminRestControllerV1 {

    private final UserService userService;

    @Autowired
    public AdminRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "Получить сведения об администраторе",
            notes = "Получить сведения об администраторе в соответствии с идентификатором")
    @ApiImplicitParam(name = "id", value = "Admin ID", required = true, dataType = "Long", paramType = "path")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        AdminUserDto result = AdminUserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
