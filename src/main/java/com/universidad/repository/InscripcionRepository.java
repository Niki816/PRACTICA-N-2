package com.universidad.repository;

import com.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);

    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.periodoAcademico = :periodo")
    List<Inscripcion> findByEstudianteAndPeriodo(@Param("estudianteId") Long estudianteId,
                                                 @Param("periodo") String periodo);

    boolean existsByEstudianteIdAndMateriaIdAndPeriodoAcademico(Long estudianteId, Long materiaId, String periodoAcademico);
}