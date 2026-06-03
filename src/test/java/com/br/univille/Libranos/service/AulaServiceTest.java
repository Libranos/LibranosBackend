package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.AulaRequestDTO;
import com.br.univille.Libranos.dto.AulaResponseDTO;
import com.br.univille.Libranos.exception.AulaNotFoundException;
import com.br.univille.Libranos.exception.ModuloNotFoundException;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.repositories.AulaRepository;
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
class AulaServiceTest {

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private AulaService aulaService;

    private Modulo modulo;
    private Aula aulaSalva;

    @BeforeEach
    void setUp() {
        modulo = new Modulo();
        modulo.setId(1);
        modulo.setTitulo("Módulo 1");
        modulo.setOrdem(1);
        modulo.setAtivo(true);

        aulaSalva = new Aula();
        aulaSalva.setId(10);
        aulaSalva.setTitulo("Aula 1");
        aulaSalva.setDescricao("Letra A em Libras");
        aulaSalva.setOrdem(1);
        aulaSalva.setAtivo(true);
        aulaSalva.setModulo(modulo);
        aulaSalva.setCreatedAt(LocalDateTime.now());
    }

    // --- criar ---

    @Test
    @DisplayName("criar: dados válidos com módulo existente → retorna DTO")
    void criar_dadosValidos_retornaDTO() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.save(any(Aula.class))).thenReturn(aulaSalva);
        var dto = new AulaRequestDTO("Aula 1", "Letra A em Libras", 1, 1);

        AulaResponseDTO result = aulaService.criar(dto);

        assertThat(result.id()).isEqualTo(10);
        assertThat(result.titulo()).isEqualTo("Aula 1");
        assertThat(result.moduloId()).isEqualTo(1);
        assertThat(result.moduloTitulo()).isEqualTo("Módulo 1");
        verify(aulaRepository).save(any(Aula.class));
    }

    @Test
    @DisplayName("criar: título em branco → IllegalArgumentException")
    void criar_tituloEmBranco_lancaException() {
        var dto = new AulaRequestDTO("", "desc", 1, 1);
        assertThatThrownBy(() -> aulaService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Título");
        verifyNoInteractions(aulaRepository);
    }

    @Test
    @DisplayName("criar: módulo inexistente → ModuloNotFoundException")
    void criar_moduloInexistente_lancaModuloNotFoundException() {
        when(moduloRepository.findById(99)).thenReturn(Optional.empty());
        var dto = new AulaRequestDTO("Aula 1", "desc", 1, 99);

        assertThatThrownBy(() -> aulaService.criar(dto))
                .isInstanceOf(ModuloNotFoundException.class)
                .hasMessageContaining("99");
        verifyNoInteractions(aulaRepository);
    }

    @Test
    @DisplayName("criar: ordem nula → IllegalArgumentException")
    void criar_ordemNula_lancaException() {
        var dto = new AulaRequestDTO("Aula 1", "desc", null, 1);
        assertThatThrownBy(() -> aulaService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(aulaRepository);
    }

    // --- listarPorModulo ---

    @Test
    @DisplayName("listarPorModulo: módulo existente → retorna lista ordenada")
    void listarPorModulo_moduloExistente_retornaLista() {
        when(moduloRepository.existsById(1)).thenReturn(true);
        when(aulaRepository.findByModuloIdOrderByOrdem(1)).thenReturn(List.of(aulaSalva));

        List<AulaResponseDTO> result = aulaService.listarPorModulo(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).titulo()).isEqualTo("Aula 1");
    }

    @Test
    @DisplayName("listarPorModulo: módulo inexistente → ModuloNotFoundException")
    void listarPorModulo_moduloInexistente_lancaException() {
        when(moduloRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> aulaService.listarPorModulo(99))
                .isInstanceOf(ModuloNotFoundException.class);
        verifyNoInteractions(aulaRepository);
    }

    // --- buscarPorId ---

    @Test
    @DisplayName("buscarPorId: id existente → retorna DTO")
    void buscarPorId_idExistente_retornaDTO() {
        when(aulaRepository.findById(10)).thenReturn(Optional.of(aulaSalva));

        AulaResponseDTO result = aulaService.buscarPorId(10);

        assertThat(result.id()).isEqualTo(10);
    }

    @Test
    @DisplayName("buscarPorId: id inexistente → AulaNotFoundException")
    void buscarPorId_idInexistente_lancaException() {
        when(aulaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aulaService.buscarPorId(99))
                .isInstanceOf(AulaNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- atualizar ---

    @Test
    @DisplayName("atualizar: dados válidos → retorna DTO atualizado")
    void atualizar_dadosValidos_retornaDTOAtualizado() {
        when(aulaRepository.findById(10)).thenReturn(Optional.of(aulaSalva));
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.save(any(Aula.class))).thenAnswer(inv -> inv.getArgument(0));
        var dto = new AulaRequestDTO("Aula Atualizada", "nova desc", 2, 1);

        AulaResponseDTO result = aulaService.atualizar(10, dto);

        assertThat(result.titulo()).isEqualTo("Aula Atualizada");
        assertThat(result.ordem()).isEqualTo(2);
    }

    @Test
    @DisplayName("atualizar: aula inexistente → AulaNotFoundException")
    void atualizar_aulaInexistente_lancaException() {
        when(aulaRepository.findById(99)).thenReturn(Optional.empty());
        var dto = new AulaRequestDTO("Título", "desc", 1, 1);

        assertThatThrownBy(() -> aulaService.atualizar(99, dto))
                .isInstanceOf(AulaNotFoundException.class);
    }

    @Test
    @DisplayName("atualizar: módulo inexistente → ModuloNotFoundException")
    void atualizar_moduloInexistente_lancaException() {
        when(aulaRepository.findById(10)).thenReturn(Optional.of(aulaSalva));
        when(moduloRepository.findById(99)).thenReturn(Optional.empty());
        var dto = new AulaRequestDTO("Título", "desc", 1, 99);

        assertThatThrownBy(() -> aulaService.atualizar(10, dto))
                .isInstanceOf(ModuloNotFoundException.class);
    }

    // --- deletar ---

    @Test
    @DisplayName("deletar: id existente → chama repository.delete")
    void deletar_idExistente_deletaAula() {
        when(aulaRepository.findById(10)).thenReturn(Optional.of(aulaSalva));

        aulaService.deletar(10);

        verify(aulaRepository).delete(aulaSalva);
    }

    @Test
    @DisplayName("deletar: id inexistente → AulaNotFoundException")
    void deletar_idInexistente_lancaException() {
        when(aulaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aulaService.deletar(99))
                .isInstanceOf(AulaNotFoundException.class);
        verify(aulaRepository, never()).delete(any());
    }
}
