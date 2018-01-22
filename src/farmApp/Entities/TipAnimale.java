/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Ghetto
 */
public class TipAnimale {
    
    
    private int animalId;
    private boolean active;
    private Timestamp createDate;
    private String tipAnimal;
    private Timestamp updateDate;
    private Collection<TipAnimale> coll;

    public TipAnimale() {
    }

    public TipAnimale(boolean active, Timestamp createDate, String tipAnimal, Timestamp updateDate) {
        this.active = active;
        this.createDate = createDate;
        this.tipAnimal = tipAnimal;
        this.updateDate = updateDate;
    }

    public TipAnimale (String tipAnimal, Collection<TipAnimale> coll){
    
        this.tipAnimal=tipAnimal;
        this.coll=coll;
    
    
    }
    
    public TipAnimale(int animalId, boolean active, Timestamp createDate, String tipAnimal, Timestamp updateDate) {
        this.animalId = animalId;
        this.active = active;
        this.createDate = createDate;
        this.tipAnimal = tipAnimal;
        this.updateDate = updateDate;
    }

   

    

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getTipAnimal() {
        return tipAnimal;
    }

    public void setTipAnimal(String tipAnimal) {
        this.tipAnimal = tipAnimal;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    
    @Override
    public String toString() {
        return ""+tipAnimal ;
        
    }
  

   

   
    
    
    
    
    
    
}
