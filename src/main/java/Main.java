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
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class Main {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
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
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
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
        int x = 0, y = 0;
        LocalDate now;

        while(true){

            time.setTime();

            backgroundImage.setImage(time.time);

            if(time.time.equals("12:00 am") && !flag) {

                weather.refreshWeather();
                System.out.println(tasks.maxTime);
                tasks.updateTime();
                System.out.println(tasks.maxTime);
                tasks.refresh();
                flag = true;

                now = LocalDate.now();
                if(calendar.reOrderDate.isBefore(now)){
                    calendar.reOrderCalendar();
                }

            } else if(!time.time.equals("12:00 am") && flag){

                flag = false;

            }

            if ((x == 10) && !time.time.equals("12:00 am")) {

                calendar.refreshDateRow();
                x = 0;

            }
            if ((y == 20) && !time.time.equals("12:00 am")) {

                tasks.refresh();
                y = 0;

            }

            x++;
            y++;

            now = LocalDate.now();
            if(calendar.reOrderDate.isBefore(now)){
                calendar.reOrderCalendar();
            }


        }


    }
}
