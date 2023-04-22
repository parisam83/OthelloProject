import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Piece {
    final int x, y;
    boolean color;

    public Piece(int x, int y, boolean color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}

class MyPanel extends JPanel {
    private static MyPanel instance;
    public int rowCount = 8, colCount = 8;
    private boolean playerTurn = false;
    ArrayList<Piece> pieces = new ArrayList<>();

    public static MyPanel getInstance() {
        return instance;
    }

    public MyPanel() {
        instance = this;
        setLayout(null);
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                MyPanel panel = MyPanel.getInstance();
                int windowWidth = panel.getWidth() - 5;
                int windowHeight = panel.getHeight() - 20;
                int x = me.getX() / (windowWidth / rowCount);
                int y = me.getY() / (windowHeight / colCount);
                if (x >= rowCount || y >= rowCount) {
                    return;
                }
                boolean doesFlip = false;
                Piece[][] board = panel.getCellState();
                if (board[x][y] != null) {
                    return;
                }
                int[] dx = {1, 1, -1, -1, 0, 0, 1, -1};
                int[] dy = {-1, 1, 1, -1, 1, -1, 0, 0};
                mainLoop:
                for (int k = 0; k < dx.length; k++) {
                    ArrayList<Piece> flipRow = new ArrayList<>();
                    for (int i = 1; i <= Math.max(rowCount, colCount); i++) {
                        int nx = x + i * dx[k], ny = y + i * dy[k];
                        if (nx >= colCount || nx < 0 || ny >= rowCount || ny < 0 || board[nx][ny] == null)
                            continue mainLoop;
                        if (board[nx][ny].color == playerTurn)
                            break;
                        flipRow.add(board[nx][ny]);
                    }
                    for (Piece piece : flipRow) {
                        doesFlip = true;
                        piece.color = playerTurn;
                    }
                }
                if (doesFlip) {
                    panel.pieces.add(new Piece(x, y, playerTurn));
                }
                if (!doesFlip)
                    return;
                playerTurn = !playerTurn;
                if (!panel.playerHasMove(playerTurn))
                    playerTurn = !playerTurn;
            }
        });
    }

    public Piece[][] getCellState() {
        Piece[][] board = new Piece[rowCount][colCount];
        for (Piece piece : this.pieces) {
            board[piece.x][piece.y] = piece;
        }
        return board;
    }

    public boolean playerHasMove(boolean player) {
        for (int i = 0; i < rowCount; i++)
            for (int j = 0; j < colCount; j++) {
                Piece[][] board = this.getCellState();
                if (board[i][j] != null) {
                    continue;
                }
                int[] dx = {1, 1, -1, -1, 0, 0, 1, -1};
                int[] dy = {-1, 1, 1, -1, 1, -1, 0, 0};
                boolean doesFlip = false;
                mainLoop:
                for (int k = 0; k < dx.length; k++) {
                    ArrayList<Piece> flipRow = new ArrayList<>();
                    for (int l = 1; l <= Math.max(rowCount, colCount); l++) {
                        int nx = i + l * dx[k], ny = j + l * dy[k];
                        if (nx >= colCount || nx < 0 || ny >= rowCount || ny < 0 || board[nx][ny] == null)
                            continue mainLoop;
                        if (board[nx][ny].color == player)
                            break;
                        flipRow.add(board[nx][ny]);
                    }
                    doesFlip = !flipRow.isEmpty();
                }
                if (doesFlip)
                    return true;
            }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(0, 106, 65));

        g.setColor(Color.WHITE);
        int windowHeight = this.getHeight() - 20;
        if (!(this.playerHasMove(false) || this.playerHasMove(true))) {
            int p1Score = 0;
            int p2Score = 0;
            for (Piece piece : pieces) {
                p1Score += piece.color ? 0 : 1;
                p2Score += piece.color ? 1 : 0;
            }
            String gameEndStr = "";
            if (p1Score > p2Score)
                gameEndStr = "Winner: Player 1";
            else if (p2Score > p1Score)
                gameEndStr = "Winner: Player 2";
            else
                gameEndStr = "Tie";
            g.drawString(gameEndStr, 0, windowHeight + 15);
        } else {
            g.drawString("Player turn: " + (this.playerTurn ? "white" : "black"), 0, windowHeight + 15);
        }
        int windowWidth = this.getWidth() - 5;
        for (int i = 0; i <= rowCount; i++) {
            g.drawLine(0, i * windowHeight / rowCount, windowWidth, i * windowHeight / rowCount);
            g.drawLine(i * windowWidth / colCount, 0, i * windowWidth / rowCount, windowHeight);
        }
        int pieceSize = Math.min(windowWidth / colCount, windowHeight / rowCount) / 2;
        for (Piece piece : this.pieces) {
            int x = piece.x * (windowWidth / colCount) + (windowWidth / colCount) / 2 - pieceSize / 2;
            int y = piece.y * (windowHeight / rowCount) + (windowHeight / rowCount) / 2 - pieceSize / 2;
            g.setColor(piece.color ? Color.WHITE : Color.BLACK);
            g.drawOval(x, y, pieceSize, pieceSize);
            g.fillOval(x, y, pieceSize, pieceSize);
        }
    }
}


public class DirtyMain {
    static JFrame frame;

    public static void main(String[] args) throws InterruptedException {
        frame = new JFrame();
        MyPanel panel = new MyPanel();

        int screenWidth, screenHeight, windowWidth, windowHeight;
        screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        windowWidth = 500;
        windowHeight = 500;
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(windowWidth, windowHeight));
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        frame.setVisible(true);
        frame.setContentPane(panel);

        startGame();
    }

    public static void startGame() throws InterruptedException {
        MyPanel panel = MyPanel.getInstance();
        panel.pieces.add(new Piece(3, 3, false));
        panel.pieces.add(new Piece(4, 4, false));
        panel.pieces.add(new Piece(3, 4, true));
        panel.pieces.add(new Piece(4, 3, true));

        while (true) {
            Thread.sleep(1000 / 60);
            frame.repaint();
            frame.revalidate();
            if (!(panel.playerHasMove(false) || panel.playerHasMove(true))) {
                JOptionPane.showMessageDialog(frame, "somebody win the game!");
                panel.pieces.clear();
                startGame();
            }
        }
    }
}