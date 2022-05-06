package com.example.tokenauthserver.rest;

import com.example.tokenauthserver.dto.UserDto;
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
@RequestMapping(value = "/api/v1/users/")
public class UserRestControllerV1 {
    private final UserService userService;

    @Autowired
    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "Получить сведения о пользователе",
            notes = "Получить сведения о пользователе в соответствии с идентификатором")
    @ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "Long", paramType = "path")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
