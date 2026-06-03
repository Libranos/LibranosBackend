package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.AlternativaRequestDTO;
import com.br.univille.Libranos.dto.AlternativaResponseDTO;
import com.br.univille.Libranos.dto.AtividadeRequestDTO;
import com.br.univille.Libranos.dto.AtividadeResponseDTO;
import com.br.univille.Libranos.exception.AtividadeNotFoundException;
import com.br.univille.Libranos.exception.AulaNotFoundException;
import com.br.univille.Libranos.exception.DomainException;
import com.br.univille.Libranos.models.Alternativa;
import com.br.univille.Libranos.models.Atividade;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.repositories.AtividadeRepository;
import com.br.univille.Libranos.repositories.AulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final AulaRepository aulaRepository;

    public AtividadeService(AtividadeRepository atividadeRepository, AulaRepository aulaRepository) {
        this.atividadeRepository = atividadeRepository;
        this.aulaRepository = aulaRepository;
    }

    @Transactional
    public AtividadeResponseDTO criar(AtividadeRequestDTO dto) {
        validarAlternativas(dto.alternativas());
        Aula aula = findAulaOrThrow(dto.aulaId());
        Atividade atividade = new Atividade();
        preencherAtividade(atividade, dto, aula);
        return toDTO(atividadeRepository.save(atividade));
    }

    public List<AtividadeResponseDTO> listarPorAula(Integer aulaId) {
        if (!aulaRepository.existsById(aulaId)) throw new AulaNotFoundException(aulaId);
        return atividadeRepository.findByAulaIdOrderByOrdem(aulaId).stream().map(this::toDTO).toList();
    }

    public AtividadeResponseDTO buscarPorId(Integer id) {
        return toDTO(findOrThrow(id));
    }

    @Transactional
    public AtividadeResponseDTO atualizar(Integer id, AtividadeRequestDTO dto) {
        validarAlternativas(dto.alternativas());
        Atividade atividade = findOrThrow(id);
        Aula aula = findAulaOrThrow(dto.aulaId());
        atividade.getAlternativas().clear();
        preencherAtividade(atividade, dto, aula);
        return toDTO(atividadeRepository.save(atividade));
    }

    @Transactional
    public void deletar(Integer id) {
        atividadeRepository.delete(findOrThrow(id));
    }

    // ── Regras de negócio ────────────────────────────────────────────────────────

    private void validarAlternativas(List<AlternativaRequestDTO> alternativas) {
        if (alternativas == null || alternativas.size() < 2 || alternativas.size() > 5) {
            throw new DomainException("Uma atividade deve ter entre 2 e 5 alternativas.");
        }
        long corretas = alternativas.stream().filter(AlternativaRequestDTO::correta).count();
        if (corretas != 1) {
            throw new DomainException("Exatamente uma alternativa deve ser marcada como correta.");
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void preencherAtividade(Atividade atividade, AtividadeRequestDTO dto, Aula aula) {
        atividade.setTitulo(dto.titulo());
        atividade.setDescricao(dto.descricao());
        atividade.setMidiaUrl(dto.midiaUrl());
        atividade.setOrdem(dto.ordem());
        atividade.setAtivo(true);
        atividade.setAula(aula);

        dto.alternativas().forEach(ar -> {
            Alternativa alt = new Alternativa();
            alt.setTexto(ar.texto());
            alt.setMidiaUrl(ar.midiaUrl());
            alt.setCorreta(ar.correta());
            alt.setAtividade(atividade);
            atividade.getAlternativas().add(alt);
        });
    }

    private Atividade findOrThrow(Integer id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new AtividadeNotFoundException(id));
    }

    private Aula findAulaOrThrow(Integer aulaId) {
        return aulaRepository.findById(aulaId)
                .orElseThrow(() -> new AulaNotFoundException(aulaId));
    }

    private AtividadeResponseDTO toDTO(Atividade a) {
        List<AlternativaResponseDTO> alts = a.getAlternativas().stream()
                .map(alt -> new AlternativaResponseDTO(alt.getId(), alt.getTexto(), alt.getMidiaUrl(), alt.getCorreta()))
                .toList();
        return new AtividadeResponseDTO(
                a.getId(), a.getTitulo(), a.getDescricao(), a.getMidiaUrl(),
                a.getOrdem(), a.getAtivo(),
                a.getAula().getId(), a.getAula().getTitulo(),
                alts, a.getCreatedAt()
        );
    }
}
