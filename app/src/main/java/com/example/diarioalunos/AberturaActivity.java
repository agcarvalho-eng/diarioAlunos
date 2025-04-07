package com.example.diarioalunos;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class AberturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);

        // Configurações adicionais para garantir que a animação funcione
        animationView.setAnimation(R.raw.animacao);
        animationView.setSpeed(1f);
        animationView.playAnimation();

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(AberturaActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
}



