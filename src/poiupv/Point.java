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
public class Point extends Circle {
    
    /**
     * Class represents point which can be drawn on a map
     * Attributes
     * radiusBig : double
     *      radius when point is selected
     * radiusNormal : double
     *      radius when point is not selected
     * controller : FXMLDocumentController
     *      controller of window to which point belongs
     */
    
    private double radiusBig, radiusNormal;
    private FXMLDocumentController controller;
    
    public Point(double radiusNormal, double radiusBig, FXMLDocumentController controller) {
        super(radiusNormal);
        this.controller = controller;
        this.radiusNormal = radiusNormal;
        this.radiusBig = radiusBig;
        
        this.setStroke(Color.TRANSPARENT);
        this.setStrokeWidth(3);
        this.setFill(controller.getColorPicker().getValue());
        this.setMouseTransparent(true);
    }
    
    public void select() {
        this.setStroke(Color.ORANGE);
        this.setRadius(Settings.RADIUS_BIG);
        controller.getColorPicker().setValue((Color)this.getFill());
    }
    
    public void unselect() {
        this.setStroke(Color.TRANSPARENT);
        this.setRadius(radiusNormal);
    }
    
    public void initializeHandlers() {
        
        // event handler for clicking on point
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    Point.this.setFill(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    
                    // unselect previously selected mark
                    if (controller.getSelectedMark() instanceof Point) {
                        Point selectedPoint = (Point)controller.getSelectedMark();
                        selectedPoint.unselect();
                    }

                    // select new
                    controller.setSelectedMark(Point.this);
                    Point.this.select();
                    
                } 
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Point.this.setRadius(Settings.RADIUS_BIG);
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (Point.this != controller.getSelectedMark()) {
                    Point.this.setRadius(Settings.RADIUS_NORMAL);
                }
                
            }
        };
        
        //Registering the event filters
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    }
}
