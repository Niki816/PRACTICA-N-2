package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;
import com.universidad.validation.InscripcionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements IInscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;
    private final InscripcionValidator inscripcionValidator;

    @Autowired
    public InscripcionServiceImpl(InscripcionRepository inscripcionRepository,
                                  EstudianteRepository estudianteRepository,
                                  MateriaRepository materiaRepository,
                                  InscripcionValidator inscripcionValidator) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
        this.inscripcionValidator = inscripcionValidator;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"inscripciones", "inscripcionesEstudiante"}, allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        inscripcionValidator.validarInscripcion(inscripcionDTO);

        Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));

        Inscripcion inscripcion = Inscripcion.builder()
                .estudiante(estudiante)
                .materia(materia)
                .fechaInscripcion(inscripcionDTO.getFechaInscripcion())
                .periodoAcademico(inscripcionDTO.getPeriodoAcademico())
                .grupo(inscripcionDTO.getGrupo())
                .estado("ACTIVA")
                .build();

        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(savedInscripcion);
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "inscripcionesEstudiante", key = "#estudianteId")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcionesEstudiantePeriodo", key = "{#estudianteId, #periodo}")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudianteYPeriodo(Long estudianteId, String periodo) {
        return inscripcionRepository.findByEstudianteAndPeriodo(estudianteId, periodo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"inscripcion", "inscripciones", "inscripcionesEstudiante"}, allEntries = true)
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));

        inscripcion.setEstado(inscripcionDTO.getEstado());
        inscripcion.setGrupo(inscripcionDTO.getGrupo());

        Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(updatedInscripcion);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"inscripcion", "inscripciones", "inscripcionesEstudiante"}, allEntries = true)
    public void eliminarInscripcion(Long id) {
        inscripcionRepository.deleteById(id);
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .estudianteId(inscripcion.getEstudiante().getId())
                .materiaId(inscripcion.getMateria().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .periodoAcademico(inscripcion.getPeriodoAcademico())
                .grupo(inscripcion.getGrupo())
                .estado(inscripcion.getEstado())
                .build();
    }
}