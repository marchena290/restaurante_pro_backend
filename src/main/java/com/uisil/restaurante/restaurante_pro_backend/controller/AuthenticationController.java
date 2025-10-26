package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosLogin;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosRegistroUsuario;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosToken;
import com.uisil.restaurante.restaurante_pro_backend.security.service.AuthenticationService;
import com.uisil.restaurante.restaurante_pro_backend.security.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DatosToken> login(@RequestBody @Valid DatosLogin datosLogin){
            DatosToken token = authenticationService.login(datosLogin);
            return ResponseEntity.ok(token);

    }

    @PostMapping("/register")
    public ResponseEntity<Void> registrer(@RequestBody @Valid DatosRegistroUsuario request){
        authenticationService.registroUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
