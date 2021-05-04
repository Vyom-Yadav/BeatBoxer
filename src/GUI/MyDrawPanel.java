package GUI;
import javax.swing.*;
import java.awt.*;

public class MyDrawPanel extends JPanel {

/*
    @Override
    public void paintComponent(Graphics g) {
        Image image = new ImageIcon("/home/vyom/Pictures/Screenshot from 2021-05-03 18-16-32.png").getImage();
        g.drawImage(image,3,4,this);
        g.setColor(Color.RED);
        g.fillRect(100,100,100,100); // coordinates where it goes and how big it is.
    }
*/

/*
    @Override
    public void paintComponent(Graphics g) { // Paint a randomly colored circle on a black background.
        g.fillRect(0,0,this.getWidth(),this.getHeight());// 'this' is referred to current object of class
                                                                // MyDrawPanel which extends JPanel.

        int red = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);

        Color randomColor = new Color(red,blue,green);
        g.setColor(randomColor);
        g.fillOval(70,70,100,100); // start 70 pixels from left and 70 from top and make it 100 pixels wide
                                                // and 100 pixels tall.
    }
 */

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int red = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);

        Color startColor = new Color(red,blue,green);
        red = (int) (Math.random() * 255);
        blue = (int) (Math.random() * 255);
        green = (int) (Math.random() * 255);

        Color endColor = new Color(red,blue,green);

        GradientPaint gradient = new GradientPaint(70,70,startColor,180,180,endColor);
        g2d.setPaint(gradient);
        g2d.fillOval(70,70,150,150);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        MyDrawPanel a = new MyDrawPanel();
        frame.getContentPane().add(a);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }

}
