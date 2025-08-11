package serramineira.sistemas.leite.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serramineira.sistemas.leite.dto.ColetaDiariaDto;
import serramineira.sistemas.leite.dto.ColetaDiariaResponseDto;
import serramineira.sistemas.leite.dto.ColetaLoteDto;
import serramineira.sistemas.leite.model.ColetaDiaria;
import serramineira.sistemas.leite.service.ColetaDiariaService;

import java.util.List;

@RestController
@RequestMapping("/api/coletas")
public class ColetaDiariaController {

    @Autowired
    private ColetaDiariaService coletaService;

    @GetMapping
    public List<ColetaDiaria> listarTodas() {
        return coletaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColetaDiaria> buscarPorId(@PathVariable Long id) {
        try {
            ColetaDiaria coleta = coletaService.buscarPorId(id);
            return ResponseEntity.ok(coleta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid ColetaDiariaDto dto) {
        try {
            ColetaDiaria novaColeta = coletaService.criar(dto);
            return ResponseEntity.status(201).body(novaColeta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid ColetaDiariaDto dto) {
        try {
            ColetaDiaria coletaAtualizada = coletaService.atualizar(id, dto);
            return ResponseEntity.ok(coletaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            coletaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/produtor/{produtorId}/mes/{ano}/{mes}")
    public ResponseEntity<?> buscarColetasPorProdutorEMes(
            @PathVariable Long produtorId,
            @PathVariable int ano,
            @PathVariable int mes) {
        try {
            List<ColetaDiaria> coletas = coletaService.buscarPorProdutorEMes(produtorId, ano, mes);
            return ResponseEntity.ok(coletas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/lote")
    public ResponseEntity<?> salvarColetasEmLote(@RequestBody @Valid List<ColetaLoteDto> coletasDto) {
        try {
            List<ColetaDiariaResponseDto> coletasSalvas = coletaService.salvarColetasEmLote(coletasDto);
            return ResponseEntity.status(201).body(coletasSalvas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}