package com.universidad.repository;

import com.universidad.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Materia findByCodigoUnico(String codigoUnico);

    @Query("SELECT m FROM Materia m JOIN m.docentes d WHERE d.id = :docenteId")
    List<Materia> findByDocentesId(@Param("docenteId") Long docenteId);

    boolean existsByCodigoUnico(java.lang.String codigoUnico);
}