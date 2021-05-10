import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeatherPod extends JPanel {

    JLabel dayLabel;
    JLabel dayTempLabel;
    JLabel tempMaxLabel;
    JLabel tempMinLabel;
    JLabel icon;

    String day;
    String dayTemp;
    String tempMax;
    String tempMin;
    String iconCode;

    WeatherPod(Map<String, Object> daily) throws IOException {

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        getValues(daily);

        // Center Image Icon

        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        icon = new JLabel();
        icon.setIcon(makeImage(iconCode + ".png"));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconPanel.add(icon);

        // Bottom Day Temp and Day

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        dayTempLabel = new JLabel(dayTemp + "\u00B0 ");
        dayTempLabel.setFont(new Font(null, Font.PLAIN, 40));
        dayTempLabel.setForeground(Color.white);

        dayLabel = new JLabel(day + "  ");
        dayLabel.setFont(new Font(null, Font.PLAIN, 40));
        dayLabel.setForeground(Color.white);

        bottomPanel.add(dayTempLabel);
        bottomPanel.add(dayLabel);

        // Right Min and Max Temp

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        tempMaxLabel = new JLabel(tempMax + "\u00B0");
        tempMaxLabel.setFont(new Font(null, Font.PLAIN, 20));
        tempMaxLabel.setForeground(Color.white);
        tempMaxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tempMinLabel = new JLabel(tempMin + "\u00B0");
        tempMinLabel.setFont(new Font(null, Font.PLAIN, 20));
        tempMinLabel.setForeground(Color.white);
        tempMinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel spacer1 = new JPanel();
        spacer1.setSize(0,20);
        spacer1.setOpaque(false);

        JPanel spacer2 = new JPanel();
        spacer2.setSize(0,20);
        spacer2.setOpaque(false);

        rightPanel.add(spacer1);
        rightPanel.add(tempMaxLabel);
        rightPanel.add(spacer2);
        rightPanel.add(tempMinLabel);

        this.add(iconPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.PAGE_END);
        this.add(rightPanel, BorderLayout.LINE_END);
    }

    public static Map<String, Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {}.getType());
        return map;
    }

    void getValues(Map<String, Object> daily){

        day = daily.get("dt").toString();
        BigDecimal bd = new BigDecimal(day);
        long seconds = bd.longValue();
        java.util.Date time = new java.util.Date(seconds*1000);
        day = time.toString();
        day = day.substring(0,3);

        Map<String, Object> temp = jsonToMap(daily.get("temp").toString());
        float floatVar;
        int intVar;

        dayTemp = temp.get("day").toString();
        floatVar = Float.parseFloat(dayTemp);
        intVar = Math.round(floatVar);
        dayTemp = Integer.toString(intVar);

        tempMin = temp.get("min").toString();
        floatVar = Float.parseFloat(tempMin);
        intVar = Math.round(floatVar);
        tempMin = Integer.toString(intVar);

        tempMax = temp.get("max").toString();
        floatVar = Float.parseFloat(tempMax);
        intVar = Math.round(floatVar);
        tempMax = Integer.toString(intVar);

        ArrayList<Map<String, Object>> weather = (ArrayList<Map<String, Object>>) daily.get("weather");
        Map<String, Object> icon = weather.get(0);
        iconCode = icon.get("icon").toString();
        iconCode = iconCode.replace("d", "").replace("n.","");

//        System.out.println(day);
//        System.out.println(dayTemp);
//        System.out.println(tempMin);
//        System.out.println(tempMax);
//        System.out.println(iconCode);

    }

    public void refreshWeatherPod(){

        // Center Image Icon

        icon.setIcon(makeImage(iconCode + ".png"));

        // Bottom Day Temp and Day

        dayTempLabel.setText(dayTemp + "\u00B0 ");
        dayLabel.setText(day + "  ");

        // Right Min and Max Temp

        tempMaxLabel.setText(tempMax + "\u00B0");
        tempMinLabel.setText(tempMin + "\u00B0");

    }

    public static ImageIcon makeImage(String filename) {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Icons/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(myPicture);
    }

}
