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
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class googleTasks extends JPanel {

    List<JLabel> tasks;
    String dateNow;
    String minDate;
    String maxDate;

    private static final String APPLICATION_NAME = "Google Tasks API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    googleTasks() throws GeneralSecurityException, IOException {

        this.setSize(300, 500);
        this.setLocation(1600, 250);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.black);
        this.setOpaque(false);

        JLabel header = new JLabel("Tasks");
        header.setFont(new Font(null, Font.PLAIN, 28));
        header.setForeground(Color.white);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(300, 1));
        line.setOpaque(false);
        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        this.add(header);
        this.add(line);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the first 10 task lists.
        TaskLists result = service.tasklists().list()  // Issue is here as google app is not verified
                .setMaxResults(10L)
                .execute();

        List<TaskList> taskLists = result.getItems();
        if (taskLists == null || taskLists.isEmpty()) {
            System.out.println("No task lists found.");
        } else {
            System.out.println("Task lists:");
            for (TaskList tasklist : taskLists) {
                System.out.printf("%s (%s)\n", tasklist.getTitle(), tasklist.getId());
            }
        }
    }


    public void getList(){


    }

    public void createListLabels(){


    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream in = googleTasks.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


}
