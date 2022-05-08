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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author margr
 */
public class TextExtended extends Text {
    
    private final FXMLDocumentController controller;
    double size;

    public TextExtended(double x, double y, String text, FXMLDocumentController controller) {
        super(text);
        this.controller = controller;
        this.size = (double)controller.getGrosorSpinner().getValue();
        this.setX(x);
        this.setY(y);
        this.setFill(controller.getColorPicker().getValue());
        this.setFont(Font.font("Gafata", FontWeight.NORMAL, size));
        this.initializeHandlers();
    }
    
    public void distinguish() {
        /**
         * Makes object a little bit bigger when mouse hovers
         */
        this.setFont(Font.font("Gafata", FontWeight.BOLD, size));
        this.setCursor(Cursor.HAND);
    }
    public void unselect() {
        /**
         * Also undistinguish
         */
        this.setFont(Font.font("Gafata", FontWeight.NORMAL, size));
        this.setCursor(Cursor.DEFAULT);
    }
    
    public void initializeHandlers() {
        // event handler for clicking on circle
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                if (controller.tool == Tool.CHANGE_COLOR){   // mode of color changing, no selection
                    TextExtended.this.setFill(controller.getColorPicker().getValue());
                } else if (controller.tool == Tool.SELECTION) {    // selection
                    controller.getColorPicker().setValue((Color)TextExtended.this.getFill());
                } else if (controller.tool == Tool.DELETE) {    // deleting mode
                    controller.getZoomGroup().getChildren().remove((Node)e.getSource());
                }
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (controller.tool == Tool.SELECTION || controller.tool == Tool.CHANGE_COLOR || controller.tool == Tool.DELETE) {    // selection || change_color
                    TextExtended.this.distinguish();
                }
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                TextExtended.this.unselect();
            }
        };
        
        //Registering the event filters
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    }
}
