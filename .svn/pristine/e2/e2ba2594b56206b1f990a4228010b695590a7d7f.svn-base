package farmApp.DAO.implement;

import farmApp.DAO.TipAnimaleDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.TipAnimale;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.List;

public class TipAnimaleDAOImplement implements TipAnimaleDAOInterface {

    private static final Logger LOG = Logger.getLogger(TipAnimaleDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public TipAnimaleDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();

    }

    @Override
    public void add(TipAnimale tipAnimale) throws SQLException {

        String sql = "INSERT INTO animal VALUES (null,?,?,?,?)";

        try {

            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setBoolean(1, tipAnimale.isActive());
            pstat.setTimestamp(2, tipAnimale.getCreateDate());
            pstat.setString(3, tipAnimale.getTipAnimal());
            pstat.setTimestamp(4, tipAnimale.getUpdateDate());

            pstat.executeUpdate();
            LOG.info("SALVAT!");
            conn.commit();
        } catch (Exception e) {

            LOG.info("Probleme la adaugare in DB");
            e.printStackTrace();
        } finally {

            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void update(TipAnimale tipAnimale) throws SQLException {

        String sql = "UPDATE animal SET active=?, created=?, name=?, updated=?"
                + "WHERE animal_id=?";

        try {

            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setBoolean(1, tipAnimale.isActive());
            pstat.setTimestamp(2, tipAnimale.getCreateDate());
            pstat.setString(3, tipAnimale.getTipAnimal());
            pstat.setTimestamp(4, tipAnimale.getUpdateDate());
            pstat.setInt(5, tipAnimale.getAnimalId());

            pstat.executeUpdate();
            conn.commit();

        } catch (Exception e) {

            LOG.info("PROBLEME LA UPDATE");

        } finally {
            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void delete(TipAnimale tipAnimale) throws SQLException {

        String sql = "DELETE FROM animal WHERE animal_id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);

        pstat.setInt(1, tipAnimale.getAnimalId());

        pstat.executeUpdate();

        LOG.info("Sters cu Succes!");

        pstat.close();
        mds.closeConnection();

    }

    @Override
    public TipAnimale findById(int animalId) throws SQLException {

        String sql = "SELECT * FROM animal WHERE animal_id=?";
        ResultSet rs = null;
        TipAnimale tipAnimale = null;
        try {
            conn = mds.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, animalId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                tipAnimale = new TipAnimale();

                tipAnimale.setAnimalId(animalId);
                tipAnimale.setActive(rs.getBoolean("active"));
                tipAnimale.setCreateDate(rs.getTimestamp("created"));
                tipAnimale.setTipAnimal(rs.getString("name"));
                tipAnimale.setUpdateDate(rs.getTimestamp(5));

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

            return tipAnimale;

        }

    }

    @Override
    public Collection<TipAnimale> findAll() throws SQLException {

        List<TipAnimale> tipAnimaleList = new ArrayList<>();

        TipAnimale tipAnimal = null;

        String sql = "SELECT * FROM animal";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            tipAnimal = new TipAnimale(rs.getInt(1), rs.getBoolean(2), rs.getTimestamp(3), rs.getString(4), rs.getTimestamp(5));

            tipAnimaleList.add(tipAnimal);
        }

        rs.close();
        pstat.close();
        mds.closeConnection();

        return tipAnimaleList;

    }

    @Override
    public TipAnimale findLast() throws SQLException {

        TipAnimale tip = null;

        String sql = "SELECT * FROM animal ORDER BY animal_id DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            int animalId = rs.getInt(1);
            boolean active = rs.getBoolean(2);
            Timestamp createDate = rs.getTimestamp(3);
            String tipAnimal = rs.getString(4);
            Timestamp updateDate = rs.getTimestamp(5);
            tip = new TipAnimale(animalId, active, createDate, tipAnimal, updateDate);
        } else {
            throw new SQLException("nu este nici un tip");
        }

        return tip;

    }

}
