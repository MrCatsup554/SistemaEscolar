package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Personas {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty apellido;
    private final StringProperty sexo;
    private final StringProperty fechaNacimiento;
    private final StringProperty rol;

    public Personas(int id, String nombre, String apellido, String sexo, String fhNac, String rol) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.sexo = new SimpleStringProperty(sexo);
        this.fechaNacimiento = new SimpleStringProperty(fhNac);
        this.rol = new SimpleStringProperty(rol);
    }

    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getApellido() { return apellido.get(); }
    public String getSexo() { return sexo.get(); }
    public String getFechaNacimiento() { return fechaNacimiento.get(); }
    public String getRol() { return rol.get(); }
}