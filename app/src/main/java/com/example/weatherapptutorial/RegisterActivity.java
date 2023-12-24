package com.example.weatherapptutorial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsername, regPassword, regCity; // 도시 정보 추가
    private Button regRegister;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regCity = findViewById(R.id.reg_city); // 도시 정보 추가
        regRegister = findViewById(R.id.reg_register);

        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = regUsername.getText().toString().trim();
                String password = regPassword.getText().toString().trim();
                String city = regCity.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty() && !city.isEmpty()) {
                    if (dbHelper.checkIfUserExists(username)) {  // 아이디가 이미 존재하는지 확인
                        Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dbHelper.addUser(username, password, city)) {
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
