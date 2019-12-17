import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel {
    private static final int DIMENSIONS = Main.DIMENSIONS;
    private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    static JFrame frame = new JFrame("Kare Bulmaca");
    static JPanel pnlBottom = new JPanel();
    static JButton btnSolve = new JButton("Coz");
    static JPanel crossword = new JPanel(new GridLayout(15, 15, 1, 1));

    public GUI() {
        setLayout(new BorderLayout());

        add(crossword, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.PAGE_END);
    }

    private static void prepareCrosswordArea(String[] gridGuide) {
        JLabel[][] grid = new JLabel[15][15];
        for (int row = 0; row < DIMENSIONS - 1; row++) {
            for (int col = 0; col < DIMENSIONS - 1; col++) {
                grid[row][col] = new JLabel(" ", SwingConstants.CENTER);
                grid[row][col].setFont(LABEL_FONT);
                grid[row][col].setOpaque(true);

                if (gridGuide[row].charAt(col) == '#') {
                    grid[row][col].setBackground(Color.BLACK);
                } else {
                    grid[row][col].setBackground(Color.WHITE);
                }
                crossword.add(grid[row][col]);
            }
        }
    }

    private static void showSolution(char[][] solution) {
        crossword.removeAll();
        JLabel[][] grid = new JLabel[15][15];
        for (int row = 0; row < DIMENSIONS - 1; row++) {
            for (int col = 0; col < DIMENSIONS - 1; col++) {
                grid[row][col] = new JLabel(" ", SwingConstants.CENTER);
                grid[row][col].setFont(LABEL_FONT);
                grid[row][col].setOpaque(true);

                if (solution[row][col] == '#') {
                    grid[row][col].setBackground(Color.BLACK);
                } else {
                    grid[row][col].setBackground(Color.WHITE);
                    grid[row][col] = new JLabel(solution[row][col] + "", SwingConstants.CENTER);
                }
                crossword.add(grid[row][col]);
            }
        }
        crossword.revalidate();
        crossword.repaint();
    }

    private static void createAndShowGui() {
        GUI mainPanel = new GUI();

        crossword.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        crossword.setPreferredSize(new Dimension(750, 750));
        prepareCrosswordArea(Main.grid);

        pnlBottom.add(btnSolve);
        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PuzzleSolver solve = new PuzzleSolver(Main.words, Main.grid);
                solve.solvePuzzle();
                showSolution(PuzzleSolver.solution);
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setResizable(false);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void invokeGUI() {
        SwingUtilities.invokeLater(() -> {
            GUI.createAndShowGui();
        });
    }
}