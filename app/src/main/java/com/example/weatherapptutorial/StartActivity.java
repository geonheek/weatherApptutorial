package com.example.weatherapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    // 요청 코드를 정의합니다. 이 코드는 startActivityForResult를 호출할 때와 onActivityResult를 호출할 때 사용됩니다.
    private static final int REQUEST_CODE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        // '로그인' 버튼 클릭 시 LoginActivity로 이동
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // '회원가입' 버튼 클릭 시 RegisterActivity로 이동
        Button signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String newCity = data.getStringExtra("city");

            // MainActivity를 시작하고 새로운 도시 이름을 전달합니다.
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("city", newCity);
            startActivity(intent);
        }
    }
}
