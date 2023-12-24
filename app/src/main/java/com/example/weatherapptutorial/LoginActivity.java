package com.example.weatherapptutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);  // DatabaseHelper 인스턴스 생성

        // XML 레이아웃에서 위젯을 찾습니다.
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        Button buttonLogin = findViewById(R.id.login);
        Button buttonSignUp = findViewById(R.id.signUp);

        // 회원가입 버튼 클릭 시 회원가입 화면으로 이동합니다.
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭 시
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = editTextUsername.getText().toString().trim();
                String inputPassword = editTextPassword.getText().toString().trim();

                if (dbHelper.checkUser(inputUsername, inputPassword)) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    // 로그인 성공 시 MainActivity로 이동하며 도시 정보를 전달
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("city", dbHelper.getCityByUsername(inputUsername)); // getCityByUsername 메서드는 DatabaseHelper에 추가해야 합니다.
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "존재하지 않는 아이디나 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
