package com.br.univille.Libranos.repositories;

import com.br.univille.Libranos.models.ProgressoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressoAlunoRepository extends JpaRepository<ProgressoAluno, Integer> {
    boolean existsByAlunoIdAndAulaId(Integer alunoId, Integer aulaId);
    long countByAlunoIdAndAulaModuloId(Integer alunoId, Integer moduloId);
}
