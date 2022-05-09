/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.shape.Line;

/**
 *
 * @author margr
 */
public class ActionNewExtreme extends Action {
    /**
     * Class stores action of adding new extremes mark
     * Extremes mark is unique, because it includes two marks (lines)
     * Undo() causes deleting both lines
     */
    
    private Line line1, line2;

    public ActionNewExtreme(FXMLDocumentController controller, Line line1, Line line2) {
        super(controller);
        this.line1 = line1;
        this.line2 = line2;
    }
    
    @Override
    public void undo() {
        controller.getZoomGroup().getChildren().remove(line1);
        controller.getZoomGroup().getChildren().remove(line2);
    }
    
}
