package com.br.univille.Libranos.repositories;

import com.br.univille.Libranos.models.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {
    List<Atividade> findByAulaIdOrderByOrdem(Integer aulaId);
}
