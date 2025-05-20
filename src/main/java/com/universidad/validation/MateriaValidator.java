package com.universidad.validation;

import com.universidad.dto.MateriaDTO;
import com.universidad.repository.MateriaRepository;
import org.springframework.stereotype.Component;

@Component
public class MateriaValidator {
    private final MateriaRepository materiaRepository;

    public MateriaValidator(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public void validarMateria(MateriaDTO materiaDTO) {
        if (materiaRepository.existsByCodigoUnico(materiaDTO.getCodigoUnico())) {
            throw new IllegalArgumentException("Ya existe una materia con este código único");
        }

        if (materiaDTO.getCreditos() <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser mayores a cero");
        }
    }
}