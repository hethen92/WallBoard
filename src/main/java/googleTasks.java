
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import jdk.internal.vm.compiler.collections.EconomicMap;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import java.awt.*;

import java.util.List;

public class googleTasks extends JPanel {

    List<JLabel> text;
    JLabel label;
    JPanel gap;
    String maxTime;
    Tasks service;


    googleTasks(Tasks service) throws IOException, ParseException {

        this.setSize(300, 800);
        this.setLocation(1600, 250);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

        this.service = service;

        updateTime();

        com.google.api.services.tasks.model.Tasks t = service.tasks().list("@default").setFields("items(title,due)").setDueMax(maxTime).setMaxResults(8L).execute();
        text = new ArrayList<JLabel>();

       if(t != null){

           List<Task> items = t.getItems();
           orderTasks(items);

           for (Task task : items) {

               label = new JLabel("<html><div style='text-align: center;'>" + task.getTitle() + "</div></html>", SwingConstants.CENTER);
               label.setFont(new Font(null, Font.PLAIN, 25));
               label.setForeground(Color.white);
               label.setAlignmentX(CENTER_ALIGNMENT);

               gap = new JPanel();
               gap.setMaximumSize(new Dimension(50,15));
               gap.setOpaque(false);

               text.add(label);
               this.add(gap);
               this.add(label);
           }
       }

        int x = text.size();
        while (x < 8){

            label = new JLabel("<html><div style='text-align: center;'>" + " " + "</div></html>", SwingConstants.CENTER);
            label.setFont(new Font(null, Font.PLAIN, 25));
            label.setForeground(Color.white);
            label.setAlignmentX(CENTER_ALIGNMENT);

            gap = new JPanel();
            gap.setMaximumSize(new Dimension(50,15));
            gap.setOpaque(false);

            text.add(label);
            this.add(gap);
            this.add(label);
            x++;

        }

    }

    void orderTasks(List<Task> items) throws ParseException {

       Collections.sort(items, new Comparator<Task>() {

           @Override
           public int compare(Task a, Task b) {

               int result = 0;

               try {

                   Date a1 = new SimpleDateFormat("yyyy-MM-dd").parse(a.getDue().toString().substring(0,10));
                   Date b1 = new SimpleDateFormat("yyyy-MM-dd").parse(b.getDue().toString().substring(0,10));

                   result = a1.compareTo(b1);

               } catch (ParseException e) {
                   e.printStackTrace();
               }

               return result;
           }
       });

    }

    void updateTime(){

        LocalDateTime now = LocalDate.now().atTime(LocalTime.MAX);
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        maxTime = new DateTime(timeInMillis).toString();

    }

    void refresh() throws IOException, ParseException {

        try {

            com.google.api.services.tasks.model.Tasks t = service.tasks().list("@default").setFields("items(title,due)").setDueMax(maxTime).setMaxResults(8L).execute();

            int x = 0;
            if(t != null){

                List<Task> items = t.getItems();
                orderTasks(items);

                for (Task task : items) {

                    if(text.get(x).getText().equals(task.getTitle()) == false) {

                        text.get(x).setText("<html><div style='text-align: center;'>" + task.getTitle() + "</div></html>");
                    }

                    x++;

                }
            }

            while (x < 8){

                text.get(x).setText("");
                x++;

            }
        } catch (Exception e) {

            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

            System.out.println("Google Tasks API Failed to get Tasks: " + myDateObj.format(myFormatObj));

        }

    }


}


