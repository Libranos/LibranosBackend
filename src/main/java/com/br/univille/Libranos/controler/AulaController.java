package com.br.univille.Libranos.controler;

import com.br.univille.Libranos.dto.AulaRequestDTO;
import com.br.univille.Libranos.dto.AulaResponseDTO;
import com.br.univille.Libranos.service.AulaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @PostMapping
    public ResponseEntity<AulaResponseDTO> criar(@Valid @RequestBody AulaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaService.criar(dto));
    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<AulaResponseDTO>> listarPorModulo(@PathVariable Integer moduloId) {
        return ResponseEntity.ok(aulaService.listarPorModulo(moduloId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(aulaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AulaRequestDTO dto) {
        return ResponseEntity.ok(aulaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        aulaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
