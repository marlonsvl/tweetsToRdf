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
public class Scopes implements Serializable{
    private Boolean followers;

    public Scopes() {
    }
    
    public Boolean getFollowers() {
        return followers;
    }

    public void setFollowers(Boolean followers) {
        this.followers = followers;
    }
    
    
}
