import com.google.api.services.calendar.Calendar;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class dateRow extends JPanel {

    List<CalendarPod> pods;

    dateRow(LocalDate startDate, Calendar calService) throws ParseException, GeneralSecurityException, IOException {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0));
        this.setMinimumSize(new Dimension(1566,213));
        this.setMaximumSize(new Dimension(1566,213));
        this.setOpaque(false);

        pods = new ArrayList<>();
        int x = 0;
        while (x < 7) {

           CalendarPod p = new CalendarPod(startDate, calService);
           pods.add(p);
           this.add(p);
           startDate = startDate.plusDays(1);
           x++;

        }


    }

    public void newRow(LocalDate date){

        int x = 0;
        while (x < 7) {

            pods.get(x).newRowTimes(date);
            date = date.plusDays(1);
            x++;

        }

    }

    public void reOrderRows(dateRow rowPrev){

        int x = 0;
        while (x < 7){

            pods.get(x).dateNumLabel.setText(rowPrev.pods.get(x).dateNumLabel.getText());
            pods.get(x).startTime = rowPrev.pods.get(x).startTime;
            pods.get(x).endTime = rowPrev.pods.get(x).endTime;

            for(int y = 0; y < 5; y++){
                pods.get(x).texts.get(y).setText(rowPrev.pods.get(x).texts.get(y).getText());
            }

            x++;
        }

    }

    public void refreshPods() throws IOException, ParseException {

        int x = 0;

        while (x < 7){
            pods.get(x).refresh();
            x++;
        }

    }



}
