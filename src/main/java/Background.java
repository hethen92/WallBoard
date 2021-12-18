import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Background extends JPanel {

    JLabel imageHolder;
    ImageIcon icon;
    int number;
    Random random;

    Background(){

        this.setSize(1920,1080);
        this.setLayout(null);

        random = new Random();

        number = random.nextInt(19); // Change to amount of pictures

        imageHolder = new JLabel();
        imageHolder.setIcon(startImage(number + ".jpg"));
        imageHolder.setBounds(0,0,1920,1080);

        this.add(imageHolder);

    }

    public void setImage(String time){

        number = random.nextInt(19); // Change to amount of pictures

        if(( time.equalsIgnoreCase("8:00 am") || time.equalsIgnoreCase("8:01 am") || time.equalsIgnoreCase("8:02 am"))){

            imageHolder.setIcon(makeDayImage(number + ".jpg"));
            imageHolder.setBounds(0,0,1920,1080);

        } else if (time.equalsIgnoreCase("8:00 pm") || time.equalsIgnoreCase("8:01 pm") || time.equalsIgnoreCase("8:02 pm"))  {

            imageHolder.setIcon(makeNightImage(number + ".jpg"));
            imageHolder.setBounds(0,0,1920,1080);

        }
    }

    public ImageIcon startImage(String filename) {
        icon = new ImageIcon(Background.class.getResource("Night Pictures/" + filename));
        return icon;
    }

    public ImageIcon makeNightImage(String filename) {
        icon.getImage().flush();
        icon = new ImageIcon(Background.class.getResource("Night Pictures/" + filename));
        return icon;
    }

    public ImageIcon makeDayImage(String filename) {
        icon.getImage().flush();
        icon = new ImageIcon(Background.class.getResource("Day Pictures/" + filename));
        return icon;
    }


}
