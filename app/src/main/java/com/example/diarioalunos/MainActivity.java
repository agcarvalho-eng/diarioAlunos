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

// A classe MainActivity
public class MainActivity extends AppCompatActivity {

    // Declaração dos componentes usados na Activity
    private ListView listViewAlunos;
    private final String URL = "https://my-json-server.typicode.com/agcarvalho-eng/DiarioAlunoTarefa2/db"; // URL de onde os dados dos alunos serão obtidos
    private Diario diario;
    // Executor para rodar tarefas em background
    private ExecutorService executorService;
    // Handler para atualizar a UI na thread principal
    private Handler handler;

    // Método onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da atividade
        setContentView(R.layout.activity_main);

        // Inicializa o ListView onde os alunos serão exibidos
        listViewAlunos = findViewById(R.id.dadosID);
        // Inicializa o Executor para executar tarefas em segundo plano
        executorService = Executors.newSingleThreadExecutor();
        // Inicializa o Handler para atualizar a UI na thread principal
        handler = new Handler(Looper.getMainLooper());

        // Configura o comportamento das bordas da tela para adicionar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                // Obtém as bordas da tela
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Aplica padding nas bordas
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        // Configura o clique nos itens da lista
        listViewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Verifica se há alunos e se a posição clicada está dentro do tamanho da lista
                if (diario != null && diario.getEstudantes() != null && position < diario.getEstudantes().size()) {
                    // Obtém o aluno selecionado
                    Estudante alunoSelecionado = diario.getEstudantes().get(position);
                    // Abre a tela de detalhes passando o aluno selecionado
                    abrirTelaDetalhes(alunoSelecionado);
                }
            }
        });

        // Inicia o processo de download dos dados dos alunos
        iniciarDownload();
    }

    // Método para abrir a tela de detalhes do aluno
    private void abrirTelaDetalhes(Estudante aluno) {
        // Cria uma Intent para abrir a DetalhesActivity
        Intent intent = new Intent(this, DetalhesActivity.class);
        // Passa o aluno selecionado para a tela de detalhes
        intent.putExtra("ALUNO", aluno);
        // Passa todos os alunos em formato JSON
        intent.putExtra("TODOS_ALUNOS", new Gson().toJson(diario.getEstudantes()));
        // Inicia a atividade
        startActivity(intent);
    }

    // Método para iniciar o download dos dados dos alunos
    private void iniciarDownload() {
        Toast.makeText(this, "Carregando dados dos alunos...", Toast.LENGTH_SHORT).show();

        // Executa a tarefa de download em segundo plano
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Cria uma instância da classe Conexao para realizar a requisição HTTP
                    Conexao conexao = new Conexao();
                    // Obtém os dados da URL como InputStream
                    InputStream inputStream = conexao.obterRespostaHTTP(URL);

                    // Se não conseguiu obter os dados, exibe um erro
                    if (inputStream == null) {
                        mostrarErro("Erro na conexão com o servidor");
                        return;
                    }

                    // Converte o InputStream para uma string JSON
                    String textoJSON = conexao.converter(inputStream);
                    // Exibe o JSON no log para depuração
                    Log.d("JSON_RECEBIDO:", textoJSON);

                    // Se o JSON não estiver vazio, processa os dados
                    if (textoJSON != null && !textoJSON.trim().isEmpty()) {
                        processarDadosJSON(textoJSON);
                    } else {
                        mostrarErro("Dados recebidos estão vazios");
                    }
                } catch (Exception e) {
                    // Exibe erro no log
                    Log.e("JSON_ERROR", "Erro ao processar JSON: " + e.getMessage());
                    // Exibe um Toast com o erro
                    mostrarErro("Erro ao carregar dados: " + e.getMessage());
                }
            }
        });
    }

    // Método que processa o JSON recebido
    private void processarDadosJSON(String textoJSON) {
        // Cria uma instância do Gson para converter o JSON em objetos
        Gson gson = new Gson();
        // Converte o JSON em um objeto Diario
        diario = gson.fromJson(textoJSON, Diario.class);

        // Lista para armazenar os nomes dos alunos
        final List<String> nomeEstudantes = new ArrayList<>();
        if (diario != null && diario.getEstudantes() != null) {
            // Adiciona os nomes dos alunos à lista
            for (Estudante estudante : diario.getEstudantes()) {
                nomeEstudantes.add(estudante.getNome());
            }
        }

        // Atualiza a UI na thread principal
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Verifica se a Activity não está finalizada ou destruída
                if (!isFinishing() && !isDestroyed()) {
                    // Se não houver alunos, exibe uma mensagem e adiciona um item na lista
                    if (nomeEstudantes.isEmpty()) {
                        nomeEstudantes.add("Nenhum aluno cadastrado");
                        Toast.makeText(MainActivity.this,
                                "Nenhum aluno encontrado", Toast.LENGTH_SHORT).show();
                    }

                    // Cria um adapter para exibir os nomes dos alunos no ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            nomeEstudantes
                    );
                    // Configura o ListView com o adapter
                    listViewAlunos.setAdapter(adapter);
                }
            }
        });
    }

    // Método para exibir um erro na UI
    private void mostrarErro(final String mensagem) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Exibe um Toast com a mensagem de erro
                Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Método chamado quando a Activity é destruída
    @Override
    protected void onDestroy() {
        // Chama o método onDestroy da classe pai (AppCompatActivity)
        super.onDestroy();
        // Se o ExecutorService estiver ativo, ele é finalizado
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}


