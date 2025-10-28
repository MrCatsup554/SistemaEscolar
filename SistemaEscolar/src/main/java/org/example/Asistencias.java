package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Asistencias {

    private final IntegerProperty idAsistencia;
    private final IntegerProperty idInscripcion;
    private final StringProperty fecha;

    public Asistencias(int idAsistencia, int idInscripcion, String fecha) {
        this.idAsistencia = new SimpleIntegerProperty(idAsistencia);
        this.idInscripcion = new SimpleIntegerProperty(idInscripcion);
        this.fecha = new SimpleStringProperty(fecha);
    }

    public int getIdAsistencia() { return idAsistencia.get(); }
    public int getIdInscripcion() { return idInscripcion.get(); }
    public String getFecha() { return fecha.get(); }
}