/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.marcas;

import poiupv.acciones.ActionChangeColor;
import poiupv.acciones.ActionDeleteMark;
import poiupv.FXMLDocumentController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import poiupv.constantes.Tool;

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
        this.setCursor(Cursor.HAND);
    }
    public void unselect() {
        /**
         * Also undistinguish
         */
        this.setStrokeWidth(widthNormal);
        this.setCursor(Cursor.DEFAULT);
    }
    
    public void initializeHandlers() {
        
        // event handler for clicking on circle
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    ActionChangeColor action = new ActionChangeColor(controller, (Color)CircleExtended.this.getStroke(), CircleExtended.this);
                    controller.saveAction(action);
                    CircleExtended.this.setStroke(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    controller.getColorPicker().setValue((Color)CircleExtended.this.getStroke());
                } else if (controller.tool == Tool.DELETE) {    // deleting mode
                    ActionDeleteMark action = new ActionDeleteMark(controller, CircleExtended.this);
                    controller.saveAction(action);
                    controller.getZoomGroup().getChildren().remove((Node)e.getSource());
                }
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (controller.tool == Tool.SELECTION || controller.tool == Tool.CHANGE_COLOR || controller.tool == Tool.DELETE) {    // selection || change_color
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
