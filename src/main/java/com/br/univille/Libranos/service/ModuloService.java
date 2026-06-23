package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.ModuloRequestDTO;
import com.br.univille.Libranos.dto.ModuloResponseDTO;
import com.br.univille.Libranos.exception.ModuloNotFoundException;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.repositories.ModuloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;

    public ModuloService(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    public ModuloResponseDTO criar(ModuloRequestDTO dto) {
        validar(dto);
        Modulo modulo = new Modulo();
        modulo.setTitulo(dto.titulo());
        modulo.setDescricao(dto.descricao());
        modulo.setOrdem(dto.ordem());
        modulo.setAtivo(true);
        return toDTO(moduloRepository.save(modulo));
    }

    public List<ModuloResponseDTO> listar() {
        return moduloRepository.findAll().stream().map(this::toDTO).toList();
    }

    public ModuloResponseDTO buscarPorId(Integer id) {
        return toDTO(findOrThrow(id));
    }

    public ModuloResponseDTO atualizar(Integer id, ModuloRequestDTO dto) {
        validar(dto);
        Modulo modulo = findOrThrow(id);
        modulo.setTitulo(dto.titulo());
        modulo.setDescricao(dto.descricao());
        modulo.setOrdem(dto.ordem());
        return toDTO(moduloRepository.save(modulo));
    }

    public void deletar(Integer id) {
        Modulo modulo = findOrThrow(id);
        moduloRepository.delete(modulo);
    }

    Modulo findOrThrow(Integer id) {
        return moduloRepository.findById(id)
                .orElseThrow(() -> new ModuloNotFoundException(id));
    }

    private void validar(ModuloRequestDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (dto.ordem() == null || dto.ordem() <= 0) {
            throw new IllegalArgumentException("Ordem deve ser um número positivo");
        }
    }

    private ModuloResponseDTO toDTO(Modulo m) {
        return new ModuloResponseDTO(m.getId(), m.getTitulo(), m.getDescricao(),
                m.getOrdem(), m.getAtivo(), m.getCreatedAt());
    }
}
