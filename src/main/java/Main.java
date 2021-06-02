import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class Main {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "C:\\WallBoardToken";
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar.events.readonly", TasksScopes.TASKS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";


    public static void main(String[] args) throws IOException, GeneralSecurityException, ParseException {


        // Get Credentials

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        InputStream in = Calendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential user = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        Calendar calService = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, user).setApplicationName("calendar").build();
        Tasks taskService = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, user).setApplicationName("tasks").build();

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

        digitalCalendar calendar = new digitalCalendar(calService);

        // Tasks

        googleTasks tasks = new googleTasks(taskService);


        // Adding all Components

        layeredPane.add(backgroundImage, Integer.valueOf(0));
        layeredPane.add(time, Integer.valueOf(1));
        layeredPane.add(weather, Integer.valueOf(1));
        layeredPane.add(calendar, Integer.valueOf(1));
        layeredPane.add(tasks, Integer.valueOf(1));

        container.add(layeredPane);
        container.setVisible(true);


        // Updating all Components

        boolean flag = false;
        boolean timeFlag = false;
        int x = 0;
        LocalDate now;

        while(true){

            time.setTime();

            if( ( time.time.equalsIgnoreCase("8:00 am") || time.time.equalsIgnoreCase("8:01 am") || time.time.equalsIgnoreCase("8:02 am") ||
                    time.time.equalsIgnoreCase("8:00 pm") || time.time.equalsIgnoreCase("8:01 pm") || time.time.equalsIgnoreCase("8:02 pm")) && !timeFlag){

                backgroundImage.setImage(time.time);
                timeFlag = true;

            } else if( ( time.time.equalsIgnoreCase("8:03 am") || time.time.equalsIgnoreCase("8:04 am") || time.time.equalsIgnoreCase("8:05 am") ||
                    time.time.equalsIgnoreCase("8:03 pm") || time.time.equalsIgnoreCase("8:04 pm") || time.time.equalsIgnoreCase("8:05 pm")) && timeFlag){

                timeFlag = false;
            }


            if( ( time.time.equalsIgnoreCase("12:01 am") || time.time.equalsIgnoreCase("12:02 am") || time.time.equalsIgnoreCase("12:03 am") ) && !flag) {

                weather.refreshWeather();
                tasks.updateTime();
                tasks.refresh();
                flag = true;

                now = LocalDate.now();
                if(calendar.reOrderDate.isBefore(now)){
                    calendar.reOrderCalendar();
                }

            } else if( ( time.time.equalsIgnoreCase("12:04 am") || time.time.equalsIgnoreCase("12:05 am") || time.time.equalsIgnoreCase("12:06 am") ) && flag){

                flag = false;

            }

            if ((x == 20)) {

                calendar.refreshDateRow(); // 120,960 Per Person
                tasks.refresh(); // 4,320 Per Person
                x = 0;

            }
            x++;


            now = LocalDate.now();
            if(calendar.reOrderDate.isBefore(now) || calendar.reOrderDate.equals(now)){
                calendar.reOrderCalendar();
            }


        }


    }
}
