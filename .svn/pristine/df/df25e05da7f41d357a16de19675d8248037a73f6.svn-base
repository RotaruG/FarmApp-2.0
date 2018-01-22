package farmApp.Controllers.util;

import farmApp.DB.MyDataSource;
import farmApp.Entities.Users;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;


public class CheckUserNameAndPassword {

    private static final Logger LOG = Logger.getLogger(CheckUserNameAndPassword.class.getName());
    Users u;
    String databaseUsername = "";
    String databasePassword = "";
    Timestamp dataExpirare;
    LocalDateTime local = LocalDateTime.now();
    Boolean activat;
    Boolean enabled;
    public int role;
    private MyDataSource mds = MyDataSource.getInstance();
    private Connection conn;
    private PreparedStatement pstat;
    
    

    public CheckUserNameAndPassword(Users u) throws ClassNotFoundException, SQLException, IOException {

        
        this.u = u;

        mds = MyDataSource.getInstance();
    }

    public boolean getInfo(Users u) throws SQLException {

        String sql = "SELECT * FROM users WHERE username=? && password=?";

        ResultSet rs = null;

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, u.getUserName());
        pstat.setString(2, u.getPassword());
        rs = pstat.executeQuery();

        while (rs.next()) {
            databaseUsername = rs.getString("username");
            databasePassword = rs.getString("password");
        }

        if (u.getUserName().equals(databaseUsername) && u.getPassword().equals(databasePassword)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getExpirationDate(Users u) throws SQLException {

        String sql = "SELECT data_expirarii FROM users WHERE username=? && password=?";

        ResultSet rs = null;

        farmApp.Controllers.util.ConvertorData dc = new farmApp.Controllers.util.ConvertorData();
                  
        
        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, u.getUserName());
        pstat.setString(2, u.getPassword());
        rs = pstat.executeQuery();

        while (rs.next()) {
            dataExpirare = rs.getTimestamp("data_expirarii");

        }

        if (local.isAfter(dc.convertToEntityAttribute(dataExpirare))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getActive(Users u) throws SQLException {

        String sql = "SELECT activat FROM users WHERE username=? && password=?";

        ResultSet rs = null;

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, u.getUserName());
        pstat.setString(2, u.getPassword());
        rs = pstat.executeQuery();

        while (rs.next()) {
            activat = rs.getBoolean("activat");

        }

        if (activat) {
            return true;
        } else {
            return false;
        }
    }
     public boolean getEnabled(Users u) throws SQLException {

        String sql = "SELECT enabled FROM users WHERE username=? && password=?";

        ResultSet rs = null;

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, u.getUserName());
        pstat.setString(2, u.getPassword());
        rs = pstat.executeQuery();

        while (rs.next()) {
            enabled = rs.getBoolean("enabled");

        }

        if (enabled) {
            return true;
        } else {
            return false;
        }
    }

    public int getRole(Users u) throws SQLException {

        LOG.info("ajunge la metoda");

        String sql = "SELECT * FROM users WHERE username=? && password=?";

        ResultSet rs = null;

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, u.getUserName());
        pstat.setString(2, u.getPassword());
        rs = pstat.executeQuery();

        while (rs.next()) {
            role = rs.getInt("role");
        }

        if (role == 1) {
            return u.setRole(1);
        }
        if (role == 2) {
            return u.setRole(2);
        }
        if (role == 0) {
            return u.setRole(0);
        }
        return u.getRole();

    }

}
