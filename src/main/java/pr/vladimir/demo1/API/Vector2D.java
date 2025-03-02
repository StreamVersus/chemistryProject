package pr.vladimir.demo1.API;

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

    public void addY(double y){
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
