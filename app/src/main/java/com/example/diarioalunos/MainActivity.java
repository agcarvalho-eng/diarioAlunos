package com.example.diarioalunos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diarioalunos.models.Diario;
import com.example.diarioalunos.models.Estudante;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView listViewAlunos;
    private final String URL = "https://my-json-server.typicode.com/agcarvalho-eng/DiarioAlunoTarefa2/db";
    private Diario diario;
    private ExecutorService executorService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa componentes
        listViewAlunos = findViewById(R.id.dadosID);
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        // Configura o tratamento das bordas da tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        // Configura o clique nos itens da lista
        listViewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (diario != null && diario.getEstudantes() != null && position < diario.getEstudantes().size()) {
                    Estudante alunoSelecionado = diario.getEstudantes().get(position);
                    abrirTelaDetalhes(alunoSelecionado);
                }
            }
        });

        // Inicia o download dos dados
        iniciarDownload();
    }

    private void abrirTelaDetalhes(Estudante aluno) {
        Intent intent = new Intent(this, DetalhesActivity.class);
        intent.putExtra("ALUNO", aluno);
        intent.putExtra("TODOS_ALUNOS", new Gson().toJson(diario.getEstudantes()));
        startActivity(intent);
    }

    private void iniciarDownload() {
        Toast.makeText(this, "Carregando dados dos alunos...", Toast.LENGTH_SHORT).show();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Conexao conexao = new Conexao();
                    InputStream inputStream = conexao.obterRespostaHTTP(URL);

                    if (inputStream == null) {
                        mostrarErro("Erro na conexão com o servidor");
                        return;
                    }

                    String textoJSON = conexao.converter(inputStream);
                    Log.d("JSON_RECEIVED", textoJSON);

                    if (textoJSON != null && !textoJSON.trim().isEmpty()) {
                        processarDadosJSON(textoJSON);
                    } else {
                        mostrarErro("Dados recebidos estão vazios");
                    }
                } catch (Exception e) {
                    Log.e("JSON_ERROR", "Erro ao processar JSON: " + e.getMessage());
                    mostrarErro("Erro ao carregar dados: " + e.getMessage());
                }
            }
        });
    }

    private void processarDadosJSON(String textoJSON) {
        Gson gson = new Gson();
        diario = gson.fromJson(textoJSON, Diario.class);

        final List<String> nomeEstudantes = new ArrayList<>();
        if (diario != null && diario.getEstudantes() != null) {
            for (Estudante estudante : diario.getEstudantes()) {
                nomeEstudantes.add(estudante.getNome());
            }
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    if (nomeEstudantes.isEmpty()) {
                        nomeEstudantes.add("Nenhum aluno cadastrado");
                        Toast.makeText(MainActivity.this,
                                "Nenhum aluno encontrado", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            nomeEstudantes
                    );
                    listViewAlunos.setAdapter(adapter);
                }
            }
        });
    }

    private void mostrarErro(final String mensagem) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

