package com.br.univille.Libranos.controler;

import com.br.univille.Libranos.dto.ModuloRequestDTO;
import com.br.univille.Libranos.dto.ModuloResponseDTO;
import com.br.univille.Libranos.service.ModuloService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @PostMapping
    public ResponseEntity<ModuloResponseDTO> criar(@Valid @RequestBody ModuloRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduloService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ModuloResponseDTO>> listar() {
        return ResponseEntity.ok(moduloService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(moduloService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuloResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ModuloRequestDTO dto) {
        return ResponseEntity.ok(moduloService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        moduloService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
