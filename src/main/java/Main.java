import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, GeneralSecurityException, ParseException {

        // Container Frame

        JFrame container = new JFrame();
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setUndecorated(true);
        container.setLayout(null);
        container.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Layered Pane

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(1920,1080);

        // Background Image

        Background backgroundImage = new Background();

        // Time

        Time time = new Time();

        // Weather

        Weather weather = new Weather();

        // Calendar

        digitalCalendar calendar = new digitalCalendar();

        // Tasks

//        googleTasks tasks = new googleTasks();


        // Adding all Components

        layeredPane.add(backgroundImage, Integer.valueOf(0));
        layeredPane.add(time, Integer.valueOf(1));
        layeredPane.add(weather, Integer.valueOf(1));
        layeredPane.add(calendar, Integer.valueOf(1));
//        layeredPane.add(tasks, Integer.valueOf(1));

        container.add(layeredPane);
        container.setVisible(true);


        // Updating all Components

        boolean weatherFlag = false;
        int x = 0;
        LocalDate now;

        while(true){

            time.setTime();

            backgroundImage.setImage(time.time);

            if(time.time.equals("12:00 am") && !weatherFlag) { // Does not work

                weather.refreshWeather();
                weatherFlag = true;

                now = LocalDate.now();
                if(calendar.reOrderDate.isBefore(now)){
                    calendar.reOrderCalendar();
                }

            } else if(!time.time.equals("12:00 am") && weatherFlag){

                weatherFlag = false;

            }

            if (x == 10){

                calendar.refreshDateRow();
                x = 0;

            }
            x++;
            now = LocalDate.now();
            if(calendar.reOrderDate.isBefore(now)){
                calendar.reOrderCalendar();
            }
        }


    }
}
