/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.acciones;

import poiupv.FXMLDocumentController;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import poiupv.marcas.CircleExtended;
import poiupv.marcas.LineExtended;
import poiupv.marcas.Point;
import poiupv.marcas.TextExtended;

/**
 *
 * @author margr
 */
public class ActionChangeColor extends Action {
    /**
     * Class stores action of changing color
     * Undo() causes reverting that change
     */
    
    private Color oldColor;
    private Node mark;
    
    public ActionChangeColor(FXMLDocumentController controller, Color color, Node node) {
        super(controller);
        this.oldColor = color;
        this.mark = node;
    }
    
    @Override
    public void undo() {
        if (mark instanceof Point) {
            Point point = (Point)mark;
            point.setFill(oldColor);
        } else if (mark instanceof LineExtended) {
            LineExtended line = (LineExtended)mark;
            line.setStroke(oldColor);
        } else if (mark instanceof CircleExtended) {
            CircleExtended circle = (CircleExtended)mark;
            circle.setStroke(oldColor);
        } else if (mark instanceof TextExtended) {
            TextExtended text = (TextExtended)mark;
            text.setFill(oldColor);
        }
    }
    
}
