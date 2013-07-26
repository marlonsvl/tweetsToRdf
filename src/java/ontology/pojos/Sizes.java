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
public class Sizes implements Serializable{
    private Size thumb;
    private Size large;
    private Size medium;
    private Size small;

    public Sizes() {
    }
    
    public Size getThumb() {
        return thumb;
    }

    public void setThumb(Size thumb) {
        this.thumb = thumb;
    }

    public Size getLarge() {
        return large;
    }

    public void setLarge(Size large) {
        this.large = large;
    }

    public Size getMedium() {
        return medium;
    }

    public void setMedium(Size medium) {
        this.medium = medium;
    }

    public Size getSmall() {
        return small;
    }

    public void setSmall(Size small) {
        this.small = small;
    }
    
    
}
