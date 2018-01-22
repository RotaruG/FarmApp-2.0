
package farmApp.Controllers.util;


public class Role {
    private static Role role = null;
    private int x;

    public Role(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public static Role getInstance() {
        if(role == null) {
            throw new AssertionError("You have to call init first");
        }

        return role;
    }
    
    

    public  static Role init(int x) {
        if (role != null)
        {
         
            role = new Role(x);
        }

        role = new Role(x);
        return role;
    }

}