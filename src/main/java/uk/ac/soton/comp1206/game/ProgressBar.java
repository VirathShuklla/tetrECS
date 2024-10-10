package uk.ac.soton.comp1206.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBar {

    JFrame frame = new JFrame();
    static JProgressBar bar = new JProgressBar(0,100);
    static Timer timer;

    ProgressBar(){
        bar.setValue(0);
        bar.setBounds(0,0,420,50);
        bar.setStringPainted(true);
        bar.setFont(new Font("MV Boli",Font.BOLD,25));
        bar.setForeground(Color.red);
        bar.setBackground(Color.black);

        frame.add(bar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);

        startTimer();
    }

    public static void startTimer() {
        if (timer != null){
            timer.stop();
        }
        else {
            timer = new Timer(Game.getTimerDelay(), new ActionListener() {
                int counter = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    while(counter<=Game.getTimerDelay()) {

                        bar.setValue(counter);
                        try {
                            Thread.sleep(Game.getTimerDelay());
                        } catch (InterruptedException eq) {
                            // TODO Auto-generated catch block
                            eq.printStackTrace();
                        }
                        counter +=1;
                    }
                    bar.setString("Done! :)");
                }
            });
            timer.start();
        }
    }

    public void main(String[] args) {
        new ProgressBar();
    }
}
