package com.example.weatherapptutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * MainActivity 클래스는 날씨 정보를 표시하는 메인 화면을 나타냅니다.
 * OpenWeatherMap API를 이용하여 현재 위치 또는 선택한 도시의 날씨 정보를 가져와 표시합니다.
 */

public class MainActivity extends AppCompatActivity {

    //OpenWeatherMap API키 & 엔드포인트 URL
    final String APP_ID = "310008b2e7cc618f59323781648a35b1";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final int CITY_FINDER_REQUEST_CODE = 1;
    final int REQUEST_CODE = 101;

    // UI 요소 변수 선언
    TextView NameofCity, weatherState, Temperature, mFeelsLikeTextView, mMinTempTextView, mMaxTempTextView, mHumidityTextView;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;

    // 위치 정보 관련 변수 선언
    LocationManager mLocationManager;
    LocationListener mLocationListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 요소 초기화
        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);
        NameofCity = findViewById(R.id.cityName);
        mFeelsLikeTextView = findViewById(R.id.feelsLike);
        mMinTempTextView = findViewById(R.id.minTemp);
        mMaxTempTextView = findViewById(R.id.maxTemp);
        mHumidityTextView = findViewById(R.id.humidity);

        /// StartActivity로부터 전달받은 도시 이름을 가져옵니다.
        String city = getIntent().getStringExtra("city");
        if (city != null) {
            // 새로운 도시의 날씨 정보를 가져옵니다.
            getWeatherForNewCity(city);
        }

        // 도시 찾기 버튼 클릭 시 이벤트 처리
        mCityFinder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, cityFinder.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    // 도시 찾기 액티비티에서 결과를 받아올 때 호출되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String newCity = data.getStringExtra("city");

            // 새로운 도시의 날씨 정보를 가져옵니다.
            getWeatherForNewCity(newCity);
        }
    }

    // 액티비티가 화면에 나타날 때 호출되는 메서드
    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String city = mIntent.getStringExtra("City");
        if (city != null) {
            getWeatherForNewCity(city);
        } else {
            getWeatherForCurrentLocation();
        }
    }

    // 액티비티가 일시 중지될 때 호출되는 메서드
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null && mLocationListner != null) {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }

    // 새로운 도시의 날씨 정보를 가져오는 메서드
    private void getWeatherForNewCity(String city) {
        // 위치 변경 시 UI 초기화
        resetUI();

        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);

        RequestWeather(params);
    }


    // 현재 위치의 날씨 정보를 가져오는 메서드
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("WeatherApp", "Location changed: " + location.getLatitude() + ", " + location.getLongitude());
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                // 위치 변경 시 UI 초기화
                resetUI();

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                RequestWeather(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };

        // 위치 권한이 허용되어 있지 않으면 권한 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
    }

    // 새로운 도시를 선택했을 때 UI를 초기화하는 메서드
    private void resetUI() {
        Temperature.setText("");
        NameofCity.setText("");
        weatherState.setText("");
        mFeelsLikeTextView.setText("");
        mMinTempTextView.setText("");
        mMaxTempTextView.setText("");
        mHumidityTextView.setText("");
        mweatherIcon.setImageResource(android.R.color.transparent); // 이미지 초기화
    }

    // 위치 권한 요청 결과를 처리하는 메서드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location get Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                // 사용자에게 권한이 필요하다고 알리거나 적절한 조치를 취할 수 있습니다.
                Toast.makeText(MainActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // OpenWeatherMap API를 통해 날씨 정보를 요청하는 메서드
    private void RequestWeather(RequestParams params) {
        Log.d("WeatherApp", "Making network request with params: " + params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "데이터 로드 성공", Toast.LENGTH_SHORT).show();
                weatherData weatherD = weatherData.fromJson(response);

                if (weatherD != null) {
                    updateUI(weatherD);
                } else {
                    // weatherD가 null인 경우 처리
                    Toast.makeText(MainActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            // 날씨 정보를 가져오지 못한 경우 처리
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("WeatherApp", "데이터 로드 실패", throwable);
                Toast.makeText(MainActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(weatherData weather) {
        Temperature.setText(getString(R.string.temperature_format, weather.getmTemperature()));
        NameofCity.setText(getString(R.string.city_format, weather.getMcity()));
        weatherState.setText(getString(R.string.weather_type_format, weather.getmWeatherType()));
        mFeelsLikeTextView.setText(getString(R.string.feels_like_format, weather.getmFeelsLike()));
        mMinTempTextView.setText(getString(R.string.min_temp_format, weather.getmMinTemp()));
        mMaxTempTextView.setText(getString(R.string.max_temp_format, weather.getmMaxTemp()));
        mHumidityTextView.setText(getString(R.string.humidity_format, weather.getmHumidity()));

        int resourceID = getResources().getIdentifier(weather.getMicon(), "drawable", getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

}