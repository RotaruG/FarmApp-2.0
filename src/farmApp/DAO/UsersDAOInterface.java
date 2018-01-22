/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO;


import farmApp.Entities.Users;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Ghetto
 */
public interface UsersDAOInterface {

    void add(Users users) throws SQLException;  //C

    void update(Users users) throws SQLException;  //U

    void delete(Users users) throws SQLException;  //D

    Users findByUsername(String userName) throws SQLException;  //R
    
    Users findLast() throws SQLException;  //R

    List<Users> findAll() throws SQLException;//R

}
