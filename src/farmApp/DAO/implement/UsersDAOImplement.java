package farmApp.DAO.implement;

import farmApp.DAO.UsersDAOInterface;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Users;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDAOImplement implements UsersDAOInterface {

    private static final Logger LOG = Logger.getLogger(UsersDAOImplement.class.getName());

    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;

    public UsersDAOImplement() throws ClassNotFoundException, SQLException, IOException {

        mds = MyDataSource.getInstance();

    }

    @Override
    public void add(Users users) throws SQLException {

        String sql = "INSERT INTO users VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setString(1, users.getUserName());
            pstat.setBoolean(2, users.getActivat());
            pstat.setTimestamp(3, users.getDataCreare());
            pstat.setTimestamp(4, users.getDataExpirare());
            pstat.setBoolean(5, users.getEnabled());
            pstat.setString(6, users.getPassword());
            pstat.setString(7, users.getPasswordConfirm());
            pstat.setTimestamp(8, users.getUpdated());
            pstat.setInt(9, users.getRole());

            pstat.executeUpdate();
            LOG.info(" User Salvat ");

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
    public void update(Users users) throws SQLException {

        String sql = "UPDATE users SET activat=?, created=?, data_expirarii=?, enabled=?, password=?, passwordConfirm=?, updated=?, role=? WHERE username=?";

        try {
            conn = mds.getConnection();
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);

            pstat.setBoolean(1, users.getActivat());
            pstat.setTimestamp(2, users.getDataCreare());
            pstat.setTimestamp(3, users.getDataExpirare());
            pstat.setBoolean(4, users.getEnabled());
            pstat.setString(5, users.getPassword());
            pstat.setString(6, users.getPasswordConfirm());
            pstat.setTimestamp(7, users.getUpdated());
            pstat.setInt(8, users.getRole());
            pstat.setString(9, users.getUserName());

            pstat.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            LOG.info("Probleme la update! ");
            e.printStackTrace();
        } finally {
            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public void delete(Users users) throws SQLException {

        String sql = "DELETE FROM users WHERE username=?";
        try {
            conn = mds.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, users.getUserName());

            pstat.executeUpdate();
            pstat.close();
            mds.closeConnection();

        } catch (Exception e) {
            LOG.info("Probleme la delete! ");
            e.printStackTrace();
        } finally {
            pstat.close();
            mds.closeConnection();

        }

    }

    @Override
    public Users findByUsername(String userName) throws SQLException {

        String sql = "SELECT * FROM users WHERE username=?";
        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, userName);
        ResultSet rs = pstat.executeQuery();
        Users user = null;

        if (rs.next()) {
            user = new Users(rs.getString(1), rs.getBoolean(2),
                    rs.getTimestamp(3), rs.getTimestamp(4),
                    rs.getBoolean(5), rs.getString(6),
                    rs.getString(7), rs.getTimestamp(8), rs.getInt(9));

        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return user;

    }

    @Override
    public List<Users> findAll() throws SQLException {

        List<Users> usersList = new ArrayList<>();

        Users user = null;

        String sql = "SELECT * FROM users";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {

            user = new Users();
            user.setUserName(rs.getString(1));
            user.setActivat(rs.getBoolean(2));
            user.setDataCreare(rs.getTimestamp(3));
            user.setDataExpirare(rs.getTimestamp(4));
            user.setEnabled(rs.getBoolean(5));
            user.setPassword(rs.getString(6));
            user.setPasswordConfirm(rs.getString(7));
            user.setUpdated(rs.getTimestamp(8));
            user.setRole(rs.getInt(9));

            usersList.add(user);
        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return usersList;

    }

    @Override
    public Users findLast() throws SQLException {

        Users u = null;

        String sql = "SELECT * FROM users ORDER BY username DESC LIMIT 0,1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        if (rs.next()) {
            String userName = rs.getString(1);
            Boolean activat = rs.getBoolean(2);
            Timestamp dataCreare = rs.getTimestamp(3);
            Timestamp dataExpirare = rs.getTimestamp(4);
            Boolean enabled = rs.getBoolean(5);
            String password = rs.getString(6);
            String passwordConfirm = rs.getString(7);
            Timestamp updated = rs.getTimestamp(8);
            int role = rs.getInt(9);
            u = new Users(userName, activat, dataCreare, dataExpirare, enabled, password, passwordConfirm,updated,role);
        } else {
            throw new SQLException("nu este nici un personal");
        }

        return u;

    }

}
