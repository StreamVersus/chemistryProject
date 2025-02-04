package pr.vladimir.demo1;

public class Vector2D {
    private double x, y;
    private final Vector2D limitX, limitY;
    public Vector2D(double x, double y, Vector2D limitX, Vector2D limitY) {
        this.x = x;
        this.y = y;
        this.limitX = limitX;
        this.limitY = limitY;
    }

    public Vector2D(double x, double y) {
        this(x, y, null, null);
    }

    public static Vector2D build(double x, double y, double limitX, double limitY) {
        Vector2D limitXvec, limitYvec;
        if(limitX >= 0) limitXvec = new Vector2D(0, limitX);
        else limitXvec = new Vector2D(limitX, 0);

        if(limitY >= 0) limitYvec = new Vector2D(0, limitY);
        else limitYvec = new Vector2D(limitY, 0);

        return new Vector2D(x, y, limitXvec, limitYvec);
    }

    public boolean isNotOnRange(double i){
        return !(x <= i) && !(y >= i);
    }

    public void setX(double x) {
        if(limitX != null) if(limitX.isNotOnRange(x)) return;
        this.x = x;
    }

    public void setY(double y) {
        if(limitY != null) if(limitY.isNotOnRange(y)) return;
        this.y = y;
    }

    public void addX(double x){
        if(limitX != null) if(limitX.isNotOnRange(this.x + x)) return;
        this.x += x;
    }

    public void addY(double Y){
        if(limitY != null) if(limitX.isNotOnRange(this.y + y)) return;
        this.y += y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
