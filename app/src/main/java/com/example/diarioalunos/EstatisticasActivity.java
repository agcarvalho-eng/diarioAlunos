package com.example.diarioalunos;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diarioalunos.models.Estudante;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EstatisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        String alunosJson = getIntent().getStringExtra("TODOS_ALUNOS");
        Type type = new TypeToken<List<Estudante>>() {}.getType();
        List<Estudante> alunos = new Gson().fromJson(alunosJson, type);

        calcularEstatisticas(alunos);
    }

    private void calcularEstatisticas(List<Estudante> alunos) {
        TextView textViewMediaTurma = findViewById(R.id.textViewMediaTurma);
        TextView textViewAlunoMaiorNota = findViewById(R.id.textViewAlunoMaiorNota);
        TextView textViewAlunoMenorNota = findViewById(R.id.textViewAlunoMenorNota);
        TextView textViewMediaIdade = findViewById(R.id.textViewMediaIdade);
        TextView textViewAprovados = findViewById(R.id.textViewAprovados);
        TextView textViewReprovados = findViewById(R.id.textViewReprovados);

        // Cálculos
        double somaMedias = 0;
        double somaIdades = 0;
        double maiorNota = -1;
        double menorNota = 11;
        String nomeMaiorNota = "";
        String nomeMenorNota = "";
        List<String> aprovados = new ArrayList<>();
        List<String> reprovados = new ArrayList<>();

        for (Estudante aluno : alunos) {
            double media = calcularMedia(aluno.getNotas());
            double presenca = calcularPercentualPresenca(aluno.getPresenca());
            String situacao = verificarSituacao(media, presenca);

            somaMedias += media;
            somaIdades += aluno.getIdade();

            if (media > maiorNota) {
                maiorNota = media;
                nomeMaiorNota = aluno.getNome();
            }
            if (media < menorNota) {
                menorNota = media;
                nomeMenorNota = aluno.getNome();
            }

            if (situacao.equals("Aprovado")) {
                aprovados.add(aluno.getNome());
            } else {
                reprovados.add(aluno.getNome());
            }
        }

        // Exibição dos resultados
        textViewMediaTurma.setText(String.format("Média geral da turma: %.2f", somaMedias / alunos.size()));
        textViewAlunoMaiorNota.setText(String.format("Aluno com maior nota: %s (%.2f)", nomeMaiorNota, maiorNota));
        textViewAlunoMenorNota.setText(String.format("Aluno com menor nota: %s (%.2f)", nomeMenorNota, menorNota));
        textViewMediaIdade.setText(String.format("Média de idade: %.1f anos", somaIdades / alunos.size()));
        textViewAprovados.setText("Aprovados: " + String.join(", ", aprovados));
        textViewReprovados.setText("Reprovados: " + String.join(", ", reprovados));
    }

    // Métodos auxiliares (iguais aos da DetalhesActivity)
    private double calcularMedia(List<Double> notas) {
        if (notas == null || notas.isEmpty()) return 0;
        double soma = 0;
        for (Double nota : notas) {
            soma += nota;
        }
        return soma / notas.size();
    }

    private double calcularPercentualPresenca(List<Boolean> presencas) {
        if (presencas == null || presencas.isEmpty()) return 0;
        int presentes = 0;
        for (Boolean presente : presencas) {
            if (presente) presentes++;
        }
        return (presentes * 100.0) / presencas.size();
    }

    private String verificarSituacao(double media, double presenca) {
        return (media >= 7 && presenca >= 75) ? "Aprovado" : "Reprovado";
    }
}
