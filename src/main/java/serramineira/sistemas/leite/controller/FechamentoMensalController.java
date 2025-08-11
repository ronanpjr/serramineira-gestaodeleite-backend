package serramineira.sistemas.leite.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serramineira.sistemas.leite.dto.CriarFechamentoDto;
import serramineira.sistemas.leite.dto.AtualizarFechamentoDto;
import serramineira.sistemas.leite.dto.FechamentoMensalResponseDto; // Importar
import serramineira.sistemas.leite.service.FechamentoMensalService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fechamentos")
public class FechamentoMensalController {

    @Autowired
    private FechamentoMensalService fechamentoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid CriarFechamentoDto dto) {
        try {
            FechamentoMensalResponseDto novoFechamento = fechamentoService.criarFechamento(dto);
            URI location = URI.create("/api/fechamentos/" + novoFechamento.id());
            return ResponseEntity.created(location).body(novoFechamento);
        } catch (EntityNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/pagamento")
    public ResponseEntity<?> atualizarPagamento(@PathVariable Long id, @RequestBody @Valid AtualizarFechamentoDto dto) {
        try {
            FechamentoMensalResponseDto fechamentoAtualizado = fechamentoService.atualizarPagamento(id, dto);
            return ResponseEntity.ok(fechamentoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FechamentoMensalResponseDto>> listarTodos() {
        List<FechamentoMensalResponseDto> fechamentos = fechamentoService.listarTodos();
        return ResponseEntity.ok(fechamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FechamentoMensalResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            FechamentoMensalResponseDto fechamento = fechamentoService.buscarPorId(id);
            return ResponseEntity.ok(fechamento);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            fechamentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}