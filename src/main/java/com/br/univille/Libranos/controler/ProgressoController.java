package com.br.univille.Libranos.controler;

import com.br.univille.Libranos.dto.ProgressoModuloResponseDTO;
import com.br.univille.Libranos.dto.ProgressoResponseDTO;
import com.br.univille.Libranos.models.User;
import com.br.univille.Libranos.service.ProgressoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progresso")
public class ProgressoController {

    private final ProgressoService progressoService;

    public ProgressoController(ProgressoService progressoService) {
        this.progressoService = progressoService;
    }

    // POST /api/progresso/aulas/{aulaId}/concluir
    @PostMapping("/aulas/{aulaId}/concluir")
    public ResponseEntity<ProgressoResponseDTO> concluirAula(
            @PathVariable Integer aulaId,
            @AuthenticationPrincipal User aluno) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressoService.concluirAula(aulaId, aluno));
    }

    // GET /api/progresso/modulos/{moduloId}
    @GetMapping("/modulos/{moduloId}")
    public ResponseEntity<ProgressoModuloResponseDTO> calcularProgressoModulo(
            @PathVariable Integer moduloId,
            @AuthenticationPrincipal User aluno) {
        return ResponseEntity.ok(progressoService.calcularProgressoModulo(moduloId, aluno));
    }
}
