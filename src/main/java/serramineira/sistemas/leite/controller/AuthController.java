package serramineira.sistemas.leite.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serramineira.sistemas.leite.dto.LoginDto;
import serramineira.sistemas.leite.dto.RegistroUsuarioDto;
import serramineira.sistemas.leite.dto.TokenDto;
import serramineira.sistemas.leite.model.Usuario;
import serramineira.sistemas.leite.repository.UsuarioRepository;
import serramineira.sistemas.leite.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistroUsuarioDto dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            return ResponseEntity.badRequest().build(); // ou lançar uma exceção específica
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        Usuario novoUsuario = new Usuario(null, dto.email(), dto.nome(), senhaCriptografada);
        usuarioRepository.save(novoUsuario);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        String tokenJwt = tokenService.gerarToken(usuarioAutenticado);

        return ResponseEntity.ok(new TokenDto(tokenJwt));
    }
}