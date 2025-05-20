package com.universidad.controller;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Materia;
import com.universidad.model.Estudiante;
import com.universidad.service.IEstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "Estudiantes", description = "API para gestión de estudiantes")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @Operation(summary = "Obtener todos los estudiantes",
            description = "Retorna una lista de todos los estudiantes registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN o DOCENTE")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> obtenerTodosLosEstudiantes() {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerTodosLosEstudiantes: {}", inicio);
        List<EstudianteDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerTodosLosEstudiantes: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiantes);
    }

    @Operation(summary = "Obtener estudiante por ID",
            description = "Retorna un estudiante específico según su ID único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorId(
            @Parameter(description = "ID del estudiante a buscar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantePorId(id));
    }

    @Operation(summary = "Obtener estudiante por número de inscripción",
            description = "Retorna un estudiante según su número de inscripción único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/inscripcion/{numeroInscripcion}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorNumeroInscripcion(
            @Parameter(description = "Número de inscripción del estudiante", required = true, example = "2023-001")
            @PathVariable String numeroInscripcion) {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerEstudiantePorNumeroInscripcion: {}", inicio);
        EstudianteDTO estudiante = estudianteService.obtenerEstudiantePorNumeroInscripcion(numeroInscripcion);
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerEstudiantePorNumeroInscripcion: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiante);
    }

    @Operation(summary = "Obtener materias de un estudiante",
            description = "Retorna la lista de materias en las que está inscrito un estudiante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias obtenida"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}/materias")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<Materia>> obtenerMateriasDeEstudiante(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable("id") Long estudianteId) {
        List<Materia> materias = estudianteService.obtenerMateriasDeEstudiante(estudianteId);
        return ResponseEntity.ok(materias);
    }

    @Operation(summary = "Obtener estudiantes activos",
            description = "Retorna una lista de todos los estudiantes con estado 'activo'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes activos obtenida"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudianteActivo() {
        List<EstudianteDTO> estudiantesActivos = estudianteService.obtenerEstudianteActivo();
        return ResponseEntity.ok(estudiantesActivos);
    }

    @Operation(summary = "Crear nuevo estudiante",
            description = "Registra un nuevo estudiante en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN")
    })
    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EstudianteDTO> crearEstudiante(
            @Parameter(description = "Datos del estudiante a crear", required = true)
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO nuevoEstudiante = estudianteService.crearEstudiante(estudianteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstudiante);
    }

    @Operation(summary = "Actualizar estudiante",
            description = "Actualiza los datos de un estudiante existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN")
    })
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(
            @Parameter(description = "ID del estudiante a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del estudiante", required = true)
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteActualizado);
    }

    @Operation(summary = "Dar de baja a un estudiante",
            description = "Realiza una baja lógica cambiando el estado del estudiante a 'inactivo'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN")
    })
    @PutMapping("/{id}/baja")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> eliminarEstudiante(
            @Parameter(description = "ID del estudiante a dar de baja", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de baja del estudiante", required = true)
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteEliminado = estudianteService.eliminarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteEliminado);
    }

    @Operation(summary = "Obtener estudiante con bloqueo",
            description = "Obtiene un estudiante con bloqueo pesimista para operaciones concurrentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante obtenido con bloqueo"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN")
    })
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estudiante> getEstudianteConBloqueo(
            @Parameter(description = "ID del estudiante a bloquear", required = true, example = "1")
            @PathVariable Long id) {
        Estudiante estudiante = estudianteService.obtenerEstudianteConBloqueo(id);
        return ResponseEntity.ok(estudiante);
    }

    @Operation(
            summary = "Eliminar físicamente un estudiante",
            description = "Elimina permanentemente un estudiante de la base de datos. ¡Esta acción no se puede deshacer!",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estudiante eliminado permanentemente"),
                    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
                    @ApiResponse(responseCode = "403", description = "Prohibido, requiere rol ADMIN"),
                    @ApiResponse(responseCode = "409", description = "Conflicto, el estudiante tiene materias asignadas")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarFisicamente(
            @Parameter(description = "ID del estudiante a eliminar permanentemente", required = true, example = "1")
            @PathVariable Long id) {
        estudianteService.eliminarFisicamente(id);
        return ResponseEntity.noContent().build();
    }
}