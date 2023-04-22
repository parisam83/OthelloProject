package controller;

import Loaders.ConfigLoader;
import model.Game;
import model.PieceModel;
import view.GamePanel;
import view.MainFrame;
import view.GraphicalPiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Player 1: White
// Player 2: Black
public class Controller {
    private char playerTurn = '2';
    private boolean gameFinished = false;
    private final MainFrame mainFrame;
    private final GamePanel gamePanel = new GamePanel();
    private final Game game = new Game();
    private static int gridWidth, gridHeight;
    private static final int rowCount = ConfigLoader.getInstance().getProperty(Integer.class, "rowCount");
    private static final int colCount = ConfigLoader.getInstance().getProperty(Integer.class, "colCount");
    private int lastPieceId = 0;
    private GraphicalPiece pieceToAdd;
    private static Controller instance = new Controller(new MainFrame().getInstance());

    public static Controller getInstance(){
        return instance;
    }

    public Controller(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        arrangeInitialPieces();
        this.mainFrame.setContentPane(gamePanel);
    }

    public void arrangeInitialPieces(){
        ArrayList<GraphicalPiece> graphicalPieces = new ArrayList<>();
        ArrayList<PieceModel> pieceModels = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String pieceName = "piece" + i;
            List<String> pieceConfig = ConfigLoader.getInstance().getPropertyList(String.class, pieceName);
            lastPieceId++;
            Color pieceColor = pieceConfig.get(2).equals("Black") ? Color.BLACK : Color.WHITE;
            pieceModels.add(new PieceModel(lastPieceId, pieceColor));
            graphicalPieces.add(new GraphicalPiece(lastPieceId, Integer.parseInt(pieceConfig.get(0)), Integer.parseInt(pieceConfig.get(1)), pieceColor));
        }
        game.setPieceModels(pieceModels);
        gamePanel.setGraphicalPieces(graphicalPieces);
    }

    public void run() {
        while (!gameFinished){
            sleep();
            checkRequests();
            updateGraphics();
            checkGameStatus();
        }
    }

    public void sleep(){
        try {
            Thread.sleep(1000 / 60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkRequests(){
        if (pieceToAdd == null) return;
        int x = pieceToAdd.getX(), y = pieceToAdd.getY();

        if (x >= rowCount || y >= rowCount) return;
        if (gamePanel.getGraphicalPiece(x, y) != null) return;
        ArrayList<GraphicalPiece> flips = gamePanel.getNeighbourFlips(x, y, getCurrentPlayerColor());
        if (!flips.isEmpty()) {
            for (GraphicalPiece piece : flips) {
                piece.flipColor();
                game.findPiece(piece.getPieceId()).flipColor();
            }

            lastPieceId++;
            game.addPieceModel(new PieceModel(lastPieceId, pieceToAdd.getColor()));
            gamePanel.addGraphicalPiece(pieceToAdd);
            swapTurn();
            if (!playerHasMove(getCurrentPlayerColor()))
                swapTurn();
        }
        pieceToAdd = null;
    }

    public void updateGraphics(){
        mainFrame.repaint();
        mainFrame.revalidate();
    }
    public void checkGameStatus(){
        if (!(playerHasMove(Color.WHITE) || playerHasMove(Color.BLACK))){
            gameFinished = true;
            int score[] = {0, 0};
            for (PieceModel pieceModel : game.getPieceModels())
                score[pieceModel.getColor() == Color.WHITE ? 0 : 1]++;

            if (score[0] == score[1]) gamePanel.setWinnerId(0);
            else if (score[0] > score[1]) gamePanel.setWinnerId(1);
            else gamePanel.setWinnerId(2);

            JOptionPane.showMessageDialog(mainFrame, "somebody win the game!");
            resetGame();
        }
    }

    public void resetGame(){
        instance = new Controller(mainFrame);
        instance.run();
    }
    public boolean playerHasMove(Color colorOfPlayer){
        for (int x = 0; x < rowCount; x++)
            for (int y = 0; y < colCount; y++)
                if (gamePanel.getGraphicalPiece(x, y) == null && !gamePanel.getNeighbourFlips(x, y, colorOfPlayer).isEmpty())
                    return true;
        return false;
    }

    public void addPieceRequest(int x, int y){
        pieceToAdd = new GraphicalPiece(lastPieceId + 1, x, y, getCurrentPlayerColor());
    }

    public void swapTurn(){
        playerTurn = (playerTurn == '1' ? '2' : '1');
    }

    public Color getOtherPlayerColor(){
        return playerTurn == '1' ? Color.BLACK : Color.WHITE;
    }
    public Color getCurrentPlayerColor() {
        return playerTurn == '1' ? Color.WHITE : Color.BLACK;
    }

    public static int getRowCount() {
        return rowCount;
    }

    public static int getColCount() {
        return colCount;
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    public static int getGridHeight() {
        return gridHeight;
    }
    public static void setGridSize(int gridWidth, int gridHeight) {
        Controller.gridWidth = gridWidth;
        Controller.gridHeight = gridHeight;
    }
}