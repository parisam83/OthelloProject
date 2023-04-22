package view;

import controller.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (Controller.getGridWidth() / Controller.getRowCount());
        int y = e.getY() / (Controller.getGridHeight() / Controller.getColCount());

        Controller.getInstance().addPieceRequest(x, y);
    }
}
