/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author margr
 */
public class TextExtended extends Text {
    
    private final FXMLDocumentController controller;

    public TextExtended(double x, double y, String text, FXMLDocumentController controller) {
        super(text);
        this.controller = controller;
        this.setX(x);
        this.setY(y);
        double size = (double)controller.getGrosorSpinner().getValue();
        this.setFont(Font.font("Gafata", size));
        
        
    }
}
