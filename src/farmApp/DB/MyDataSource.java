package farmApp.DB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDataSource {

    private static final Logger LOG = Logger.getLogger(MyDataSource.class.getName());

    private String driverName = "com.mysql.jdbc.Driver";
    private String username = "root";
    private String password = "free";
    private String dbUrl = "jdbc:mysql://localhost:3306/store_farm";
    private Connection conn;


    private static MyDataSource instance;

    public static MyDataSource getInstance() throws ClassNotFoundException, SQLException, IOException {

        if (instance == null) {

            instance = new MyDataSource();
        }

        return instance;
    }

    private MyDataSource() throws ClassNotFoundException, SQLException, IOException{
    
       // loadPropreties();
       // LOG.info("Proprietati Incarcate!");
        loadDriver();
        LOG.info("Driver Incarcat!");
        loadConnection();
        LOG.info("Conexiune Stabilita!");
    
    
    
}

    private void loadPropreties() throws FileNotFoundException, IOException {

        LOG.info("proprietati din fisier");

        Properties propFile = new Properties();
        propFile.load(new FileReader("proprietati.properties"));
        String pdrivername = propFile.getProperty("jdbc.drivername");
        String puserbd = propFile.getProperty("jdbc.user");
        String ppassdb = propFile.getProperty("jdbc.password");
        String purldb = propFile.getProperty("jdbc.url");
        
        if (driverName==null) {
            this.driverName=pdrivername;
        }
        if (username==null) {
            this.username=puserbd;
        }
        if (password==null) {
            this.password=ppassdb;
        }
        if (dbUrl==null) {
            this.dbUrl=purldb;
        }
        
    }

    private void loadDriver() throws ClassNotFoundException {

        Class.forName(driverName);
    }

    private void loadConnection() throws SQLException {
   
        conn = DriverManager.getConnection(dbUrl, username, password);
    
    }
    
    public Connection getConnection() throws SQLException{
        if (conn==null || (conn!=null && conn.isClosed())) {
          loadConnection();  
        }
    
    return conn;
    }

  public void closeConnection() throws SQLException{
  
      if (conn!=null && !conn.isClosed()) {
          conn.close();
      }
  
  }
   
    public static void main(String[] args) {

        try {
            MyDataSource mds = MyDataSource.getInstance();

            Connection conn = mds.getConnection();

            String cat = conn.getCatalog();

            mds.closeConnection();

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }
 
}
