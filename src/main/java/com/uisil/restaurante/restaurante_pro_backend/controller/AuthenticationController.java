package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosLogin;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosRegistroUsuario;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosToken;
import com.uisil.restaurante.restaurante_pro_backend.security.service.AuthenticationService;
import com.uisil.restaurante.restaurante_pro_backend.security.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticacion", description = "Login y registro de usuarios")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/login")
        @Operation(
            summary = "Iniciar sesion",
            description = "Valida credenciales y devuelve un token JWT para consumir endpoints protegidos."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
        })
    public ResponseEntity<DatosToken> login(@RequestBody @Valid DatosLogin datosLogin){
            DatosToken token = authenticationService.login(datosLogin);
            return ResponseEntity.ok(token);

    }

    @PostMapping("/register")
        @Operation(
            summary = "Registrar usuario",
            description = "Crea una nueva cuenta en el sistema."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
        })
    public ResponseEntity<Void> registrer(@RequestBody @Valid DatosRegistroUsuario request){
        authenticationService.registroUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
