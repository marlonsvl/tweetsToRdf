/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author utpl
 */
@JsonIgnoreProperties
public class HashTag implements Serializable{
    private Integer[] indices;
    private String text;

    public HashTag() {
    }
    
    public Integer[] getIndices() {
        return indices;
    }

    public void setIndices(Integer[] indices) {
        this.indices = indices;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
     
}
