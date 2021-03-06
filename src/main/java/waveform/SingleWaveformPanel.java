package waveform;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import virtualcut.model.TrackModel;


public class SingleWaveformPanel extends JPanel {
    protected static final Color BACKGROUND_COLOR = Color.black;
    protected static final Color REFERENCE_LINE_COLOR = Color.gray;
    protected static final Color WAVEFORM_COLOR = Color.orange;

    private TrackModel model;
    private int channelIndex;
    private double width;
    
    public SingleWaveformPanel(TrackModel model, int channelIndex) {
        this.model = model;
        this.channelIndex = channelIndex;
        setBackground(BACKGROUND_COLOR);
        width = model.getWidth() * model.getXScaleFactor(model.getWidth());
        setPreferredSize(new Dimension((int)width, 140));

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int lineHeight = getHeight() / 2;
        g.setColor(REFERENCE_LINE_COLOR);
        g.drawLine(0, lineHeight, (int)getWidth(), lineHeight);

        drawWaveform(g, model.getAudio(channelIndex));

    }

    protected void drawWaveform(Graphics g, int[] samples) {
        if (samples == null) {
            return;
        }

        int oldX = 0;
        int oldY = (int) (getHeight() / 2);
        int xIndex = 0;

        int increment = model.getIncrement(model.getXScaleFactor(model.getWidth()));
    //    int increment = model.getIncrement();
        g.setColor(WAVEFORM_COLOR);

        int t = 0;

        for (t = 0; t < increment; t += increment) {
            g.drawLine(oldX, oldY, xIndex, oldY);
            xIndex++;
            oldX = xIndex;
        }

        for (; t < samples.length; t += increment) {
            double scaleFactor = model.getYScaleFactor(getHeight());
            double scaledSample = samples[t] * scaleFactor;
            int y = (int) ((getHeight() / 2) - (scaledSample));
            g.drawLine(oldX, oldY, xIndex, y);

            xIndex++;
            oldX = xIndex;
            oldY = y;
        }
    }
}
