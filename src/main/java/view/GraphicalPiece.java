package view;

import java.awt.*;

public class GraphicalPiece implements Drawable{
    private final int pieceId;
    private final int x, y;
    private Color color;

    public GraphicalPiece(int pieceId, int x, int y, Color color){
        this.pieceId = pieceId;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public void flipColor(){
        color = (color == Color.BLACK ? Color.WHITE : Color.BLACK);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Color getColor() {
        return color;
    }
    public int getPieceId() {
        return pieceId;
    }
}
