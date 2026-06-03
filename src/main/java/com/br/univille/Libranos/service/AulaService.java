package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.AulaRequestDTO;
import com.br.univille.Libranos.dto.AulaResponseDTO;
import com.br.univille.Libranos.exception.AulaNotFoundException;
import com.br.univille.Libranos.exception.ModuloNotFoundException;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.repositories.AulaRepository;
import com.br.univille.Libranos.repositories.ModuloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final ModuloRepository moduloRepository;

    public AulaService(AulaRepository aulaRepository, ModuloRepository moduloRepository) {
        this.aulaRepository = aulaRepository;
        this.moduloRepository = moduloRepository;
    }

    public AulaResponseDTO criar(AulaRequestDTO dto) {
        validar(dto);
        Modulo modulo = findModuloOrThrow(dto.moduloId());
        Aula aula = new Aula();
        aula.setTitulo(dto.titulo());
        aula.setDescricao(dto.descricao());
        aula.setOrdem(dto.ordem());
        aula.setAtivo(true);
        aula.setModulo(modulo);
        return toDTO(aulaRepository.save(aula));
    }

    public List<AulaResponseDTO> listarPorModulo(Integer moduloId) {
        if (!moduloRepository.existsById(moduloId)) {
            throw new ModuloNotFoundException(moduloId);
        }
        return aulaRepository.findByModuloIdOrderByOrdem(moduloId).stream().map(this::toDTO).toList();
    }

    public AulaResponseDTO buscarPorId(Integer id) {
        return toDTO(findOrThrow(id));
    }

    public AulaResponseDTO atualizar(Integer id, AulaRequestDTO dto) {
        validar(dto);
        Aula aula = findOrThrow(id);
        Modulo modulo = findModuloOrThrow(dto.moduloId());
        aula.setTitulo(dto.titulo());
        aula.setDescricao(dto.descricao());
        aula.setOrdem(dto.ordem());
        aula.setModulo(modulo);
        return toDTO(aulaRepository.save(aula));
    }

    public void deletar(Integer id) {
        aulaRepository.delete(findOrThrow(id));
    }

    private Aula findOrThrow(Integer id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new AulaNotFoundException(id));
    }

    private Modulo findModuloOrThrow(Integer moduloId) {
        return moduloRepository.findById(moduloId)
                .orElseThrow(() -> new ModuloNotFoundException(moduloId));
    }

    private void validar(AulaRequestDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (dto.ordem() == null || dto.ordem() <= 0) {
            throw new IllegalArgumentException("Ordem deve ser um número positivo");
        }
    }

    private AulaResponseDTO toDTO(Aula a) {
        return new AulaResponseDTO(a.getId(), a.getTitulo(), a.getDescricao(),
                a.getOrdem(), a.getAtivo(),
                a.getModulo().getId(), a.getModulo().getTitulo(),
                a.getCreatedAt());
    }
}
