package com.br.univille.Libranos.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "progresso_aluno",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "aula_id"}))
public class ProgressoAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private User aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @CreationTimestamp
    private LocalDateTime concluidoEm;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getAluno() { return aluno; }
    public void setAluno(User aluno) { this.aluno = aluno; }

    public Aula getAula() { return aula; }
    public void setAula(Aula aula) { this.aula = aula; }

    public LocalDateTime getConcluidoEm() { return concluidoEm; }
    public void setConcluidoEm(LocalDateTime concluidoEm) { this.concluidoEm = concluidoEm; }
}
