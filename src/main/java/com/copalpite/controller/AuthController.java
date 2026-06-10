package com.copalpite.controller;

import com.copalpite.config.JwtService;
import com.copalpite.dto.LoginDTO;
import com.copalpite.dto.LoginRespostaDTO;
import com.copalpite.entity.Usuario;
import com.copalpite.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDTO> login(@RequestBody LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getUsername());

        LoginRespostaDTO resposta = new LoginRespostaDTO();
        resposta.setToken(token);
        resposta.setId(usuario.getId());
        resposta.setUsername(usuario.getUsername());
        resposta.setNome(usuario.getNome());

        return ResponseEntity.ok(resposta);
    }
}