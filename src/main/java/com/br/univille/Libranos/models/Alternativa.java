package com.br.univille.Libranos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "alternativas")
public class Alternativa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String texto;

    private String midiaUrl;

    @Column(nullable = false)
    private Boolean correta = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getMidiaUrl() { return midiaUrl; }
    public void setMidiaUrl(String midiaUrl) { this.midiaUrl = midiaUrl; }

    public Boolean getCorreta() { return correta; }
    public void setCorreta(Boolean correta) { this.correta = correta; }

    public Atividade getAtividade() { return atividade; }
    public void setAtividade(Atividade atividade) { this.atividade = atividade; }
}
