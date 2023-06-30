import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.io.IOException;


import com.dtw.TimeWarpInfo;
import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;


public class MouseMovementRecorder extends JPanel implements MouseMotionListener {
    private List<Point> recordedMovements;
    private BufferedImage canvas;
    private List<Template> templates;
    private boolean recognitionEnabled;
    private List<Point> recordedMovementsFree;



    public MouseMovementRecorder() {
        recordedMovements = new ArrayList<>();
        canvas = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        templates = new ArrayList<>();
        recognitionEnabled = false;

        addMouseMotionListener(this);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templateName = JOptionPane.showInputDialog("Geben Sie den Namen des Templates ein:");
                if (templateName != null && !templateName.isEmpty()) {
                    saveTemplate(templateName);
                }
            }
        });

        JButton classifyButton = new JButton("Zug machen");
        classifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (TicTacToe.isPlayer1Turn)
                {
                    try {
                        int classifiedNumber = classifyInput()-1;
                        TicTacToe.gridCells[classifiedNumber].setForeground(new Color(252, 0, 0));
                        TicTacToe.gridCells[classifiedNumber].setText("X");
                        TicTacToe.isPlayer1Turn = false;
                        TicTacToe.title.setText("O's turn");
                        TicTacToe.turnCounter++;
                        try {
                            TicTacToe.checkForWinner();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                //if gridCell is already filled do nothing
                else
                {
                    try {
                        int classifiedNumber = classifyInput()-1;
                        TicTacToe.gridCells[classifiedNumber].setForeground(new Color(9, 58, 255));
                        TicTacToe.gridCells[classifiedNumber].setText("O");
                        TicTacToe.isPlayer1Turn = true;
                        TicTacToe.title.setText("X's turn");
                        TicTacToe.turnCounter++;
                        try {
                            TicTacToe.checkForWinner();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                recordedMovements.clear();
                repaint();
            }
        });

        JButton templateManagerButton = new JButton("Template Manager");
        templateManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTemplateManagerWindow();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(classifyButton);
        buttonPanel.add(templateManagerButton);
        buttonPanel.add(TicTacToe.undoButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        recordedMovementsFree = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!recognitionEnabled) {
                    recordedMovements.clear();
                }
                recordedMovementsFree.clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!recognitionEnabled) {
                    recordedMovements.add(e.getPoint());
                } else {
                    recordedMovementsFree.add(e.getPoint());
                    repaint();
                }
            }
        });
    }

    private void saveTemplate(String templateName) {
        Template template = new Template(templateName, new ArrayList<>(recordedMovements));
        templates.add(template);
        recordedMovements.clear();
        repaint();
    }

    private void openTemplateManagerWindow() {
        TemplateManagerWindow templateManagerWindow = new TemplateManagerWindow(templates);
        templateManagerWindow.setVisible(true);
    }

    public int classifyInput() throws IOException {
        if (templates.isEmpty()) {
            System.out.println("Es wurden noch keine Templates gespeichert.");
            return 0;
        }
        List<Point> freeInputPoint = new ArrayList<>(recordedMovements);
        List<double[]> freeInput = CSVFileManager.convertPointListToDoubleArray(freeInputPoint);
        double minDistance = Double.MAX_VALUE;
        int classifiedIndex = -1;

        for (int i = 0; i < templates.size(); i++) {
            List<Point> templatePoints = templates.get(i).getPoints();
            String templateName = templates.get(i).getName();
            String filePathForTemplates = templateName + ".csv";
            String filePathForInput = "Input.csv";

            // Erstellen eines CSVFileManager-Objekts
            CSVFileManager filePathTemplates = new CSVFileManager(filePathForTemplates);
            CSVFileManager filePathInput = new CSVFileManager(filePathForInput);

            // Erstellen der CSV-Datei
            filePathTemplates.createCSVFile();
            filePathInput.createCSVFile();

            List<double[]> templatesDouble = CSVFileManager.convertPointListToDoubleArray(templatePoints);

            filePathTemplates.appendDataToCSV(templatesDouble);
            filePathInput.appendDataToCSV(freeInput);

            TimeSeries timeSeriesTemplates = new TimeSeries(filePathForTemplates, false, false, ',');
            TimeSeries timeSeriesFreeInput = new TimeSeries(filePathForInput, false, false, ',');

            // Create Euclidean Distance
            final DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");
            // Perform Fast DTW
            final TimeWarpInfo info = com.dtw.FastDTW.getWarpInfoBetween(timeSeriesFreeInput, timeSeriesTemplates, 100, distFn);
            double distance = info.getDistance();
            System.out.println(info.getDistance());

            if (distance < minDistance) {
                minDistance = distance;
                classifiedIndex = i;
            }


        if (classifiedIndex != -1) {
            System.out.println("Geste erkannt! Klassifiziert als Template " + templates.get(classifiedIndex).getName());
        } else {
            System.out.println("Geste nicht erkannt!");
        }
        filePathTemplates.deleteCSVFile();
        filePathInput.deleteCSVFile();
    }
        int klassifikation = Integer.parseInt(templates.get(classifiedIndex).getName());
        return klassifikation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Zeichnen der aufgezeichneten Mausbewegungen
        g.setColor(Color.RED);
        for (int i = 1; i < recordedMovements.size(); i++) {
            Point prevPoint = recordedMovements.get(i - 1);
            Point currentPoint = recordedMovements.get(i);
            g.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
        }

        // Zeichnen der ausgewählten gespeicherten Zeichnung
        if (recognitionEnabled) {
            g.drawImage(canvas, 0, 0, null);
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (!recognitionEnabled) {
            recordedMovements.add(e.getPoint());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
