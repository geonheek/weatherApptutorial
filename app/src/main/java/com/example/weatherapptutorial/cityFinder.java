package com.example.weatherapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * cityFinder 클래스는 도시 선택 화면을 나타내며, 검색 도시를 입력하거나
 * 미리 설정된 예시 도시(서울, 런던, 파리)를 선택할 수 있습니다.
 */

public class cityFinder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);

        // 검색 도시를 입력하는 EditText
        final EditText editText = findViewById(R.id.searchCity);
        // 뒤로 가기 버튼
        ImageView backButton = findViewById(R.id.backButton);
        // 위치 버튼
        ImageView locationButton = findViewById(R.id.locationButton);
        // 예시로 서울, 런던, 파리 버튼
        Button seoulButton = findViewById(R.id.seoulButton);
        Button londonButton = findViewById(R.id.londonButton);
        Button parisButton = findViewById(R.id.parisButton);

        backButton.setOnClickListener(v -> finish());

        // EditText에서 엔터 키 입력 시 호출되는 이벤트 처리
        editText.setOnEditorActionListener((v, actionId, event) -> {
            String newCity = editText.getText().toString();
            returnToMainActivity(newCity);
            return false;
        });

        seoulButton.setOnClickListener(v -> returnToMainActivity("Seoul"));
        londonButton.setOnClickListener(v -> returnToMainActivity("London"));
        parisButton.setOnClickListener(v -> returnToMainActivity("Paris"));
    }

    private void returnToMainActivity(String selectedCity) {
        Intent intent = new Intent();
        intent.putExtra("city", selectedCity);
        setResult(RESULT_OK, intent);
        finish();
    }
}
