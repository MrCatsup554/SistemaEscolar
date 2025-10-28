package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Materias {
    private final IntegerProperty idMateria;
    private final StringProperty descripcion;
    private final IntegerProperty semestre;
    private final IntegerProperty creditos;

    public Materias(int idMateria, String descripcion, int semestre, int creditos) {
        this.idMateria = new SimpleIntegerProperty(idMateria);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.semestre = new SimpleIntegerProperty(semestre);
        this.creditos = new SimpleIntegerProperty(creditos);
    }

    public int getIdMateria() { return idMateria.get(); }
    public String getDescripcion() { return descripcion.get(); }
    public int getSemestre() { return semestre.get(); }
    public int getCreditos() { return creditos.get(); }
}