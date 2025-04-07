package com.example.diarioalunos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Diario {

    @SerializedName("estudantes")
    @Expose
    private List<Estudante> estudantes;

    /**
     * No args constructor for use in serialization
     *
     */
    public Diario() {
    }

    public Diario(List<Estudante> estudantes) {
        super();
        this.estudantes = estudantes;
    }

    public List<Estudante> getEstudantes() {
        return estudantes;
    }

    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    @Override
    public String toString() {
        return "Diario{" +
                "estudantes=" + estudantes +
                '}';
    }
}
