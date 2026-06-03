package com.br.univille.Libranos.repositories;

import com.br.univille.Libranos.models.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
    List<Aula> findByModuloIdOrderByOrdem(Integer moduloId);
}
