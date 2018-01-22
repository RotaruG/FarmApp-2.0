/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO.implement;

import farmApp.DAO.VeterinarDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Veterinar;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Ghetto
 */
public class VeterinarDAOImplement implements VeterinarDAOInterface {

    private static final Logger LOG = Logger.getLogger(VeterinarDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public VeterinarDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();

    }

    @Override
    public void add(Veterinar vet) throws SQLException {

        String sql = "INSERT INTO animalstockinfo VALUES(null,?,?,?,?,?);";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, vet.getAction());
            pstat.setTimestamp(2, vet.getCreated());
            pstat.setString(3, vet.getDescription());
            pstat.setString(4, vet.getUsername());
            pstat.setInt(5, vet.getStockId());

            pstat.executeUpdate();
            LOG.info("Info Vet salvat ");

            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la adaugare in DB!");
            e.printStackTrace();

            throw new SQLException();

        } finally {

            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void update(Veterinar vet) throws SQLException {

        String sql = "UPDATE animalstockinfo SET action=?, created=?, description=?, username=?, stock_id=? "
                + "WHERE animal_stock_id=?";
        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, vet.getAction());
            pstat.setTimestamp(2, vet.getCreated());
            pstat.setString(3, vet.getDescription());
            pstat.setString(4, vet.getUsername());
            pstat.setInt(5, vet.getStockId());
            pstat.setInt(6, vet.getAnimalStockId());

            pstat.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la Update!");
        } finally {
            pstat.close();
            mds.closeConnection();
        }

    }

    @Override
    public void delete(Veterinar vet) throws SQLException {

        String sql = "DELETE FROM animalstockinfo WHERE animal_stock_id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);

        pstat.setInt(1, vet.getAnimalStockId());

        pstat.executeUpdate();

        pstat.close();
        mds.closeConnection();

    }

    @Override
    public Veterinar findById(int id) throws SQLException {

        String sql = "SELECT * FROM animalstockinfo WHERE animal_stock_id=?";
        ResultSet rs = null;
        Veterinar vet = null;
        try {
            conn = mds.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, id);
            rs = pstat.executeQuery();
            if (rs.next()) {
                vet = new Veterinar();
                vet.setAnimalStockId(id);
                vet.setAction(rs.getString("action").trim());
                vet.setCreated(rs.getTimestamp("created"));
                vet.setDescription(rs.getString("description").trim());
                vet.setUsername(rs.getString("username").trim());
                vet.setStockId(rs.getInt("stock_id"));

            }
        } catch (SQLException ex) {
            LOG.info("Nu a fost gasit");
        } finally {
            try {
                pstat.close();
                rs.close();
                mds.closeConnection();
            } catch (SQLException ex) {

            }

            return vet;

        }
    }

    @Override
    public List<Veterinar> findAll() throws SQLException {

        List<Veterinar> vetList = new ArrayList<>();

        Veterinar vet = null;

        String sql = "SELECT * FROM animalstockinfo";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {

            vet = new Veterinar(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5), rs.getInt(6));
            vetList.add(vet);

        }

        return vetList;
    }

    @Override
    public Veterinar findLast() throws SQLException {

        Veterinar vet = null;

        String sql = "SELECT * FROM animalstockinfo ORDER BY animal_stock_id DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            int animalStockId = rs.getInt(1);
            String action = rs.getString(2);
            Timestamp created = rs.getTimestamp(3);
            String description = rs.getString(4);
            String username = rs.getString(5);
            int stockId = rs.getInt(6);
            vet = new Veterinar(animalStockId, action, created, description, username, stockId);
        } else {
            throw new SQLException("nu este nici un veterinar");
        }

        return vet;

    }

}
