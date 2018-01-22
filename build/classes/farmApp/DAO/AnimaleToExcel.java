/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO;

import farmApp.Entities.Animale;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ghetto
 */
public interface AnimaleToExcel {
    void scrie(List<Animale> lista, String fileName) throws IOException;
  
    List<Animale> citeste(String fileName) throws IOException;   
}
