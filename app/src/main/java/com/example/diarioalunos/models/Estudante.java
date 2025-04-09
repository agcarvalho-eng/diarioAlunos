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

    // Método para calcular a média das notas do aluno
    public double calcularMedia() {
        // Se a lista de notas for nula ou vazia, retorna 0
        if (notas == null || notas.isEmpty()) return 0;

        // Variável para somar as notas
        double soma = 0;

        // Percorre cada nota e adiciona ao total
        for (Double nota : notas) {
            soma += nota;
        }

        // Retorna a média das notas
        return soma / notas.size();
    }

    // Método para calcular o percentual de presença do aluno
    public double calcularPercentualPresenca() {
        // Se a lista de presenças for nula ou vazia, retorna 0
        if (presenca == null || presenca.isEmpty()) return 0;

        // Conta quantas presenças o aluno teve
        int presentes = 0;

        // Percorre a lista de presenças e conta quantos "true" (presença) existem
        for (Boolean presente : presenca) {
            if (presente) presentes++;
        }

        // Retorna o percentual de presença (número de presenças dividido pelo total de aulas)
        return (presentes * 100.0) / presenca.size();
    }

    // Método para verificar a situação do aluno (Aprovado ou Reprovado)
    public String verificarSituacao() {
        double media = calcularMedia();
        double presencaPercentual = calcularPercentualPresenca();
        // O aluno é aprovado se a média for maior ou igual a 7 e a presença for maior ou igual a 75%
        return (media >= 7 && presencaPercentual >= 75) ? "Aprovado" : "Reprovado";
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
