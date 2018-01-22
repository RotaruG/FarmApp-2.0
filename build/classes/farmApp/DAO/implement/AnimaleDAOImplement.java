/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO.implement;

import farmApp.DAO.AnimaleDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Animale;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ghetto
 */
public class AnimaleDAOImplement implements AnimaleDAOInterface {

    private static final Logger LOG = Logger.getLogger(AnimaleDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public AnimaleDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();
    }

    @Override
    public void add(Animale animal) throws SQLException {

        String sql = "INSERT INTO stock VALUES(null,?,?,?,?,?,?,?,?,?);";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setBoolean(1, animal.isActive());
            pstat.setString(2, animal.getAnimalCode());
            pstat.setTimestamp(3, animal.getDeliveryDate());
            pstat.setTimestamp(4, animal.getReceivedDate());
            pstat.setString(5, animal.getReason());
            pstat.setString(6, animal.getSellerName());
            pstat.setDouble(7, animal.getReceivedWeight());
            pstat.setInt(8, animal.getAnimalId());
            pstat.setInt(9, animal.getLocationId());

            pstat.executeUpdate();
            LOG.info("Animal salvat ");

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("Probleme la adaugare in DB!");
        } finally {

            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void update(Animale animal) throws SQLException {

        String sql = "UPDATE stock SET active=?, animal_code=?, date_delivery=? , date_recive=?, reason=?, seller_name=?, weight_recived=?,animal_id=?, location_id=? WHERE stock_id=?";
        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setBoolean(1, animal.isActive());
            pstat.setString(2, animal.getAnimalCode());
            pstat.setTimestamp(3, animal.getDeliveryDate());
            pstat.setTimestamp(4, animal.getReceivedDate());
            pstat.setString(5, animal.getReason());
            pstat.setString(6, animal.getSellerName());
            pstat.setDouble(7, animal.getReceivedWeight());
            pstat.setInt(8, animal.getAnimalId());
            pstat.setInt(9, animal.getLocationId());
            pstat.setInt(10, animal.getStockId());
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
    public void delete(Animale animal) throws SQLException {

        String sql = "DELETE FROM stock WHERE stock_id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);

        pstat.setInt(1, animal.getStockId());

        pstat.executeUpdate();

        pstat.close();
        mds.closeConnection();

    }

    @Override
    public Animale findById(int stockId) throws SQLException {
        Animale animal = null;

        String sql = "SELECT * FROM stock WHERE stock_id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setInt(1, stockId);
        ResultSet rs = pstat.executeQuery();

        if (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));

        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return animal;
    }

    @Override
    public List<Animale> findAll() throws SQLException {

        List<Animale> animaleList = new ArrayList<>();

        Animale animal = null;

        String sql = "SELECT * FROM stock";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));

            animaleList.add(animal);
        }
        rs.close();
        pstat.close();
        mds.closeConnection();
        return animaleList;
    }

    @Override
    public Animale findByCode(String animalCode) throws SQLException {
        Animale animal = null;

        String sql = "SELECT * FROM stock WHERE animal_code=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, animalCode);
        ResultSet rs = pstat.executeQuery();

        if (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));

        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return animal;
    }

    @Override
    public Animale findLast() throws SQLException {
        Animale a = null;

        String sql = "SELECT * FROM stock ORDER BY stock_id DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            int stockId = rs.getInt(1);
            boolean active = rs.getBoolean(2);
            String animalCode = rs.getString(3);
            Timestamp deliveryDate = rs.getTimestamp(4);
            Timestamp receivedDate = rs.getTimestamp(5);
            String reason = rs.getString(6);
            String sellerName = rs.getString(7);
            Double receivedWeight = rs.getDouble(8);
            int animalId = rs.getInt(9);
            int locationId = rs.getInt(10);
            a = new Animale(stockId, active, animalCode, deliveryDate, receivedDate, reason, sellerName,receivedWeight,animalId,locationId);
        } else {
            throw new SQLException("nu este nici un animal");
        }

        return a;
    }

}
