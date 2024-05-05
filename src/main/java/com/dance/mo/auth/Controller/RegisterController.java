package com.dance.mo.auth.Controller;

import com.dance.mo.Exceptions.UserException;
import com.dance.mo.auth.DTO.RegisterRequest;
import com.dance.mo.auth.DTO.RegisterResponse;
import com.dance.mo.auth.Service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class RegisterController {
    private final RegistrationService service;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            service.register(registerRequest);
            registerResponse.setMessageResponse("User Created");
            registerResponse.setEmailResponse(registerRequest.getEmail());
            registerResponse.setRoleResponse(registerRequest.getRole().name());
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
        }catch (UserException e) {
            registerResponse.setMessageResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessageResponse("An error occurred while registering user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(registerResponse);
        }
    }
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam String token) {
        try {
            String confirmationMessage = service.confirm(token);
            return ResponseEntity.ok(confirmationMessage);
        }catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while confirming user.");
        }
    }


}
