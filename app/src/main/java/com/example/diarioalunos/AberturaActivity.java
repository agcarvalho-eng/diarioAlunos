package com.example.diarioalunos;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class AberturaActivity extends AppCompatActivity {

    // Método onCreate (atividade é criada)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout para a atividade
        setContentView(R.layout.activity_abertura);

        // Obtém a referência para o LottieAnimationView
        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);

        // Configura a animação que será exibida
        animationView.setAnimation(R.raw.animacao);

        // Define a velocidade da animação
        animationView.setSpeed(2f);

        // Inicia a reprodução da animação
        animationView.playAnimation();

        // Adiciona um ouvinte de animação para responder a eventos da animação (início, fim, etc.)
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            // Método chamado quando a animação começa (não faz nada aqui)
            @Override
            public void onAnimationStart(Animator animation) {

            }
            // Método chamado quando a animação termina.
            @Override
            public void onAnimationEnd(Animator animation) {

                // Inicia a MainActivity
                startActivity(new Intent(AberturaActivity.this, MainActivity.class));
                //  Finaliza a AberturaActivity
                finish();
            }
            // Método chamado quando a animação é cancelada (não faz nada aqui)
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            // Método chamado quando a animação é repetida (não faz nada aqui)
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}




