package hr.fer.zemris.optjava.dz12.gui;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.gui.actionlistener.ActionListener;
import hr.fer.zemris.optjava.dz12.gui.jcomponent.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimulationFrame extends JFrame implements ActionListener {

    private Solution solution;
    private GPEngine engine;

    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton continueButton = new JButton("Continue");
    private JButton resetButton = new JButton("Reset");

    private volatile boolean stop;

    public SimulationFrame(Solution solution, GPEngine engine) {
        this.solution = solution;
        this.engine = engine;
        this.engine.addListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SimulationFrame frame = SimulationFrame.this;
                frame.engine.removeListener(frame);
                System.exit(0);
            }
        });
        setResizable(false);
        setTitle("Ant trail simulation");
        initGUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void initGUI() {
        initButtons();

        JPanel panel = new JPanel(new GridLayout(4, 0));
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(continueButton);
        panel.add(resetButton);

        getContentPane().add(panel, BorderLayout.EAST);
        getContentPane().add(new Platform(engine));
    }

    private void initButtons() {
        startButton.setFocusPainted(false);
        stopButton.setFocusPainted(false);
        continueButton.setFocusPainted(false);
        resetButton.setFocusPainted(false);
        stopButton.setEnabled(false);
        continueButton.setEnabled(false);
        resetButton.setEnabled(false);
        initStartButton();
        initStopButton();
        initContinueButton();
        initResetButton();
    }

    private void initStartButton() {
        startButton.addActionListener(l -> new Thread(() -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            while (engine.hasMoreActions() && !stop) solution.getRoot().applyFunction(engine);
            if (!stop) {
                stopButton.setEnabled(false);
                resetButton.setEnabled(true);
            }
        }).start());
    }

    private void initStopButton() {
        stopButton.addActionListener(l -> {
            stopButton.setEnabled(false);
            startButton.setEnabled(false);
            continueButton.setEnabled(true);
            resetButton.setEnabled(true);
            stop = true;
        });
    }

    private void initContinueButton() {
        continueButton.addActionListener(l -> {
            continueButton.setEnabled(false);
            resetButton.setEnabled(false);
            stopButton.setEnabled(true);
            startButton.setEnabled(true);
            stop = false;
            startButton.doClick();
        });
    }

    private void initResetButton() {
        resetButton.addActionListener(l -> {
            resetButton.setEnabled(false);
            stopButton.setEnabled(false);
            continueButton.setEnabled(false);
            startButton.setEnabled(true);
            engine.reset();
            stop = false;
            actionExecuted();
        });
    }

    @Override
    public void actionExecuted() {
        SwingUtilities.invokeLater(this::repaint);
    }

}
