package com.example.tokenauthserver.rest;

import com.example.tokenauthserver.dto.AuthenticationRequestDto;
import com.example.tokenauthserver.dto.RegisterUserDto;
import com.example.tokenauthserver.model.User;
import com.example.tokenauthserver.security.jwt.JwtTokenProvider;
import com.example.tokenauthserver.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager,
                                          JwtTokenProvider jwtTokenProvider,
                                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @ApiOperation(value = "Авторизация", notes = "Возвращает username и token")
    @ApiImplicitParam(name = "requestDto", value = "Login fields", required = true,
            dataType = "AuthenticationRequestDto")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ApiOperation(value = "Проверка", notes = "Возвращает статус 200 если есть доступ")
    @GetMapping("check")
    public ResponseEntity check() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Регистрация", notes = "Возвращает username и token")
    @ApiImplicitParam(name = "requestDto", value = "Registration fields", required = true,
            dataType = "AuthenticationRequestDto")
    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterUserDto requestDto) {
        try {
            String username = requestDto.getUsername();
            User user = userService.findByUsername(username);
            if (user != null) {
                return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
            }
            user = userService.findByEmail(requestDto.getEmail());
            if (user != null) {
                return new ResponseEntity<>("User with the same email already exists", HttpStatus.CONFLICT);
            }

            user = userService.register(requestDto.toUser());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
