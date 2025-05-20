package com.universidad.controller;

import com.universidad.dto.*;
import com.universidad.service.IMateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "API para gestión de materias y asignación a docentes")
@SecurityRequirement(name = "bearerAuth")
public class MateriaController {

    private final IMateriaService materiaService;

    @Autowired
    public MateriaController(IMateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @Operation(summary = "Obtener todas las materias")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
        return ResponseEntity.ok(materiaService.obtenerTodasLasMaterias());
    }

    @Operation(summary = "Obtener materia por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materiaService.obtenerMateriaPorId(id));
    }

    @Operation(summary = "Crear nueva materia")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MateriaDTO> crearMateria(@RequestBody MateriaDTO materia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crearMateria(materia));
    }

    @Operation(summary = "Actualizar materia (Requiere rol ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MateriaDTO> actualizarMateria(
            @PathVariable Long id,
            @Valid @RequestBody MateriaDTO materiaDTO) {
        MateriaDTO actualizada = materiaService.actualizarMateria(id, materiaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar materia")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar docente a materia")
    @PostMapping("/asignar-docente")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @CacheEvict(value = {"materias", "docentes"}, allEntries = true)
    public ResponseEntity<String> asignarDocenteAMateria(@RequestBody AsignacionDocenteDTO asignacion) {
        materiaService.asignarDocenteAMateria(asignacion);
        return ResponseEntity.ok("Docente asignado a materia exitosamente");
    }

    @Operation(summary = "Obtener materias por docente")
    @GetMapping("/docente/{docenteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<MateriaDTO>> obtenerMateriasPorDocente(@PathVariable Long docenteId) {
        return ResponseEntity.ok(materiaService.obtenerMateriasPorDocente(docenteId));
    }

    @Operation(summary = "Verificar si se formaría un círculo de prerequisitos")
    @GetMapping("/formaria-circulo/{materiaId}/{prerequisitoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> formariaCirculo(@PathVariable Long materiaId, @PathVariable Long prerequisitoId) {
        return ResponseEntity.ok(materiaService.formariaCirculo(materiaId, prerequisitoId));
    }
}