package com.example.diarioalunos.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Estudante implements Serializable {

    // Nome da chave Json
    @SerializedName("nome")
    @Expose // Este campo deve ser incluído no documento
    private String nome;
    // Nome da chave Json
    @SerializedName("idade")
    @Expose // Este campo deve ser incluído no documento
    private int idade;
    @SerializedName("notas")
    // Nome da chave Json
    @Expose // Este campo deve ser incluído no documento
    private List<Double> notas;
    // Nome da chave Json
    @SerializedName("presenca")
    @Expose // Este campo deve ser incluído no documento
    private List<Boolean> presenca;

    // Construtores
    public Estudante() {
    }

    public Estudante(String nome, int idade, List<Double> notas, List<Boolean> presenca) {
        super();
        this.nome = nome;
        this.idade = idade;
        this.notas = notas;
        this.presenca = presenca;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Double> getNotas() {
        return notas;
    }

    public void setNotas(List<Double> notas) {
        this.notas = notas;
    }

    public List<Boolean> getPresenca() {
        return presenca;
    }

    public void setPresenca(List<Boolean> presenca) {
        this.presenca = presenca;
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", notas=" + notas +
                ", presenca=" + presenca +
                '}';
    }
}
