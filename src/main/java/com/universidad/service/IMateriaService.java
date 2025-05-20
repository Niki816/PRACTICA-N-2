package com.universidad.service;

import com.universidad.dto.AsignacionDocenteDTO;
import com.universidad.dto.MateriaDTO;
import java.util.List;

public interface IMateriaService {
    List<MateriaDTO> obtenerTodasLasMaterias();
    MateriaDTO obtenerMateriaPorId(Long id);
    MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico);
    MateriaDTO crearMateria(MateriaDTO materia);
    MateriaDTO actualizarMateria(Long id, MateriaDTO materia);
    void eliminarMateria(Long id);
    void asignarDocenteAMateria(AsignacionDocenteDTO asignacion);
    List<MateriaDTO> obtenerMateriasPorDocente(Long docenteId);
    boolean formariaCirculo(Long materiaId, Long prerequisitoId);
}