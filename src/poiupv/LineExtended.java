/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author margr
 */
public class LineExtended extends Line {
    
    private final double widthNormal, widthBig;
    private final FXMLDocumentController controller;
    
    public LineExtended(double x1, double y1, double x2, double y2, double widthNormal, double widthBig, FXMLDocumentController controller){
        super(x1, y1, x2, y2);
        this.widthNormal = widthNormal;
        this.widthBig = widthBig;
        this.controller = controller;
        this.setStrokeWidth(widthNormal);
        this.setStroke(controller.getColorPicker().getValue());
        this.setMouseTransparent(true);
    }
    
    public void select() {
        controller.getColorPicker().setValue((Color)this.getStroke());
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
    
    public void initializeHandlers() {
        
        // event handler for clicking on line
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    LineExtended.this.setStroke(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    
                    // unselect previously selected mark
                    if (controller.getSelectedMark() instanceof Point) {
                        Point selectedPoint = (Point)controller.getSelectedMark();
                        selectedPoint.unselect();
                    }

                    // select new
                    controller.getColorPicker().setValue((Color)LineExtended.this.getStroke());
                }
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (controller.tool == Tool.SELECTION || controller.tool == Tool.CHANGE_COLOR) {    // selection || change_color
                    LineExtended.this.distinguish();
                }
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                LineExtended.this.unselect();
            }
        };
        
        //Registering the event filters
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    
    }
    
    
    
}
