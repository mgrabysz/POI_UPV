/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author margr
 */
public class Point extends Circle {
    
    /**
     * Class represents point which can be drawn on a map
     * Attributes
     * radiusBig : double
     *      radius when point is selected
     * radiusNormal : double
     *      radius when point is not selected
     */
    
    private double radiusBig, radiusNormal;
    
    public Point(double radiusNormal, double radiusBig) {
        super(radiusNormal);
        this.radiusNormal = radiusNormal;
        this.radiusBig = radiusBig;
    }
    
    public void select(ColorPicker picker) {
        this.setStroke(Color.ORANGE);
        this.setRadius(Settings.RADIUS_BIG);
        picker.setValue((Color)this.getFill());
    }
    
    public void unselect() {
        this.setStroke(Color.TRANSPARENT);
        this.setRadius(radiusNormal);
    }
}
