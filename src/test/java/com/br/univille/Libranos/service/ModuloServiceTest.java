package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.ModuloRequestDTO;
import com.br.univille.Libranos.dto.ModuloResponseDTO;
import com.br.univille.Libranos.exception.ModuloNotFoundException;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.repositories.ModuloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuloServiceTest {

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ModuloService moduloService;

    private Modulo moduloSalvo;

    @BeforeEach
    void setUp() {
        moduloSalvo = new Modulo();
        moduloSalvo.setId(1);
        moduloSalvo.setTitulo("Módulo 1");
        moduloSalvo.setDescricao("Introdução ao alfabeto");
        moduloSalvo.setOrdem(1);
        moduloSalvo.setAtivo(true);
        moduloSalvo.setCreatedAt(LocalDateTime.now());
    }

    // --- criar ---

    @Test
    @DisplayName("criar: dados válidos → retorna DTO com id")
    void criar_dadosValidos_retornaDTO() {
        when(moduloRepository.save(any(Modulo.class))).thenReturn(moduloSalvo);
        var dto = new ModuloRequestDTO("Módulo 1", "Introdução ao alfabeto", 1);

        ModuloResponseDTO response = moduloService.criar(dto);

        assertThat(response.id()).isEqualTo(1);
        assertThat(response.titulo()).isEqualTo("Módulo 1");
        assertThat(response.ativo()).isTrue();
        verify(moduloRepository).save(any(Modulo.class));
    }

    @Test
    @DisplayName("criar: título nulo → IllegalArgumentException")
    void criar_tituloNulo_lancaException() {
        var dto = new ModuloRequestDTO(null, "desc", 1);
        assertThatThrownBy(() -> moduloService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Título");
        verifyNoInteractions(moduloRepository);
    }

    @Test
    @DisplayName("criar: título em branco → IllegalArgumentException")
    void criar_tituloEmBranco_lancaException() {
        var dto = new ModuloRequestDTO("  ", "desc", 1);
        assertThatThrownBy(() -> moduloService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(moduloRepository);
    }

    @Test
    @DisplayName("criar: ordem nula → IllegalArgumentException")
    void criar_ordemNula_lancaException() {
        var dto = new ModuloRequestDTO("Módulo 1", "desc", null);
        assertThatThrownBy(() -> moduloService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rdem");
        verifyNoInteractions(moduloRepository);
    }

    @Test
    @DisplayName("criar: ordem zero → IllegalArgumentException")
    void criar_ordemZero_lancaException() {
        var dto = new ModuloRequestDTO("Módulo 1", "desc", 0);
        assertThatThrownBy(() -> moduloService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(moduloRepository);
    }

    // --- listar ---

    @Test
    @DisplayName("listar: retorna todos os módulos mapeados")
    void listar_retornaTodosOsModulos() {
        when(moduloRepository.findAll()).thenReturn(List.of(moduloSalvo));

        List<ModuloResponseDTO> result = moduloService.listar();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).titulo()).isEqualTo("Módulo 1");
    }

    // --- buscarPorId ---

    @Test
    @DisplayName("buscarPorId: id existente → retorna DTO")
    void buscarPorId_idExistente_retornaDTO() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(moduloSalvo));

        ModuloResponseDTO result = moduloService.buscarPorId(1);

        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId: id inexistente → ModuloNotFoundException")
    void buscarPorId_idInexistente_lancaException() {
        when(moduloRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moduloService.buscarPorId(99))
                .isInstanceOf(ModuloNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- atualizar ---

    @Test
    @DisplayName("atualizar: dados válidos + id existente → retorna DTO atualizado")
    void atualizar_dadosValidos_retornaDTOAtualizado() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(moduloSalvo));
        when(moduloRepository.save(any(Modulo.class))).thenAnswer(inv -> inv.getArgument(0));
        var dto = new ModuloRequestDTO("Módulo Atualizado", "nova desc", 2);

        ModuloResponseDTO result = moduloService.atualizar(1, dto);

        assertThat(result.titulo()).isEqualTo("Módulo Atualizado");
        assertThat(result.ordem()).isEqualTo(2);
    }

    @Test
    @DisplayName("atualizar: id inexistente → ModuloNotFoundException")
    void atualizar_idInexistente_lancaException() {
        when(moduloRepository.findById(99)).thenReturn(Optional.empty());
        var dto = new ModuloRequestDTO("Título", "desc", 1);

        assertThatThrownBy(() -> moduloService.atualizar(99, dto))
                .isInstanceOf(ModuloNotFoundException.class);
    }

    // --- deletar ---

    @Test
    @DisplayName("deletar: id existente → chama repository.delete")
    void deletar_idExistente_deletaModulo() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(moduloSalvo));

        moduloService.deletar(1);

        verify(moduloRepository).delete(moduloSalvo);
    }

    @Test
    @DisplayName("deletar: id inexistente → ModuloNotFoundException")
    void deletar_idInexistente_lancaException() {
        when(moduloRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moduloService.deletar(99))
                .isInstanceOf(ModuloNotFoundException.class);
        verify(moduloRepository, never()).delete(any());
    }
}
