/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.pojos;

import java.io.Serializable;

/**
 *
 * @author utpl
 */
class Symbols implements Serializable{
    private String text;
    private Integer[] indices;

    public Symbols() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer[] getIndices() {
        return indices;
    }

    public void setIndices(Integer[] indices) {
        this.indices = indices;
    }
    
    
}
