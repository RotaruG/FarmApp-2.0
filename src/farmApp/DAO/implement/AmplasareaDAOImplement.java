package farmApp.DAO.implement;

import farmApp.DAO.AmplasareDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Amplasarea;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Ghetto
 */
public class AmplasareaDAOImplement implements AmplasareDAOInterface {

    private static final Logger LOG = Logger.getLogger(AmplasareaDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public AmplasareaDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();

    }

    @Override
    public void add(Amplasarea amplasarea) throws SQLException {

        String sql = "INSERT INTO location_animal VALUES(null,?,?,?,?,?,?,?)";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, amplasarea.getBuilding());
            pstat.setString(2, amplasarea.getCell());
            pstat.setString(3, amplasarea.getFirstDetail());
            pstat.setString(4, amplasarea.getSecondDetail());
            pstat.setString(5, amplasarea.getSector());
            pstat.setString(6, amplasarea.getShortLocation());
            pstat.setString(7, amplasarea.getThirdDetail());

            pstat.executeUpdate();
            LOG.info("Amplasarea salvata");
            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la adaugat !");
            e.printStackTrace();
        } finally {

            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void update(Amplasarea amplasarea) throws SQLException {

        String sql = "UPDATE location_animal SET build=?, cell=?, first_detail=?, second_detail=?, sector=?, short_location=?, third_detail=? WHERE location_id=?";
        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, amplasarea.getBuilding());
            pstat.setString(2, amplasarea.getCell());
            pstat.setString(3, amplasarea.getFirstDetail());
            pstat.setString(4, amplasarea.getSecondDetail());
            pstat.setString(5, amplasarea.getSector());
            pstat.setString(6, amplasarea.getShortLocation());
            pstat.setString(7, amplasarea.getThirdDetail());
            pstat.setInt(8, amplasarea.getId());

            pstat.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la Update!");
            e.printStackTrace();
        } finally {
            pstat.close();
            mds.closeConnection();
        }

    }

    @Override
    public void delete(Amplasarea amplasarea) throws SQLException {

        String sql = "DELETE FROM location_animal WHERE location_id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);

        pstat.setInt(1, amplasarea.getId());

        pstat.executeUpdate();

        pstat.close();
        mds.closeConnection();

    }

    @Override
    public Amplasarea findById(int id) throws SQLException {

        String sql = "SELECT * FROM location_animal WHERE location_id=?";
        ResultSet rs = null;
        Amplasarea amplasarea = null;
        try {
            conn = mds.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, id);
            rs = pstat.executeQuery();
            if (rs.next()) {
                amplasarea = new Amplasarea();
                amplasarea.setId(id);
                amplasarea.setBuilding(rs.getString("build"));
                amplasarea.setCell(rs.getString("cell"));
                amplasarea.setFirstDetail(rs.getString("first_detail"));
                amplasarea.setSecondDetail(rs.getString("second_detail"));
                amplasarea.setSector(rs.getString("sector"));
                amplasarea.setShortLocation(rs.getString("short_location"));
                amplasarea.setThirdDetail(rs.getString("third_detail"));

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

            return amplasarea;

        }

    }

    @Override
    public Collection<Amplasarea> findAll() throws SQLException {

        Collection<Amplasarea> amplasareaList = new ArrayList<>();

        Amplasarea amplasarea = null;

        String sql = "SELECT * FROM location_animal";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            amplasarea = new Amplasarea(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));

            amplasareaList.add(amplasarea);
        }

        return amplasareaList;
    }

    @Override
    public Amplasarea findLast() throws SQLException {

        Amplasarea amplasarea = null;

        String sql = "SELECT * FROM location_animal ORDER BY location_id DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            String building = rs.getString(2);
            String cell = rs.getString(3);
            String firstDetail = rs.getString(4);
            String secondDetail = rs.getString(5);
            String sector = rs.getString(6);
            String shortLocation = rs.getString(7);
            String thirdDetail = rs.getString(8);
            amplasarea = new Amplasarea(id, building, cell, firstDetail, secondDetail, sector, shortLocation,thirdDetail);
        } else {
            throw new SQLException("nu este nici o amplasare");
        }

        return amplasarea;

    }

}
