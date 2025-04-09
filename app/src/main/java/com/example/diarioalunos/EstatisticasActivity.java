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
    private Estudante estudante;

    // Método onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da atividade
        setContentView(R.layout.activity_estatisticas);

        // Recupera a string JSON representando todos os alunos passada pela Intent
        String alunosJson = getIntent().getStringExtra("TODOS_ALUNOS");

        // Define o tipo de lista
        Type type = new TypeToken<List<Estudante>>() {}.getType();

        // Converte a string JSON em uma lista de objetos Estudante
        List<Estudante> alunos = new Gson().fromJson(alunosJson, type);

        calcularEstatisticas(alunos);
    }

    // Método que realiza os cálculos das estatísticas da turma e exibe os resultados
    private void calcularEstatisticas(List<Estudante> alunos) {
        TextView textViewMediaTurma = findViewById(R.id.textViewMediaTurma);
        TextView textViewAlunoMaiorNota = findViewById(R.id.textViewAlunoMaiorNota);
        TextView textViewAlunoMenorNota = findViewById(R.id.textViewAlunoMenorNota);
        TextView textViewMediaIdade = findViewById(R.id.textViewMediaIdade);
        TextView textViewAprovados = findViewById(R.id.textViewAprovados);
        TextView textViewReprovados = findViewById(R.id.textViewReprovados);

        // Cria e inicializa as variáveis necessárias
        double somaMedias = 0;
        double somaIdades = 0;
        double maiorNota = -1;
        double menorNota = 11;
        String nomeMaiorNota = "";
        String nomeMenorNota = "";
        List<String> aprovados = new ArrayList<>();
        List<String> reprovados = new ArrayList<>();

        // Percorre todos os alunos para calcular as estatísticas
        for (Estudante aluno : alunos) {
            // Calcula a média e a presença do aluno
            double media = aluno.calcularMedia();
            double presenca = aluno.calcularPercentualPresenca();
            // Verifica a situação (Aprovado ou Reprovado) do aluno
            String situacao = aluno.verificarSituacao();

            // Atualiza as variáveis de soma de médias e idades
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
}
