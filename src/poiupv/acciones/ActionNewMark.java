/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv.acciones;

import poiupv.FXMLDocumentController;
import javafx.scene.Node;

/**
 *
 * @author margr
 */
public class ActionNewMark extends Action {
    /**
     * Class stores action of adding new mark (point, circle, line, text)
     * Undo() causes deleting this mark
     */

    private Node mark;
    
    public ActionNewMark(FXMLDocumentController controller, Node node){
        super(controller);
        this.mark = node;
        
    }
    @Override
    public void undo() {
        controller.getZoomGroup().getChildren().remove(mark);
    }
    
}
