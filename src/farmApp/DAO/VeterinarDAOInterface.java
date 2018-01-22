/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO;


import farmApp.Entities.Veterinar;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Ghetto
 */
public interface VeterinarDAOInterface {
    
    void add(Veterinar vet) throws SQLException;  //C

    void update(Veterinar vet) throws SQLException;  //U

    void delete(Veterinar vet) throws SQLException;  //D

    Veterinar findById(int id) throws SQLException;  //R
    
    Veterinar findLast() throws SQLException;  //R

    List<Veterinar> findAll() throws SQLException;//R
    
    
    
}
