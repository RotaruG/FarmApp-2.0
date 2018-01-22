package farmApp.Entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Veterinar {

   private int animalStockId;
   private String action;
   private Timestamp created;
   private String description;
   private String username;
   private int stockId;

   
    public Veterinar() {
    }

    public Veterinar(String action, Timestamp created, String description, String username) {
        this.action = action;
        this.created = created;
        this.description = description;
        this.username = username;
    }

    public Veterinar(int animalStockId, String action, Timestamp created, String description, String username) {
        this.animalStockId = animalStockId;
        this.action = action;
        this.created = created;
        this.description = description;
        this.username = username;
    }

    public Veterinar(int animalStockId, String action, Timestamp created, String description, String username, int stockId) {
        this.animalStockId = animalStockId;
        this.action = action;
        this.created = created;
        this.description = description;
        this.username = username;
        this.stockId = stockId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getAnimalStockId() {
        return animalStockId;
    }

    public void setAnimalStockId(int animalStockId) {
        this.animalStockId = animalStockId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Veterinar{" + "animalStockId=" + animalStockId + ", action=" + action + ", created=" + created + ", description=" + description + ", username=" + username + ", stockId=" + stockId + '}';
    }
   

}
