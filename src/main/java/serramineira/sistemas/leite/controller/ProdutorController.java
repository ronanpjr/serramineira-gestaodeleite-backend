package serramineira.sistemas.leite.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serramineira.sistemas.leite.dto.ProdutorRequestDto;
import serramineira.sistemas.leite.dto.ProdutorResponseDto;
import serramineira.sistemas.leite.service.ProdutorService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtores")
public class ProdutorController {

    @Autowired
    private ProdutorService produtorService;

    @GetMapping
    public ResponseEntity<List<ProdutorResponseDto>> listarTodos() {
        List<ProdutorResponseDto> produtores = produtorService.listarTodos();
        return ResponseEntity.ok(produtores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutorResponseDto> buscarPorId(@PathVariable Long id) {
        try {
            ProdutorResponseDto produtor = produtorService.buscarPorId(id);
            return ResponseEntity.ok(produtor);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProdutorResponseDto> criar(@RequestBody @Valid ProdutorRequestDto dto) {
        ProdutorResponseDto novoProdutor = produtorService.criar(dto);
        // Retorna o status 201 Created com a localização do novo recurso
        URI location = URI.create("/api/produtores/" + novoProdutor.id());
        return ResponseEntity.created(location).body(novoProdutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutorResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutorRequestDto dto) {
        try {
            ProdutorResponseDto produtorAtualizado = produtorService.atualizar(id, dto);
            return ResponseEntity.ok(produtorAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            produtorService.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}