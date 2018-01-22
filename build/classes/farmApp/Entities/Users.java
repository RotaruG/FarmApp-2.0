/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Entities;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Ghetto
 */
public class Users {
    
   private String userName;
   private Boolean activat;
   private Timestamp dataCreare;
   private Timestamp dataExpirare;
   private Boolean enabled;
   private String password;
   private String passwordConfirm;
   private Timestamp updated;
   private int role;

    public Users() {
    }

    public Users(String userName, Boolean activat, Timestamp dataCreare, Timestamp dataExpirare, Boolean enabled, String password, String passwordConfirm, Timestamp updated, int role) {
        this.userName = userName;
        this.activat = activat;
        this.dataCreare = dataCreare;
        this.dataExpirare = dataExpirare;
        this.enabled = enabled;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.updated = updated;
        this.role = role;
    }

    
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActivat() {
        return activat;
    }

    public void setActivat(Boolean activat) {
        this.activat = activat;
    }

    public Timestamp getDataCreare() {
        return dataCreare;
    }

    public void setDataCreare(Timestamp dataCreare) {
        this.dataCreare = dataCreare;
    }

    public Timestamp getDataExpirare() {
        return dataExpirare;
    }

    public void setDataExpirare(Timestamp dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String toString() {
        return "Users{" + "userName=" + userName + ", activat=" + activat + ", dataCreare=" + dataCreare + ", dataExpirare=" + dataExpirare + ", enabled=" + enabled + ", password=" + password + ", passwordConfirm=" + passwordConfirm + ", updated=" + updated + '}';
    }

    public int getRole() {
        return role;
    }

    public int setRole(int role) {
        this.role = role;
       return role;
    }

    
   
}
