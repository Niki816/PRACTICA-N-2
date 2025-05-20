package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "API para gestión de inscripciones de estudiantes")
@SecurityRequirement(name = "bearerAuth")
public class InscripcionController {

    private final IInscripcionService inscripcionService;

    @Autowired
    public InscripcionController(IInscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @Operation(summary = "Crear nueva inscripción")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ESTUDIANTE')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InscripcionDTO> crearInscripcion(@RequestBody InscripcionDTO inscripcionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.crearInscripcion(inscripcionDTO));
    }

    @Operation(summary = "Obtener inscripción por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<?> obtenerInscripcionPorId(@PathVariable Long id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        if (inscripcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcion);
    }

    @Operation(summary = "Obtener inscripciones por estudiante")
    @GetMapping("/estudiante/{estudianteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(
            @PathVariable Long estudianteId) {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId));
    }

    @Operation(summary = "Obtener inscripciones por estudiante y período")
    @GetMapping("/estudiante/{estudianteId}/periodo/{periodo}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudianteYPeriodo(
            @PathVariable Long estudianteId,
            @PathVariable String periodo) {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPorEstudianteYPeriodo(estudianteId, periodo));
    }

    @Operation(summary = "Actualizar estado de inscripción")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(
            @PathVariable Long id,
            @RequestBody InscripcionDTO inscripcionDTO) {
        return ResponseEntity.ok(inscripcionService.actualizarInscripcion(id, inscripcionDTO));
    }

    @Operation(summary = "Eliminar inscripción")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
    //Mira ahora necesito hacer un
    //Gestor de Actividades Extracurriculares, Desccripcion: Aplicacion para
}