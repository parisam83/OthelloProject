package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel{
    private int winnerId = -1;
    private static final int rowCount = Controller.getRowCount(), colCount = Controller.getColCount();
    private int gridWidth, gridHeight;
    private ArrayList<GraphicalPiece> graphicalPieces;
    private final GraphicalPiece[][] board = new GraphicalPiece[rowCount][colCount];
    public GamePanel(){
        this.setLayout(null);
        this.addMouseListener(new MouseListener());
    }

    public void createBoard(){
        for (GraphicalPiece graphicalPiece : graphicalPieces)
            board[graphicalPiece.getX()][graphicalPiece.getY()] = graphicalPiece;
    }

    public ArrayList<GraphicalPiece> getNeighbourFlips(int x, int y, Color color){
        int[] dx = {1, 1, -1, -1, 0, 0, 1, -1};
        int[] dy = {-1, 1, 1, -1, 1, -1, 0, 0};
        ArrayList<GraphicalPiece> neighbourFlips = new ArrayList<>();

        MainLoop:
        for (int k = 0; k < dx.length; k++) {
            ArrayList<GraphicalPiece> rowFlips = new ArrayList<>();
            for (int l = 1; l <= Math.max(rowCount, colCount); l++) {
                int nx = x + l * dx[k], ny = y + l * dy[k];
                if (nx >= colCount || nx < 0 || ny >= rowCount || ny < 0 || getGraphicalPiece(nx, ny) == null)
                    continue MainLoop;
                if (board[nx][ny].getColor() == color)
                    break;
                rowFlips.add(getGraphicalPiece(nx, ny));
            }
            neighbourFlips.addAll(rowFlips);
        }
        return neighbourFlips;
    }

    public void addGraphicalPiece(GraphicalPiece piece){
        graphicalPieces.add(piece);
    }
    public GraphicalPiece getGraphicalPiece(int x, int y){
        createBoard();
        return board[x][y];
    }
    public void setGraphicalPieces(ArrayList<GraphicalPiece> graphicalPieces) {
        this.graphicalPieces = graphicalPieces;
    }
    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(0, 106, 65));

        g.setColor(Color.WHITE);
        gridWidth = this.getWidth() - 5;
        gridHeight = this.getHeight() - 20;
        Controller.setGridSize(gridWidth, gridHeight);
        drawGameTurn(g);
        drawGrid(g);
        drawPieces(g);
    }

    public void drawGameTurn(Graphics g) {
        String gameTurn = "";
        if (winnerId == -1) gameTurn = "Player turn: " + (Controller.getInstance().getCurrentPlayerColor() == Color.WHITE ? "white" : "black");
        else if (winnerId == 0) gameTurn = "Tie";
        else gameTurn = "Winner: Player " + winnerId;

        g.drawString(gameTurn, 0, gridHeight + 15);
    }

    public void drawGrid(Graphics g){
        for (int i = 0; i <= rowCount; i++){
            g.drawLine(0, i * gridHeight / rowCount, gridWidth, i * gridHeight / rowCount);
            g.drawLine(i * gridWidth / colCount, 0, i * gridWidth / rowCount, gridHeight);
        }
    }

    private void drawPieces(Graphics g) {
        int pieceSize = Math.min(gridWidth / colCount, gridHeight / rowCount) / 2;
        for (GraphicalPiece piece : graphicalPieces){
            int x = piece.getX() * (gridWidth / colCount) + (gridWidth / colCount) / 2 - pieceSize / 2;
            int y = piece.getY() * (gridHeight / rowCount) + (gridHeight / rowCount) / 2 - pieceSize / 2;
            g.setColor(piece.getColor());
            g.drawOval(x, y, pieceSize, pieceSize);
            g.fillOval(x, y, pieceSize, pieceSize);
        }
    }
}
