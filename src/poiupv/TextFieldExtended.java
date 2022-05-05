/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.control.TextField;
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
            Text text = new Text(this.getText());
            text.setX(this.getLayoutX());
            text.setY(this.getLayoutY());
            text.setStyle("-fx-font-family: Gafata; -fx-font-size: 40;");
            controller.getZoomGroup().getChildren().add(text);
            controller.getZoomGroup().getChildren().remove(this);
            e.consume();
        });
        
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Text text = new Text(this.getText());
                text.setX(this.getLayoutX());
                text.setY(this.getLayoutY());
                text.setStyle("-fx-font-family: Gafata; -fx-font-size: 40;");
                controller.getZoomGroup().getChildren().add(text);
                controller.getZoomGroup().getChildren().remove(this);
            }
        });
    }
}
