/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author margr
 */
public class LineExtended extends Line {
    
    private double widthNormal, widthBig;
    
    public LineExtended(double widthNormal, double widthBig){
        super();
        this.widthNormal = widthNormal;
        this.widthBig = widthBig;
    }
    
    public void select(ColorPicker picker, Spinner<Double> spinner) {
        picker.setValue((Color)this.getStroke());
    }
    
    public void distinguish() {
        /**
         * Makes object a little bit bigger when mouse hovers
         */
        this.setStrokeWidth(widthBig);
    }
    public void unselect() {
        /**
         * Also undistinguish
         */
        this.setStrokeWidth(widthNormal);
    }
}
