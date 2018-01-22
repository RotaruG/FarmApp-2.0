/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO.implement;


import farmApp.DAO.PersonalDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Personal;
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
public class PersonalDAOImplement implements PersonalDAOInterface {

    private static final Logger LOG = Logger.getLogger(PersonalDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public PersonalDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();

    }

    @Override
    public void add(Personal personal) throws SQLException {

        String sql = "INSERT INTO personal VALUES(null,?,?,?,?,?,?)";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            // pstat.setInt(1, personal.getId());
            pstat.setString(1, personal.getNume().trim());
            pstat.setString(2, personal.getPrenume().trim());
            pstat.setString(3, personal.getFunctia().trim());
            pstat.setBoolean(4, personal.getStatut());
            pstat.setTimestamp(5, personal.getDataAngajare());
            pstat.setTimestamp(6, personal.getDataConcediere());

            pstat.executeUpdate();
            LOG.info(" Persoana Salvata ");

            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la adaugare in DB!");
        } finally {

            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void update(Personal personal) throws SQLException {

        String sql = "UPDATE personal SET nume=?, prenume=?, functia=? , statut=?, data_angajare=?, data_concediere=?"
                + "WHERE id=?";
        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, personal.getNume());
            pstat.setString(2, personal.getPrenume());
            pstat.setString(3, personal.getFunctia());
            pstat.setBoolean(4, personal.getStatut());
            pstat.setTimestamp(5, personal.getDataAngajare());
            pstat.setTimestamp(6, personal.getDataConcediere());
            pstat.setInt(7, personal.getId());

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
    public void delete(Personal personal) throws SQLException {

        String sql = "DELETE FROM personal WHERE id=?";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);

        pstat.setInt(1, personal.getId());

        pstat.executeUpdate();

        pstat.close();
        mds.closeConnection();

        LOG.info("a fost in metoda delete");

    }

    @Override
    public Personal findById(int id) throws SQLException {

        String sql = "SELECT * FROM personal WHERE id=?";
        ResultSet rs = null;
        Personal personal = new Personal();
        try {
            conn = mds.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, id);
            rs = pstat.executeQuery();
            if (rs.next()) {
                personal.setId(id);
                personal.setNume(rs.getString("nume").trim());
                personal.setPrenume(rs.getString("prenume").trim());
                personal.setFunctia(rs.getString("functia").trim());
                personal.setStatut(rs.getBoolean("statut"));
                personal.setDataAngajare(rs.getTimestamp("data_angajare"));
                personal.setDataConcediere(rs.getTimestamp("data_concediere"));
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

            return personal;

        }

    }

    @Override
    public List<Personal> findAll() throws SQLException {

        List<Personal> personalList = new ArrayList<>();

        String sql = "SELECT * FROM personal ";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {

            Personal p = new Personal(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    rs.getBoolean(5), rs.getTimestamp(6), rs.getTimestamp(7));

            personalList.add(p);
        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return personalList;
    }
    
     @Override
    public Personal findLast() throws SQLException {
        Personal personal = null;

        String sql = "SELECT * FROM personal ORDER BY id DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            String nume = rs.getString(2);
            String prenume = rs.getString(3);
            String functia = rs.getString(4);
            Boolean statut = rs.getBoolean(5);
            Timestamp dataAngajare = rs.getTimestamp(6);
            Timestamp dataConcediere = rs.getTimestamp(7);
            personal = new Personal(id, nume, prenume, functia, statut, dataAngajare, dataConcediere );
        } else {
            
        }

        return personal;
    }


}
