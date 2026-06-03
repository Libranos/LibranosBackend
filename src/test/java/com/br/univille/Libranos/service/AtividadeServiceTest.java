package com.br.univille.Libranos.service;

import com.br.univille.Libranos.dto.AlternativaRequestDTO;
import com.br.univille.Libranos.dto.AtividadeRequestDTO;
import com.br.univille.Libranos.dto.AtividadeResponseDTO;
import com.br.univille.Libranos.exception.DomainException;
import com.br.univille.Libranos.models.Alternativa;
import com.br.univille.Libranos.models.Aula;
import com.br.univille.Libranos.models.Atividade;
import com.br.univille.Libranos.models.Modulo;
import com.br.univille.Libranos.repositories.AtividadeRepository;
import com.br.univille.Libranos.repositories.AulaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtividadeServiceTest {

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private AulaRepository aulaRepository;

    @InjectMocks
    private AtividadeService atividadeService;

    private Aula aula;

    @BeforeEach
    void setUp() {
        Modulo modulo = new Modulo();
        modulo.setId(1);
        modulo.setTitulo("Módulo 1");

        aula = new Aula();
        aula.setId(1);
        aula.setTitulo("Aula 1");
        aula.setModulo(modulo);
    }

    // ── Regra 1: mínimo de alternativas ─────────────────────────────────────────

    @Test
    void criar_deveLancarDomainException_quandoMenosDeDuasAlternativas() {
        var dto = buildDto(List.of(
                new AlternativaRequestDTO("A", null, true)
        ));
        assertThrows(DomainException.class, () -> atividadeService.criar(dto));
        verifyNoInteractions(atividadeRepository);
    }

    // ── Regra 1: máximo de alternativas ─────────────────────────────────────────

    @Test
    void criar_deveLancarDomainException_quandoMaisDeCincoAlternativas() {
        var dto = buildDto(List.of(
                new AlternativaRequestDTO("A", null, true),
                new AlternativaRequestDTO("B", null, false),
                new AlternativaRequestDTO("C", null, false),
                new AlternativaRequestDTO("D", null, false),
                new AlternativaRequestDTO("E", null, false),
                new AlternativaRequestDTO("F", null, false)
        ));
        assertThrows(DomainException.class, () -> atividadeService.criar(dto));
        verifyNoInteractions(atividadeRepository);
    }

    // ── Regra 2: nenhuma alternativa correta ─────────────────────────────────────

    @Test
    void criar_deveLancarDomainException_quandoNenhumaAlternativaCorreta() {
        var dto = buildDto(List.of(
                new AlternativaRequestDTO("A", null, false),
                new AlternativaRequestDTO("B", null, false)
        ));
        assertThrows(DomainException.class, () -> atividadeService.criar(dto));
        verifyNoInteractions(atividadeRepository);
    }

    // ── Regra 2: mais de uma alternativa correta ─────────────────────────────────

    @Test
    void criar_deveLancarDomainException_quandoMaisDeUmaAlternativaCorreta() {
        var dto = buildDto(List.of(
                new AlternativaRequestDTO("A", null, true),
                new AlternativaRequestDTO("B", null, true),
                new AlternativaRequestDTO("C", null, false)
        ));
        assertThrows(DomainException.class, () -> atividadeService.criar(dto));
        verifyNoInteractions(atividadeRepository);
    }

    // ── Regras 1 e 2 satisfeitas: deve salvar com sucesso ───────────────────────

    @Test
    void criar_deveSalvarComSucesso_quandoDadosValidos() {
        var dto = buildDto(alternativasValidas());

        Atividade saved = buildAtividade(dto);
        when(aulaRepository.findById(1)).thenReturn(Optional.of(aula));
        when(atividadeRepository.save(any(Atividade.class))).thenReturn(saved);

        AtividadeResponseDTO result = atividadeService.criar(dto);

        assertNotNull(result);
        assertEquals(dto.titulo(), result.titulo());
        assertEquals(3, result.alternativas().size());
        verify(atividadeRepository).save(any(Atividade.class));
    }

    @Test
    void atualizar_deveLancarDomainException_quandoAlternativasInvalidas() {
        var dto = buildDto(List.of(
                new AlternativaRequestDTO("A", null, true),
                new AlternativaRequestDTO("B", null, true)
        ));
        assertThrows(DomainException.class, () -> atividadeService.atualizar(1, dto));
        verifyNoInteractions(atividadeRepository);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private List<AlternativaRequestDTO> alternativasValidas() {
        return List.of(
                new AlternativaRequestDTO("Opção A", null, true),
                new AlternativaRequestDTO("Opção B", null, false),
                new AlternativaRequestDTO("Opção C", null, false)
        );
    }

    private AtividadeRequestDTO buildDto(List<AlternativaRequestDTO> alternativas) {
        return new AtividadeRequestDTO("Qual o sinal de 'Olá'?", "Descrição", null, 1, 1, alternativas);
    }

    private Atividade buildAtividade(AtividadeRequestDTO dto) {
        Atividade a = new Atividade();
        a.setId(1);
        a.setTitulo(dto.titulo());
        a.setDescricao(dto.descricao());
        a.setOrdem(dto.ordem());
        a.setAtivo(true);
        a.setAula(aula);

        List<Alternativa> alts = dto.alternativas().stream().map(ar -> {
            Alternativa alt = new Alternativa();
            alt.setId(1);
            alt.setTexto(ar.texto());
            alt.setMidiaUrl(ar.midiaUrl());
            alt.setCorreta(ar.correta());
            alt.setAtividade(a);
            return alt;
        }).toList();
        a.setAlternativas(new ArrayList<>(alts));
        return a;
    }
}
