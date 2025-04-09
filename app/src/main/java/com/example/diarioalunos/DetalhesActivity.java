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
        double media = aluno.calcularMedia();
        textViewMedia.setText(String.format("Média: %.2f", media));

        // Calcula o percentual de presença do aluno e exibe no TextView
        double presencaPercentual = aluno.calcularPercentualPresenca();
        textViewPresenca.setText(String.format("Frequência: %.1f%%", presencaPercentual));

        // Verifica a situação do aluno e exibe no TextView (Aprovado ou Reprovado)
        String situacao = aluno.verificarSituacao();
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
}

