package model;

import java.util.ArrayList;

public class Game {
    private ArrayList<PieceModel> pieceModels;

    public void addPieceModel(PieceModel pieceModel){
        pieceModels.add(pieceModel);
    }
    public void setPieceModels(ArrayList<PieceModel> pieceModels) {
        this.pieceModels = pieceModels;
    }
    public ArrayList<PieceModel> getPieceModels() {
        return pieceModels;
    }
    public PieceModel findPiece(int pieceId) {
        for (PieceModel piece : pieceModels)
            if (piece.getPieceId() == pieceId)
                return piece;
        return null;
    }
}
