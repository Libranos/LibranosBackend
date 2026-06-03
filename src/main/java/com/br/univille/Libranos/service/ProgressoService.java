package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.ProgressoModuloResponseDTO;
import com.br.univille.Libranos.dto.ProgressoResponseDTO;
import com.br.univille.Libranos.exception.AulaNotFoundException;
import com.br.univille.Libranos.exception.ModuloNotFoundException;
import com.br.univille.Libranos.exception.ProgressoDuplicadoException;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.models.ProgressoAluno;
import com.br.univille.Libranos.models.User;
import com.br.univille.Libranos.repositories.AulaRepository;
import com.br.univille.Libranos.repositories.ModuloRepository;
import com.br.univille.Libranos.repositories.ProgressoAlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressoService {

    private final ProgressoAlunoRepository progressoRepository;
    private final AulaRepository aulaRepository;
    private final ModuloRepository moduloRepository;

    public ProgressoService(ProgressoAlunoRepository progressoRepository,
                            AulaRepository aulaRepository,
                            ModuloRepository moduloRepository) {
        this.progressoRepository = progressoRepository;
        this.aulaRepository = aulaRepository;
        this.moduloRepository = moduloRepository;
    }

    // ── Regra 3: marcar aula como concluída (sem duplicidade) ────────────────────

    @Transactional
    public ProgressoResponseDTO concluirAula(Integer aulaId, User aluno) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new AulaNotFoundException(aulaId));

        if (progressoRepository.existsByAlunoIdAndAulaId(aluno.getId(), aulaId)) {
            throw new ProgressoDuplicadoException(aulaId);
        }

        ProgressoAluno progresso = new ProgressoAluno();
        progresso.setAluno(aluno);
        progresso.setAula(aula);

        return toDTO(progressoRepository.save(progresso));
    }

    // ── Regra 4: percentual de conclusão do módulo ───────────────────────────────

    public ProgressoModuloResponseDTO calcularProgressoModulo(Integer moduloId, User aluno) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new ModuloNotFoundException(moduloId));

        long totalAulas = aulaRepository.countByModuloId(moduloId);
        long aulasConcluidadas = progressoRepository.countByAlunoIdAndAulaModuloId(aluno.getId(), moduloId);

        double percentual = totalAulas == 0 ? 0.0 : (double) aulasConcluidadas / totalAulas * 100;

        return new ProgressoModuloResponseDTO(
                moduloId, modulo.getTitulo(), totalAulas, aulasConcluidadas, percentual
        );
    }

    private ProgressoResponseDTO toDTO(ProgressoAluno p) {
        return new ProgressoResponseDTO(
                p.getId(),
                p.getAula().getId(),
                p.getAula().getTitulo(),
                p.getAula().getModulo().getId(),
                p.getConcluidoEm()
        );
    }
}
