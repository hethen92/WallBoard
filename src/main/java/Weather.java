import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Weather extends JPanel {

    String urlString = "https://api.openweathermap.org/data/2.5/onecall?lat=49.1913&lon=-122.8490&appid=4515d6eda00f3d7f97a5f6497b554cf0&units=metric&exclude=hourly,current,minutely,alerts";

    Map<String, Object> weatherMap0;
    Map<String, Object> weatherMap1;
    Map<String, Object> weatherMap2;
    Map<String, Object> weatherMap3;
    Map<String, Object> weatherMap4;

    WeatherPod weatherPod0;
    WeatherPod weatherPod1;
    WeatherPod weatherPod2;
    WeatherPod weatherPod3;
    WeatherPod weatherPod4;

    Weather() throws IOException {

        this.setSize(1200,150);
        this.setLocation(720, 0);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10,0));
        this.setBackground(Color.black);
        this.setOpaque(false);

        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null){
            result.append(line);
        }
        reader.close();
        //System.out.println(result);

        Map<String, Object> respMap = jsonToMap(result.toString());
        ArrayList<Map<String, Object>> daily = (ArrayList<Map<String, Object>>) respMap.get("daily");

        weatherMap0 = daily.get(0);
        weatherMap1 = daily.get(1);
        weatherMap2 = daily.get(2);
        weatherMap3 = daily.get(3);
        weatherMap4 = daily.get(4);

        weatherPod0 = new WeatherPod(weatherMap0);
        weatherPod1 = new WeatherPod(weatherMap1);
        weatherPod2 = new WeatherPod(weatherMap2);
        weatherPod3 = new WeatherPod(weatherMap3);
        weatherPod4 = new WeatherPod(weatherMap4);

        this.add(weatherPod0);
        this.add(weatherPod1);
        this.add(weatherPod2);
        this.add(weatherPod3);
        this.add(weatherPod4);

    }

    public static Map<String, Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {}.getType());
        return map;
    }

    public void refreshWeather() throws IOException {

        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null){
            result.append(line);
        }
        reader.close();
        //System.out.println(result);

        Map<String, Object> respMap = jsonToMap(result.toString());
        ArrayList<Map<String, Object>> daily = (ArrayList<Map<String, Object>>) respMap.get("daily");

        weatherMap0 = daily.get(0);
        weatherMap1 = daily.get(1);
        weatherMap2 = daily.get(2);
        weatherMap3 = daily.get(3);
        weatherMap4 = daily.get(4);

        weatherPod0.getValues(weatherMap0);
        weatherPod1.getValues(weatherMap1);
        weatherPod2.getValues(weatherMap2);
        weatherPod3.getValues(weatherMap3);
        weatherPod4.getValues(weatherMap4);

        weatherPod0.refreshWeatherPod();
        weatherPod1.refreshWeatherPod();
        weatherPod2.refreshWeatherPod();
        weatherPod3.refreshWeatherPod();
        weatherPod4.refreshWeatherPod();

    }

}

