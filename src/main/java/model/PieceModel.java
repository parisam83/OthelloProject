package model;

import java.awt.*;

public class PieceModel {
    private final int pieceId;
    private Color color;

    public PieceModel(int pieceId, Color color){
        this.pieceId = pieceId;
        this.color = color;
    }

    public void flipColor(){
        color = (color == Color.BLACK ? Color.WHITE : Color.BLACK);
    }
    public Color getColor() {
        return color;
    }
    public int getPieceId() {
        return pieceId;
    }
}