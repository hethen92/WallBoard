import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.List;

public class CalendarPod extends JPanel {

    List<JLabel> texts;
    DateTime startTime;
    DateTime endTime;
    JLabel label;
    String dateNum;
    JLabel dateNumLabel;

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();

    public CalendarPod(LocalDate date) throws GeneralSecurityException, IOException, ParseException {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(218,213));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        this.setOpaque(false);

        dateNum = String.valueOf(date.getDayOfMonth());
        dateNumLabel = new JLabel(dateNum);
        dateNumLabel.setFont(new Font(null, Font.PLAIN, 23));
        dateNumLabel.setForeground(Color.white);

        JPanel numBox = new JPanel();
        numBox.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        numBox.setOpaque(false);
        numBox.setMaximumSize(new Dimension(218,30));
        numBox.add(dateNumLabel);

        this.add(numBox);


        Instant instant = date.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        startTime = new DateTime(timeInMillis);

        instant = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
        timeInMillis = instant.toEpochMilli();
        endTime = new DateTime(timeInMillis);

        Events events = service.events().list("primary").setTimeMin(startTime).setTimeMax(endTime).setMaxResults(5).setOrderBy("startTime").setSingleEvents(true).execute();

        List<Event> items = events.getItems();
        texts = new ArrayList<JLabel>();
        for (Event event : items) {

            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();

            if(start != null){

                SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                String s = format.format(parse.parse(start.toString()).getTime());
                s = s.replace("a.m.", "").replace("p.m.","");
                String e = format.format(parse.parse(end.toString()).getTime());
                e = e.replace("a.m.", "am").replace("p.m.","pm");

                label = new JLabel("<html><div style='text-align: center;'>" + event.getSummary() + " " + s + "- " + e + "</div></html>", SwingConstants.CENTER);

            } else {
                label = new JLabel(event.getSummary());
            }

            label.setFont(new Font(null, Font.PLAIN, 20));
            label.setForeground(Color.white);
            label.setAlignmentX(CENTER_ALIGNMENT);

            texts.add(label);
            this.add(label);

        }

        int x = texts.size();
        while (x < 5){

            label = new JLabel();
            label.setFont(new Font(null, Font.PLAIN, 20));
            label.setForeground(Color.white);
            label.setAlignmentX(CENTER_ALIGNMENT);

            texts.add(label);
            this.add(label);
            x++;

        }

    }

    public void newRowTimes(LocalDate date){

        dateNum = String.valueOf(date.getDayOfMonth());
        dateNumLabel.setText(dateNum);

        Instant instant = date.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        startTime = new DateTime(timeInMillis);

        instant = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
        timeInMillis = instant.toEpochMilli();
        endTime = new DateTime(timeInMillis);

    }

    public void refresh() throws IOException, ParseException {

        Events events = service.events().list("primary").setTimeMin(startTime).setTimeMax(endTime).setMaxResults(5).setOrderBy("startTime").setSingleEvents(true).execute();
        List<Event> items = events.getItems();

        int x = 0;
        for (Event event : items) {

            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();

            if(texts.get(x).getText().equals(event.getSummary()) == false){
                if(start != null){

                    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                    String s = format.format(parse.parse(start.toString()).getTime());
                    s = s.replace("a.m.", "").replace("p.m.","");
                    String e = format.format(parse.parse(end.toString()).getTime());
                    e = e.replace("a.m.", "am").replace("p.m.","pm");

                    texts.get(x).setText("<html><div style='text-align: center;'>" + event.getSummary() + " " + s + "- " + e + "</div></html>");

                } else {
                    texts.get(x).setText(event.getSummary());
                }
            }

            x++;
        }

        while (x < 5){

            texts.get(x).setText("");
            x++;

        }

    }


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

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
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}
