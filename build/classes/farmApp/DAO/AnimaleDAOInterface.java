/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO;

import farmApp.Entities.Animale;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Ghetto
 */
public interface AnimaleDAOInterface {

    //CRUD operations
    void add(Animale animal) throws SQLException;  //C

    void update(Animale animal) throws SQLException;  //U

    void delete(Animale animal) throws SQLException;  //D

    Animale findById(int stockId) throws SQLException;  //R
    
    Animale findLast() throws SQLException;  //R

    List<Animale> findAll() throws SQLException;//R
    
    Animale findByCode(String animalCode)throws SQLException;

}
