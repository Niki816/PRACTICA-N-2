package com.universidad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "docente") // Nombre de la tabla en la base de datos  
public class Docente extends Persona {
    @Column(name = "nro_empleado", nullable = false, unique = true) // Columna no nula y con valor único    
    private String nroEmpleado;

    @Column(name = "departamento", nullable = false) // Columna no nula
    private String departamento;

    @ManyToMany(mappedBy = "docentes")
    private Set<Materia> materias = new HashSet<>();

    /**
     * Lista de evaluaciones asociadas al docente.
     */
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionDocente> evaluaciones; // Lista de evaluaciones asociadas al docente

}
