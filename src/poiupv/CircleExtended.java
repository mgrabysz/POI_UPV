/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author margr
 */
public class CircleExtended extends Circle {
    
    private final double widthNormal, widthBig, initialX;
    private final FXMLDocumentController controller;
    
    public CircleExtended(double x, double y, double widthNormal, double widthBig, FXMLDocumentController controller){
        super(1);
        this.setCenterX(x);
        this.setCenterY(y);
        this.initialX = x;
        this.controller = controller;
        this.widthBig = widthBig;
        this.widthNormal = widthNormal;
        this.setStrokeWidth(widthNormal);
        this.setStroke(controller.getColorPicker().getValue());
        this.setFill(Color.TRANSPARENT);
    }
    
    public double getInitialX() {
        return initialX;
    }

}
