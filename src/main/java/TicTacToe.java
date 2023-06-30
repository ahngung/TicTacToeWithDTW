import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class TicTacToe implements ActionListener {
    private static final JFrame gameFrame = new JFrame(); // JFrame for the game window
    private JPanel titlePanel = new JPanel(); // JPanel for the Tic Tac Toe grid
    private MouseMovementRecorder mousePanel = new MouseMovementRecorder(); // JPanel for the Mouse Panel
    public static JLabel title = new JLabel(); // JLabel to display the game title and the current turn
    public static JButton[] gridCells = new JButton[9]; // Array of JButtons for each grid cell
    public static JButton undoButton = new JButton("Undo");
    private static final Random random = new Random(); // Random object to decide the starting player
    public static int turnCounter = 0;
    public static boolean isPlayer1Turn; // Flag to indicate the current player's turn
    public static List<Integer> moveHistory = new ArrayList<>();


    // Creating class constructor for creating grid
    public TicTacToe() {
        // Background setting
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(1200, 800);
        gameFrame.getContentPane().setBackground(new Color(114, 78, 21));
        gameFrame.setTitle("Tic Tac Toe");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setVisible(true);

        // Title setting
        title.setBackground(new Color(114, 78, 21));
        title.setForeground(new Color(63, 37, 0));
        title.setFont(new Font("Roboto", Font.BOLD, 90));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setText("Tic Tac Toe");
        title.setOpaque(true);

        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBounds(0, 0, 800, 100);

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoMove(); // Rufe die Methode zum Rückgängig machen des Zugs auf
            }
        });

        // Create Button layout (3*3)
        // JPanel for the bottom of the game window
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3,3));
        buttonPanel.setBackground(new Color(114, 78, 21));

        // Add buttons into the layout

        for (int i = 0; i < 9; i++)
        {
            gridCells[i] = new JButton();
            buttonPanel.add(gridCells[i]);
            gridCells[i].setFont(new Font("Roboto", Font.BOLD, 150));
            gridCells[i].setFocusable(false);
            gridCells[i].addActionListener(this);
            gridCells[i].setBackground(new Color(114, 78, 21));
            gridCells[i].setOpaque(true);
        }

        mousePanel.setPreferredSize(new Dimension(600, 100));
        mousePanel.setOpaque(true);
        mousePanel.setBackground(new Color(128, 128, 128));


        //add the parts of the game together in the gameFrame
        titlePanel.add(title);
        gameFrame.add(titlePanel, BorderLayout.NORTH);
        gameFrame.add(buttonPanel);
        gameFrame.add(mousePanel, BorderLayout.EAST);

        selectStartingPlayer();
    }

    //randomly chooses a starting player for every new game
    public static void selectStartingPlayer()
    {
        int startingPlayer=random.nextInt(10);

        if (startingPlayer%2 == 0)
        {
            isPlayer1Turn = true;
            title.setText("Player X begins");
        }
        else
        {
            isPlayer1Turn = false;
            title.setText("Player 0 begins");
        }
    }

    // Method to end the game and display a dialog for choosing to restart odr exit the game
    public static void end(String s) throws IOException {
        turnCounter = 0;
        Object[] option={"Restart","Exit"};
        int x=JOptionPane.showOptionDialog(gameFrame, "Game Over\n"+s,"Game Over", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if(x==0)
        {
            // Restart the game by resetting the grid and other variables
            for (int i = 0; i < 9; i++) {
                gridCells[i].setText("");
                gridCells[i].setEnabled(true);
                gridCells[i].setBackground(new Color(114, 78, 21));
            }
            moveHistory.clear();
            selectStartingPlayer();
        }
        else {
            System.exit(0);
        }
        gameFrame.setVisible(true); // Make the game frame visible again
    }



    // Creating method for checking winning conditions
    public static void checkForWinner() throws IOException {
        if ((gridCells[0].getText() == "X") && (gridCells[1].getText() == "X") && (gridCells[2].getText() == "X"))
        {
            xWins(0, 1, 2);
        }
        else if ((gridCells[0].getText() == "X") && (gridCells[4].getText() == "X") && (gridCells[8].getText() == "X"))
        {
            xWins(0, 4, 8);
        }
        else if ((gridCells[0].getText() == "X") && (gridCells[3].getText() == "X") && (gridCells[6].getText() == "X"))
        {
            xWins(0, 3, 6);
        }
        else if ((gridCells[1].getText() == "X") && (gridCells[4].getText() == "X") && (gridCells[7].getText() == "X"))
        {
            xWins(1, 4, 7);
        }
        else if ((gridCells[2].getText() == "X") && (gridCells[4].getText() == "X") && (gridCells[6].getText() == "X"))
        {
            xWins(2, 4, 6);
        }
        else if ((gridCells[2].getText() == "X") && (gridCells[5].getText() == "X") && (gridCells[8].getText() == "X"))
        {
            xWins(2, 5, 8);
        }
        else if ((gridCells[3].getText() == "X") && (gridCells[4].getText() == "X") && (gridCells[5].getText() == "X"))
        {
            xWins(3, 4, 5);
        }
        else if ((gridCells[6].getText() == "X") && (gridCells[7].getText() == "X") && (gridCells[8].getText() == "X"))
        {
            xWins(6, 7, 8);
        }

        else if ((gridCells[0].getText() == "O") && (gridCells[1].getText() == "O") && (gridCells[2].getText() == "O"))
        {
            oWins(0, 1, 2);
        }
        else if ((gridCells[0].getText() == "O") && (gridCells[3].getText() == "O") && (gridCells[6].getText() == "O"))
        {
            oWins(0, 3, 6);
        }
        else if ((gridCells[0].getText() == "O") && (gridCells[4].getText() == "O") && (gridCells[8].getText() == "O"))
        {
            oWins(0, 4, 8);
        }
        else if ((gridCells[1].getText() == "O") && (gridCells[4].getText() == "O") && (gridCells[7].getText() == "O"))
        {
            oWins(1, 4, 7);
        }
        else if ((gridCells[2].getText() == "O") && (gridCells[4].getText() == "O") && (gridCells[6].getText() == "O"))
        {
            oWins(2, 4, 6);
        }
        else if ((gridCells[2].getText() == "O") && (gridCells[5].getText() == "O") && (gridCells[8].getText() == "O"))
        {
            oWins(2, 5, 8);
        }
        else if ((gridCells[3].getText() == "O") && (gridCells[4].getText() == "O") && (gridCells[5].getText() == "O"))
        {
            oWins(3, 4, 5);
        } else if ((gridCells[6].getText() == "O") && (gridCells[7].getText() == "O") && (gridCells[8].getText() == "O"))
        {
            oWins(6, 7, 8);
        }
        else if(turnCounter==9)
        {
            title.setText("Draw!!");
            end("Draw!!");
        }
    }

    //handler for the situation when O wins
    public static void oWins(int oCell1, int oCell2, int oCell3) throws IOException {
        gridCells[oCell1].setBackground(new Color(191, 51, 201));
        gridCells[oCell2].setBackground(new Color(191, 51, 201));
        gridCells[oCell3].setBackground(new Color(191, 51, 201));

        for (int i = 0; i < 9; i++)
        {
            gridCells[i].setEnabled(false);
        }
        title.setText("Player O Wins");
        end("Player O Wins");
    }

    //handler for the situation when X wins
    public static void xWins(int xCell1, int xCell2, int xCell3) throws IOException {
        gridCells[xCell1].setBackground(new Color(191, 51, 201));
        gridCells[xCell2].setBackground(new Color(191, 51, 201));
        gridCells[xCell3].setBackground(new Color(191, 51, 201));

        for (int i = 0; i < 9; i++)
        {
            gridCells[i].setEnabled(false);
        }
        title.setText("Player X wins");
        end("Player X Wins");
    }

    public static void undoMove() {
        if (!moveHistory.isEmpty()) {
            int lastMoveIndex = moveHistory.get(moveHistory.size() - 1);
            gridCells[lastMoveIndex].setText(""); // Setze das Feld zurück
            moveHistory.remove(moveHistory.size() - 1); // Entferne den Zug aus der moveHistory
            turnCounter--; // Reduziere den Zähler der Spielzüge um eins
            isPlayer1Turn = !isPlayer1Turn; // Wechsle den aktuellen Spieler
            title.setText(isPlayer1Turn ? "X's turn" : "O's turn"); // Aktualisiere die Anzeige für den aktuellen Spieler
        }
    }

    //handler for every action performed
    @Override
    public void actionPerformed(ActionEvent event)
    {
        for (int i = 0; i < 9; i++)
        {
            if (event.getSource() == gridCells[i])
            {
                if (isPlayer1Turn)
                {
                    if (gridCells[i].getText() == "")
                    {
                        gridCells[i].setForeground(new Color(252, 0, 0));
                        gridCells[i].setText("X");
                        isPlayer1Turn = false;
                        title.setText("O's turn");
                        turnCounter++;
                        try {
                            checkForWinner();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                //if gridCell is already filled do nothing
                else
                {
                    if (gridCells[i].getText() == "")
                    {
                        gridCells[i].setForeground(new Color(9, 58, 255));
                        gridCells[i].setText("O");
                        isPlayer1Turn = true;
                        title.setText("X's turn");
                        turnCounter++;
                        try {
                            checkForWinner();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                moveHistory.add(i);
            }
        }
    }
}

