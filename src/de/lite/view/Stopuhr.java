package de.lite.view;

import de.lite.controller.StopuhrController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Stopuhr {
    private JFrame frame;
    private JButton button1;
    private JPanel mainPanel;
    private JLabel timeLabel;
    private JButton stopButton;
    private JList list1;
    private JSpinner spinner1;

    public Stopuhr(StopuhrController controller) {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = controller.getActRound();

                if (i == 0) {
                    controller.setRounds((int) spinner1.getValue());

                    setListModel(controller.getRounds());

                    setNameOfButton("Nächste Runde");

                    controller.startTimer();
                } else {
                    DefaultListModel _temp = (DefaultListModel) list1.getModel();
                    _temp.set(i - 1, controller.getRoundTime());
                    list1.setModel(_temp);
                }

                if (i == controller.getRounds()) {
                    setTimeLabel(controller.stopTimer());

                    setNameOfButton("Start");

                    controller.resetActRound();
                } else {
                    controller.incrementActRounds();
                }

                if ((i + 1) == controller.getRounds())
                    setNameOfButton("Messung abschließen");
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopTimer();

                setNameOfButton("Start");

                controller.resetActRound();

                setListModel((int) spinner1.getValue());

                setTimeLabel("00:00.000");
            }
        });

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        spinner1.setModel(spinnerModel);

        frame = new JFrame("Stopuhr");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void setListModel(int rounds) {
        DefaultListModel<String> list = new DefaultListModel<>();
        for (int i = 0; i < rounds; i++) {
            list.addElement("00:00.000");
        }
        list1.setModel(list);

        frame.pack();
    }

    public void setTimeLabel(String time) {
        timeLabel.setText(time);
        frame.repaint();
    }

    public void setNameOfButton(String name) {
        button1.setText(name);
    }
}
