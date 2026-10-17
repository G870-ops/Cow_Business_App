package com.example.cowbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView statusText;
    private String fullText = "INITIALIZING AI CORE...";
    private int index = 0;
    private long delay = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.appName);
        statusText = findViewById(R.id.splashText);

        // 1. Initial Fade In
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);

        // 2. TRIGGER GLITCH AFTER 1 SECOND
        Animation glitch = AnimationUtils.loadAnimation(this, R.anim.glitch_effect);
        new Handler().postDelayed(() -> {
            appName.startAnimation(glitch);
            // Stop glitch after a short burst to make it look "subtle"
            new Handler().postDelayed(appName::clearAnimation, 600);
        }, 1200);

        // 3. Typing Animation
        statusText.setText("");
        startTypingAnimation();

        // 4. Final Transition
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 3800);
    }

    private void startTypingAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index <= fullText.length()) {
                    statusText.setText(fullText.substring(0, index++));
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }
}