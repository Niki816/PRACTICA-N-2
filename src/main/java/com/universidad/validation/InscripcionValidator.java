package com.universidad.validation;

import com.universidad.dto.InscripcionDTO;
import com.universidad.repository.InscripcionRepository;
import org.springframework.stereotype.Component;

@Component
public class InscripcionValidator {
    private final InscripcionRepository inscripcionRepository;

    public InscripcionValidator(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    public void validarInscripcion(InscripcionDTO inscripcionDTO) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaIdAndPeriodoAcademico(
                inscripcionDTO.getEstudianteId(),
                inscripcionDTO.getMateriaId(),
                inscripcionDTO.getPeriodoAcademico())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta materia para el período académico");
        }

        if (inscripcionDTO.getFechaInscripcion().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inscripción no puede ser futura");
        }
    }
}