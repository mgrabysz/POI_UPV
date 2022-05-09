/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
        controller.getColorPicker().setValue((Color)this.getFill());
    }
    
    public void distinguish() {
        this.setRadius(Settings.RADIUS_BIG);
        this.setCursor(Cursor.HAND);
    }
    
    public void unselect() {
        this.setRadius(radiusNormal);
        this.setCursor(Cursor.DEFAULT);
    }
    
    public void initializeHandlers() {
        
        // event handler for clicking on point
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    ActionChangeColor action = new ActionChangeColor(controller, (Color)Point.this.getFill(), Point.this);
                    controller.saveAction(action);
                    Point.this.setFill(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    Point.this.select();
                } else if (controller.tool == Tool.DELETE) {    // deleting mode
                    ActionDeleteMark action = new ActionDeleteMark(controller, Point.this);
                    controller.saveAction(action);
                    controller.getZoomGroup().getChildren().remove((Node)e.getSource());
                }
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Point.this.distinguish();
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Point.this.unselect();
                
                
            }
        };
        
        //Registering the event filters
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    }
}
