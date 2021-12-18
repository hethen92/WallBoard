import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time extends JPanel {

    SimpleDateFormat timeFormat;
    SimpleDateFormat dayFormat;
    JLabel timeLabel;
    JLabel dayLabel;
    String time;
    String day;

    Time(){

        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,-15));
        this.setSize(new Dimension(480,150));
        this.setOpaque(false);

        timeFormat = new SimpleDateFormat("h:mm a");
        dayFormat = new SimpleDateFormat("EEEE, MMMM d");

        timeLabel = new JLabel();
        timeLabel.setFont(new Font(null, Font.PLAIN, 100));
        timeLabel.setForeground(Color.white);

        dayLabel = new JLabel();
        dayLabel.setFont(new Font(null, Font.PLAIN, 40));
        dayLabel.setForeground(Color.white);

        time = timeFormat.format(Calendar.getInstance().getTime());
        time = time.replace("a.m.", "am").replace("p.m.","pm");
        timeLabel.setText(time);

        day = dayFormat.format(Calendar.getInstance().getTime());
        dayLabel.setText(day);

        this.add(timeLabel);
        this.add(dayLabel);

    }

    // Insert into while loop in main file

    public void setTime(){

        time = timeFormat.format(Calendar.getInstance().getTime());
        time = time.replace("a.m.", "am").replace("p.m.","pm");
        timeLabel.setText(time);

        day = dayFormat.format(Calendar.getInstance().getTime());
        dayLabel.setText(day);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
