package com.example.diarioalunos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diarioalunos.models.Estudante;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

// Classe DetalhesActivity
public class DetalhesActivity extends AppCompatActivity {

    // Método onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da atividade
        setContentView(R.layout.activity_detalhes);

        // Obtém o objeto 'Estudante' passado pela Intent.
        Estudante aluno = (Estudante) getIntent().getSerializableExtra("ALUNO");

        // Obtém uma string JSON representando todos os alunos, passada pela Intent
        String alunosJson = getIntent().getStringExtra("TODOS_ALUNOS");

        // Define o tipo de lista de estudantes que será usada para deserializar o JSON
        Type type = new TypeToken<List<Estudante>>() {}.getType();

        // Converte a string JSON em uma lista de objetos Estudante
        List<Estudante> todosAlunos = new Gson().fromJson(alunosJson, type);

        // Inicializa as referências para os TextViews e o botão da interface
        TextView textViewNome = findViewById(R.id.textViewNome);
        TextView textViewIdade = findViewById(R.id.textViewIdade);
        TextView textViewMedia = findViewById(R.id.textViewMedia);
        TextView textViewPresenca = findViewById(R.id.textViewPresenca);
        TextView textViewSituacao = findViewById(R.id.textViewSituacao);
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);

        // Define o nome do aluno no TextView correspondente
        textViewNome.setText(aluno.getNome());

        // Exibe a idade do aluno no formato: "Idade: X anos"
        textViewIdade.setText("Idade: " + aluno.getIdade() + " anos");

        // Calcula a média das notas do aluno e exibe no TextView
        double media = calcularMedia(aluno.getNotas());
        textViewMedia.setText(String.format("Média: %.2f", media));

        // Calcula o percentual de presença do aluno e exibe no TextView
        double presencaPercentual = calcularPercentualPresenca(aluno.getPresenca());
        textViewPresenca.setText(String.format("Frequência: %.1f%%", presencaPercentual));

        // Verifica a situação do aluno e exibe no TextView (Aprovado ou Reprovado)
        String situacao = verificarSituacao(media, presencaPercentual);
        textViewSituacao.setText("Situação: " + situacao);

        // Define o que acontece quando o botão de estatísticas é clicado
        btnEstatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria uma nova Intent para abrir a activity EstatisticasActivity
                Intent intent = new Intent(DetalhesActivity.this, EstatisticasActivity.class);

                // Passa todos os alunos como uma string JSON para a nova activity
                intent.putExtra("TODOS_ALUNOS", new Gson().toJson(todosAlunos));

                // Inicia a activity EstatisticasActivity
                startActivity(intent);
            }
        });
    }

    // Método para calcular a média das notas do aluno
    private double calcularMedia(List<Double> notas) {
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
    private double calcularPercentualPresenca(List<Boolean> presencas) {
        // Se a lista de presenças for nula ou vazia, retorna 0
        if (presencas == null || presencas.isEmpty()) return 0;

        // Conta quantas presenças o aluno teve
        int presentes = 0;

        // Percorre a lista de presenças e conta quantos "true" (presença) existem
        for (Boolean presente : presencas) {
            if (presente) presentes++;
        }

        // Retorna o percentual de presença (número de presenças dividido pelo total de aulas)
        return (presentes * 100.0) / presencas.size();
    }

    // Método para verificar a situação do aluno (Aprovado ou Reprovado)
    private String verificarSituacao(double media, double presenca) {
        // O aluno é aprovado se a média for maior ou igual a 7 e a presença for maior ou igual a 75%
        return (media >= 7 && presenca >= 75) ? "Aprovado" : "Reprovado";
    }
}

