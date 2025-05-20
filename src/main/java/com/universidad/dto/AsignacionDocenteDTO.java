package com.universidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDocenteDTO {
    @NotNull(message = "El ID del docente es obligatorio")
    private Long docenteId;

    @NotNull(message = "El ID de la materia es obligatorio")
    private Long materiaId;

    private String periodoAcademico;
    private String grupo;
}