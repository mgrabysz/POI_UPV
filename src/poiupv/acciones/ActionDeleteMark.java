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
public class ActionDeleteMark extends Action {
    /**
     * Class stores action of deleting a mark
     * undo restores that mark
     */
    private Node mark;
    
    public ActionDeleteMark(FXMLDocumentController controller, Node node) {
        super(controller);
        this.mark = node;
    }
    
    @Override
    public void undo() {
        controller.getZoomGroup().getChildren().add(mark);
    }
    
}
