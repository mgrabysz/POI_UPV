/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author margr
 */
public class TextFieldExtended extends TextField {
    
    private final FXMLDocumentController controller;

    public TextFieldExtended(double x, double y, FXMLDocumentController controller) {
        super();
        this.controller = controller;
        this.setLayoutX(x);
        this.setLayoutY(y);
        
        this.setOnAction(e -> {
            // Enter pressed
            // Focus is lost automatically while deleting object triggering Listener
            controller.getZoomGroup().getChildren().remove(this);
            controller.setDrawingMark(null);
            e.consume();
        });
        
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Creating new text 
                Text text = new TextExtended(this.getLayoutX(), this.getLayoutY(), this.getText(), controller);
                ActionNewMark action = new ActionNewMark(controller, text);
                this.controller.saveAction(action);
                controller.getZoomGroup().getChildren().add(text);
                controller.getZoomGroup().getChildren().remove(this);
            }
        });
    }
}
