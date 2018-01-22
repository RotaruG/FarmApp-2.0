package farmApp.DAO;

import farmApp.Entities.Amplasarea;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface AmplasareDAOInterface {

    void add(Amplasarea amplasarea) throws SQLException ;

    void update(Amplasarea amplasarea) throws SQLException;  

    void delete(Amplasarea amplasarea) throws SQLException;  

    Amplasarea findById(int id) throws SQLException;  

    Amplasarea findLast() throws SQLException;  

    Collection<Amplasarea> findAll() throws SQLException;

}
