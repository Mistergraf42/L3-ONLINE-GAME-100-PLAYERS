package physique;

public class Controle {
    public boolean left;
    public boolean right;
    public boolean up;
    public boolean down;
    public boolean no;
    public boolean ne;
    public boolean so;
    public boolean se;


    public Controle(boolean left, boolean right, boolean up, boolean down, boolean no, boolean ne, boolean so, boolean se) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.no = no;
        this.ne = ne;
        this.so = so;
        this.se = se;
    }

    public void setLeft(boolean b){
        this.left = b;
    }
    public void setRight(boolean b){
        this.right = b;
    }
    public void setUp(boolean b){
        this.up = b;
    }
    public void setDown(boolean b){
        this.down = b;
    }
    public void setNo(boolean no) {
        this.no = no;
    }
    public void setNe(boolean ne) {
        this.ne = ne;
    }
    public void setSo(boolean so) {
        this.so = so;
    }
    public void setSe(boolean se) {
        this.se = se;
    }
    public void setAll(boolean b){
        this.up = b;
        this.down = b;
        this.left = b;
        this.right = b;
        this.no = b;
        this.ne = b;
        this.so = b;
        this.se = b;
    }
    public void resetAll(){
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.no = false;
        this.ne = false;
        this.so = false;
        this.se = false;
    }

}
