/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
        this.setMouseTransparent(true);
    }
    
    public double getInitialX() {
        return initialX;
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
        
        // event handler for clicking on circle
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    CircleExtended.this.setStroke(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    
                    // unselect previously selected mark
                    if (controller.getSelectedMark() instanceof Point) {
                        Point selectedPoint = (Point)controller.getSelectedMark();
                        selectedPoint.unselect();
                    }

                    // select new
                    controller.getColorPicker().setValue((Color)CircleExtended.this.getStroke());
                }
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (controller.tool == Tool.SELECTION || controller.tool == Tool.CHANGE_COLOR) {    // selection || change_color
                    CircleExtended.this.distinguish();
                }
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                CircleExtended.this.unselect();
            }
        };
        
        //Registering the event filters
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    
    }

}
