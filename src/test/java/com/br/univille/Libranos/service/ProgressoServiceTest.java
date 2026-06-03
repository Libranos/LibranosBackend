package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.ProgressoModuloResponseDTO;
import com.br.univille.Libranos.dto.ProgressoResponseDTO;
import com.br.univille.Libranos.exception.ProgressoDuplicadoException;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.models.ProgressoAluno;
import com.br.univille.Libranos.models.User;
import com.br.univille.Libranos.repositories.AulaRepository;
import com.br.univille.Libranos.repositories.ModuloRepository;
import com.br.univille.Libranos.repositories.ProgressoAlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressoServiceTest {

    @Mock
    private ProgressoAlunoRepository progressoRepository;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ProgressoService progressoService;

    private User aluno;
    private Aula aula;
    private Modulo modulo;

    @BeforeEach
    void setUp() {
        modulo = new Modulo();
        modulo.setId(1);
        modulo.setTitulo("Módulo 1");

        aula = new Aula();
        aula.setId(1);
        aula.setTitulo("Aula 1");
        aula.setModulo(modulo);

        aluno = new User();
        aluno.setId(1);
    }

    // ── Regra 3: não pode concluir a mesma aula duas vezes ───────────────────────

    @Test
    void concluirAula_deveLancarProgressoDuplicadoException_quandoAulaJaConcluida() {
        when(aulaRepository.findById(1)).thenReturn(Optional.of(aula));
        when(progressoRepository.existsByAlunoIdAndAulaId(1, 1)).thenReturn(true);

        assertThrows(ProgressoDuplicadoException.class,
                () -> progressoService.concluirAula(1, aluno));

        verify(progressoRepository, never()).save(any());
    }

    // ── Regra 3: happy path ───────────────────────────────────────────────────────

    @Test
    void concluirAula_deveSalvarProgresso_quandoAulaNaoConcluida() {
        when(aulaRepository.findById(1)).thenReturn(Optional.of(aula));
        when(progressoRepository.existsByAlunoIdAndAulaId(1, 1)).thenReturn(false);

        ProgressoAluno saved = new ProgressoAluno();
        saved.setId(1);
        saved.setAluno(aluno);
        saved.setAula(aula);
        saved.setConcluidoEm(LocalDateTime.now());
        when(progressoRepository.save(any(ProgressoAluno.class))).thenReturn(saved);

        ProgressoResponseDTO result = progressoService.concluirAula(1, aluno);

        assertNotNull(result);
        assertEquals(1, result.aulaId());
        assertEquals(1, result.moduloId());
        verify(progressoRepository).save(any(ProgressoAluno.class));
    }

    // ── Regra 4: cálculo de percentual correto ───────────────────────────────────

    @Test
    void calcularProgressoModulo_deveRetornarPercentualCorreto() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.countByModuloId(1)).thenReturn(4L);
        when(progressoRepository.countByAlunoIdAndAulaModuloId(1, 1)).thenReturn(2L);

        ProgressoModuloResponseDTO result = progressoService.calcularProgressoModulo(1, aluno);

        assertEquals(50.0, result.percentualConclusao());
        assertEquals(4L, result.totalAulas());
        assertEquals(2L, result.aulasConcluidadas());
    }

    // ── Regra 4: nenhuma aula concluída → 0% ─────────────────────────────────────

    @Test
    void calcularProgressoModulo_deveRetornarZero_quandoNenhumaAulaConcluida() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.countByModuloId(1)).thenReturn(3L);
        when(progressoRepository.countByAlunoIdAndAulaModuloId(1, 1)).thenReturn(0L);

        ProgressoModuloResponseDTO result = progressoService.calcularProgressoModulo(1, aluno);

        assertEquals(0.0, result.percentualConclusao());
    }

    // ── Regra 4: módulo sem aulas → 0% (sem divisão por zero) ───────────────────

    @Test
    void calcularProgressoModulo_deveRetornarZero_quandoModuloSemAulas() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.countByModuloId(1)).thenReturn(0L);
        when(progressoRepository.countByAlunoIdAndAulaModuloId(1, 1)).thenReturn(0L);

        ProgressoModuloResponseDTO result = progressoService.calcularProgressoModulo(1, aluno);

        assertEquals(0.0, result.percentualConclusao());
    }

    // ── Regra 4: 100% quando todas as aulas concluídas ──────────────────────────

    @Test
    void calcularProgressoModulo_deveRetornarCem_quandoTodasAulasConcluidas() {
        when(moduloRepository.findById(1)).thenReturn(Optional.of(modulo));
        when(aulaRepository.countByModuloId(1)).thenReturn(5L);
        when(progressoRepository.countByAlunoIdAndAulaModuloId(1, 1)).thenReturn(5L);

        ProgressoModuloResponseDTO result = progressoService.calcularProgressoModulo(1, aluno);

        assertEquals(100.0, result.percentualConclusao());
    }
}
