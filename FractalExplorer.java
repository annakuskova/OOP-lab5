import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

    public class FractalExplorer {
        private int size;
        private JImageDisplay Display;
        private FractalGenerator generator;
        private Rectangle2D.Double range;

        public FractalExplorer(int size) {
            this.size = size;

            generator = new Mandelbrot();
            range = new Rectangle2D.Double();

            generator.getInitialRange(range);
            Display = new JImageDisplay(this.size, this.size);
        }

        public void createAndShowGUI() {
            Display.setLayout(new BorderLayout());

            JButton resetButton = new JButton("Reset");
            Resetter resetHandler = new Resetter();
            resetButton.addActionListener(resetHandler);

            JButton saveButton = new JButton("Save");
            Saver saveHandler = new Saver();
            saveButton.addActionListener(saveHandler);

            Clicker click = new Clicker();
            Display.addMouseListener(click);

            FractalGenerator mandelbrotFractal = new Mandelbrot();
            FractalGenerator tricornFractal = new Tricorn();
            FractalGenerator burningShipFractal = new BurningShip();

            JComboBox comboBox = new JComboBox();

            comboBox.addItem(mandelbrotFractal);
            comboBox.addItem(tricornFractal);
            comboBox.addItem(burningShipFractal);

            Chooser fractalChooser = new Chooser();
            comboBox.addActionListener(fractalChooser);

            JLabel label = new JLabel("Fractal:");

            JPanel panel = new JPanel();
            panel.add(label);
            panel.add(comboBox);

            JPanel myBottomPanel = new JPanel();
            myBottomPanel.add(saveButton);
            myBottomPanel.add(resetButton);

            JFrame myFrame = new JFrame("Fractal Explorer");

            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myFrame.add(myBottomPanel, BorderLayout.SOUTH);
            myFrame.add(Display, BorderLayout.CENTER);
            myFrame.add(panel, BorderLayout.NORTH);

            myFrame.pack();
            myFrame.setVisible(true);
            myFrame.setResizable(false);
        }

        private void drawFractal() {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {

                    double xCoord = FractalGenerator.getCoord(range.x,
                            range.x + range.width, size, x);

                    double yCoord = FractalGenerator.getCoord(range.y,
                            range.y + range.height, size, y);

                    int iteration = generator.numIterations(xCoord, yCoord);

                    if (iteration == -1) {
                        Display.drawPixel(x, y, 0);
                    } else {
                        float hue = 0.5f + (float) iteration / 50;
                        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                        Display.drawPixel(x, y, rgbColor);
                    }

                }
            }
            Display.repaint();
        }

        private class Resetter implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Reset")) {
                    generator.getInitialRange(range);
                    drawFractal();
                }
            }
        }

        private class Chooser implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof JComboBox) {
                    JComboBox comboBox = (JComboBox) source;

                    generator = (FractalGenerator) comboBox.getSelectedItem();
                    assert generator != null;

                    generator.getInitialRange(range);
                    drawFractal();
                }
            }
        }

        private class Saver implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Save")) {
                    JFileChooser fileChooser = new JFileChooser();

                    FileFilter extensionFilter = new FileNameExtensionFilter(
                            "PNG",
                            "png"
                    );

                    fileChooser.setFileFilter(extensionFilter);

                    fileChooser.setAcceptAllFileFilterUsed(false);

                    int userSelection = fileChooser.showSaveDialog(Display);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        java.io.File file = fileChooser.getSelectedFile();
                        String filePath = file.getPath();

                        if (!filePath.contains(".png")) file = new File(filePath + ".png");
                        try {
                            BufferedImage displayImage = Display.getImage();
                            javax.imageio.ImageIO.write(displayImage, "png", file);
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(Display,
                                    exception.getMessage(), "Cannot Save Image",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else return;
                }
            }
        }

        private class Clicker extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                double xCoord = FractalGenerator.getCoord(range.x,
                        range.x + range.width, size, x);

                int y = e.getY();
                double yCoord = FractalGenerator.getCoord(range.y,
                        range.y + range.height, size, y);

                generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

                drawFractal();
            }
        }

        public static void main(String[] args)
        {
            FractalExplorer displayExplorer = new FractalExplorer(400);
            displayExplorer.createAndShowGUI();
            displayExplorer.drawFractal();
        }
    }

