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

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Estudante aluno = (Estudante) getIntent().getSerializableExtra("ALUNO");
        String alunosJson = getIntent().getStringExtra("TODOS_ALUNOS");
        Type type = new TypeToken<List<Estudante>>() {}.getType();
        List<Estudante> todosAlunos = new Gson().fromJson(alunosJson, type);

        TextView textViewNome = findViewById(R.id.textViewNome);
        TextView textViewIdade = findViewById(R.id.textViewIdade);
        TextView textViewMedia = findViewById(R.id.textViewMedia);
        TextView textViewPresenca = findViewById(R.id.textViewPresenca);
        TextView textViewSituacao = findViewById(R.id.textViewSituacao);
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);

        textViewNome.setText(aluno.getNome());
        textViewIdade.setText("Idade: " + aluno.getIdade() + " anos");

        double media = calcularMedia(aluno.getNotas());
        textViewMedia.setText(String.format("Média: %.2f", media));

        double presencaPercentual = calcularPercentualPresenca(aluno.getPresenca());
        textViewPresenca.setText(String.format("Frequência: %.1f%%", presencaPercentual));

        String situacao = verificarSituacao(media, presencaPercentual);
        textViewSituacao.setText("Situação: " + situacao);

        btnEstatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalhesActivity.this, EstatisticasActivity.class);
                intent.putExtra("TODOS_ALUNOS", new Gson().toJson(todosAlunos));
                startActivity(intent);
            }
        });
    }

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
