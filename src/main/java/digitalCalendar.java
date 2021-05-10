import com.google.api.services.calendar.model.Event;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class digitalCalendar extends JPanel {

    JPanel topRow;
    LocalDate startDate;
    LocalDate reOrderDate;
    LocalDate date;
    dateRow dateRow1;
    dateRow dateRow2;
    dateRow dateRow3;
    dateRow dateRow4;

    digitalCalendar() throws GeneralSecurityException, IOException, ParseException {

        this.setSize(new Dimension(1566,880));
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLocation(0, 200);

        createTopRow();
        this.add(topRow);

        getStartDate();
        reOrderDate = startDate.plusWeeks(1);

        dateRow1 = new dateRow(startDate);
        date = startDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        dateRow2 = new dateRow(date);
        date = date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        dateRow3 = new dateRow(date);
        date = date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        dateRow4 = new dateRow(date);

        this.add(dateRow1);
        this.add(dateRow2);
        this.add(dateRow3);
        this.add(dateRow4);

    }

    public void reOrderCalendar() throws IOException, ParseException {

        dateRow1.reOrderRows(dateRow2);
        dateRow2.reOrderRows(dateRow3);
        dateRow3.reOrderRows(dateRow4);

        date = date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        dateRow4.newRow(date);
        dateRow4.refreshPods();

        startDate = reOrderDate;
        reOrderDate = startDate.plusWeeks(1);

    }


    public void refreshDateRow() throws IOException, ParseException {

        dateRow1.refreshPods();
        dateRow2.refreshPods();
        dateRow3.refreshPods();
        dateRow4.refreshPods();

    }

    public void getStartDate(){

        LocalDate now = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        Month curMonth = now.getMonth();

        if (now.getDayOfMonth() == 1){
            this.startDate = now;
        } else {

            while (true){

                now = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

                if (now.getDayOfMonth() == 1){
                    this.startDate = now;
                    break;
                } else if (now.getMonth() != curMonth){
                    this.startDate = now;
                    break;
                }

            }

        }

    }

    public void createTopRow(){

       topRow = new JPanel();
       topRow.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0));
       topRow.setMinimumSize(new Dimension(1566,28));
       topRow.setMaximumSize(new Dimension(1566,28));
       topRow.setOpaque(false);

       topRow.add(createTopBox("Sun"));
       topRow.add(createTopBox("Mon"));
       topRow.add(createTopBox("Tue"));
       topRow.add(createTopBox("Wed"));
       topRow.add(createTopBox("Thu"));
       topRow.add(createTopBox("Fri"));
       topRow.add(createTopBox("Sat"));


    }

    public JPanel createTopBox(String name){

        JPanel box = new JPanel();
        box.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        box.setPreferredSize(new Dimension(218,28));
        box.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        box.setOpaque(false);


        JLabel text = new JLabel(name);
        text.setFont(new Font(null, Font.PLAIN, 23));
        text.setForeground(Color.white);

        box.add(text);
        return box;
    }



}



