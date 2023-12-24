package com.example.weatherapptutorial;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * weatherData 클래스는 OpenWeatherMap API에서 가져온 날씨 정보를 저장하고 처리하는 데 사용됩니다.
 * JSON 형식의 날씨 데이터를 weatherData 객체로 변환하여 사용합니다.
 */

public class weatherData {

    // 기온, 날씨 아이콘, 도시 이름, 날씨 상태, 체감 기온, 최저 기온, 최고 기온, 습도를 저장하는 변수
    private String mTemperature, micon, mcity, mWeatherType, mFeelsLike, mMinTemp, mMaxTemp, mHumidity;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {

        try
        {
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            // 기온 및 관련 정보 추출 및 변환
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            double feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like") - 273.15;
            double minTemp = jsonObject.getJSONObject("main").getDouble("temp_min") - 273.15;
            double maxTemp = jsonObject.getJSONObject("main").getDouble("temp_max") - 273.15;
            weatherD.mHumidity = jsonObject.getJSONObject("main").getString("humidity");

            // 기온 및 관련 정보를 반올림하여 정수로 변환
            int roundedValue = (int) Math.rint(tempResult);
            int roundedFeelsLike = (int) Math.rint(feelsLike);
            int roundedMinTemp = (int) Math.rint(minTemp);
            int roundedMaxTemp = (int) Math.rint(maxTemp);

            weatherD.mTemperature = Integer.toString(roundedValue);
            weatherD.mFeelsLike = Integer.toString(roundedFeelsLike);
            weatherD.mMinTemp = Integer.toString(roundedMinTemp);
            weatherD.mMaxTemp = Integer.toString(roundedMaxTemp);

            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 날씨 상태 코드를 기반으로 날씨 아이콘을 업데이트하는 메서드
    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<300)
        {
            return "thunderstorm";
        }
        if(condition>=300 && condition<500)
        {
            return "lightrain";
        }
        if(condition>=500 && condition<600)
        {
            return "shower";
        }
        if(condition>=600 && condition<700)
        {
            return "snow";
        }
        if(condition>=700 && condition<=771)
        {
            return "fog";
        }

        if(condition>=772 && condition<800)
        {
            return "overcast";
        }
        if(condition==800)
        {
            return "sunny";
        }
        if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        return null;
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public String getmFeelsLike() {
        return mFeelsLike + "°C";
    }

    public String getmMinTemp() {
        return mMinTemp + "°C";
    }

    public String getmMaxTemp() {
        return mMaxTemp + "°C";
    }

    public String getmHumidity() {
        return mHumidity + "%";
    }
}
