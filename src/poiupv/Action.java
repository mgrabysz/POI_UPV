/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

/**
 *
 * @author margr
 */
public abstract class Action {
    
    final FXMLDocumentController controller;
    
    public Action(FXMLDocumentController controller) {
        this.controller = controller;
    }

    public abstract void undo();
}
