package com.br.univille.Libranos.controler;

import com.br.univille.Libranos.dto.AtividadeRequestDTO;
import com.br.univille.Libranos.dto.AtividadeResponseDTO;
import com.br.univille.Libranos.service.AtividadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @PostMapping
    public ResponseEntity<AtividadeResponseDTO> criar(@Valid @RequestBody AtividadeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atividadeService.criar(dto));
    }

    @GetMapping("/aula/{aulaId}")
    public ResponseEntity<List<AtividadeResponseDTO>> listarPorAula(@PathVariable Integer aulaId) {
        return ResponseEntity.ok(atividadeService.listarPorAula(aulaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtividadeResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AtividadeRequestDTO dto) {
        return ResponseEntity.ok(atividadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        atividadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
