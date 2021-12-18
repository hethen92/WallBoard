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

        com.google.api.services.tasks.model.Tasks t = service.tasks().list("@default").setFields("items(title,due)").setDueMax(maxTime).setMaxResults(14L).execute();
        text = new ArrayList<JLabel>();

        if(t != null){

            List<Task> items = t.getItems();
            orderTasks(items);

            LocalDateTime now = LocalDate.now().atTime(LocalTime.MAX);
            LocalDateTime tom = LocalDate.now().plusDays(1).atTime(LocalTime.MAX);
            LocalDateTime dayAfterTom = LocalDate.now().plusDays(2).atTime(LocalTime.MAX);

            for (int x = 0; x < items.size(); x++) {

                if(x > 0){

                    Date a1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x-1).getDue().toString().substring(0,10));
                    Date b1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x).getDue().toString().substring(0,10));
                    int result = a1.compareTo(b1);

                    if (result < 0){

                        if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(now)))){
                            label = new JLabel("<html><div style='text-align: left;'>" + "Today:" + "</div></html>", SwingConstants.LEFT);
                        }else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tom)))){
                            label = new JLabel("<html><div style='text-align: left;'>" + "Tomorrow:" + "</div></html>", SwingConstants.LEFT);
                        } else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(dayAfterTom)))){
                            label = new JLabel("<html><div style='text-align: left;'>" + "Overmorrow:" + "</div></html>", SwingConstants.LEFT);
                        } else {
                            label = new JLabel("<html><div style='text-align: left;'>" + "Past:" + "</div></html>", SwingConstants.LEFT);
                        }

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

                } else {

                    Date b1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x).getDue().toString().substring(0,10));

                    if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(now)))){
                        label = new JLabel("<html><div style='text-align: left;'>" + "Today:" + "</div></html>", SwingConstants.LEFT);
                    }else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tom)))){
                        label = new JLabel("<html><div style='text-align: left;'>" + "Tomorrow:" + "</div></html>", SwingConstants.LEFT);
                    } else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(dayAfterTom)))){
                        label = new JLabel("<html><div style='text-align: left;'>" + "Overmorrow:" + "</div></html>", SwingConstants.LEFT);
                    } else {
                        label = new JLabel("<html><div style='text-align: left;'>" + "Past:" + "</div></html>", SwingConstants.LEFT);
                    }

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

                label = new JLabel("<html><div style='text-align: center;'>" + items.get(x).getTitle() + "</div></html>", SwingConstants.CENTER);
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
        while (x < 17){ // 14 Tasks description + Today: Tomorrow: Day After Tomorrow

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

        LocalDateTime nowPlus2 = LocalDate.now().plusDays(2).atTime(LocalTime.MAX);
        Instant instant = nowPlus2.atZone(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        maxTime = new DateTime(timeInMillis).toString();

    }

    void refresh() throws IOException, ParseException {

        try {

            com.google.api.services.tasks.model.Tasks t = service.tasks().list("@default").setFields("items(title,due)").setDueMax(maxTime).setMaxResults(14L).execute();

            int y = 0;
            if(t != null){

                List<Task> items = t.getItems();
                orderTasks(items);

                LocalDateTime now = LocalDate.now().atTime(LocalTime.MAX);
                LocalDateTime tom = LocalDate.now().plusDays(1).atTime(LocalTime.MAX);
                LocalDateTime dayAfterTom = LocalDate.now().plusDays(2).atTime(LocalTime.MAX);

                for (int x = 0; x < items.size(); x++) {

                    if(x > 0){

                        Date a1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x-1).getDue().toString().substring(0,10));
                        Date b1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x).getDue().toString().substring(0,10));
                        int result = a1.compareTo(b1);

                        if (result < 0){

                            if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(now)))){
                                text.get(y).setText("<html><div style='text-align: left;'>" + "Today:" + "</div></html>");
                            }else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tom)))){
                                text.get(y).setText("<html><div style='text-align: left;'>" + "Tomorrow:" + "</div></html>");
                            } else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(dayAfterTom)))){
                                text.get(y).setText("<html><div style='text-align: left;'>" + "Overmorrow:" + "</div></html>");
                            } else {
                                text.get(y).setText("<html><div style='text-align: left;'>" + "Past:" + "</div></html>");
                            }
                            text.get(y).setHorizontalAlignment(SwingConstants.LEFT);
                            y++;
                        }

                    } else {

                        Date b1 = new SimpleDateFormat("yyyy-MM-dd").parse(items.get(x).getDue().toString().substring(0,10));

                        if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(now)))){
                            text.get(y).setText("<html><div style='text-align: left;'>" + "Today:" + "</div></html>");
                        }else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tom)))){
                            text.get(y).setText("<html><div style='text-align: left;'>" + "Tomorrow:" + "</div></html>");
                        } else if(b1.equals(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(dayAfterTom)))){
                            text.get(y).setText("<html><div style='text-align: left;'>" + "Overmorrow:" + "</div></html>");
                        } else {
                            text.get(y).setText("<html><div style='text-align: left;'>" + "Past:" + "</div></html>");
                        }
                        text.get(y).setHorizontalAlignment(SwingConstants.LEFT);
                        y++;
                    }

                    text.get(y).setText("<html><div style='text-align: center;'>" + items.get(x).getTitle() + "</div></html>");
                    text.get(y).setHorizontalAlignment(SwingConstants.CENTER);
                    y++;

                }
            }

            while (y < 17){

                text.get(y).setText("");
                y++;

            }
        } catch (Exception e) {

            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

            System.out.println("Google Tasks API Failed to get Tasks: " + myDateObj.format(myFormatObj));

        }

    }


}
