package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public class Inscripciones {

    private final IntegerProperty idInscripcion;
    private final IntegerProperty idMateria;
    private final IntegerProperty idEstudiante;
    private final IntegerProperty calificacion;

    public Inscripciones(int idInscripcion, int idMateria, int idEstudiante, int calificacion) {
        this.idInscripcion = new SimpleIntegerProperty(idInscripcion);
        this.idMateria = new SimpleIntegerProperty(idMateria);
        this.idEstudiante = new SimpleIntegerProperty(idEstudiante);
        this.calificacion = new SimpleIntegerProperty(calificacion);
    }

    public int getIdInscripcion() { return idInscripcion.get(); }
    public int getIdMateria() { return idMateria.get(); }
    public int getIdEstudiante() { return idEstudiante.get(); }
    public int getCalificacion() { return calificacion.get(); }
}