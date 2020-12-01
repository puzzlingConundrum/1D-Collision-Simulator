import java.awt.*;
import java.awt.geom.*;

public class Block extends Entity {
    public Block() {
        super();
    }

    public Block(double x, double y) {
        super(x, y);
    }

    public Block(double x, double y, double w, double h, double m) {
        super(x,y,w,h,m);
    }

    public Block(double x, double y, double w, double h, double m, double v) {
        super(x,y,w,h,m,v);
    }

    public Block(double x, double y, double w, double h, double m, double v, Color c) {
        super(x,y,w,h,m,v,c);
    }

    public Block(double x, double y, double m, double v, Color c) {
        super(x,y-m*10, m*10, m*10, m, v, c);
    }

    @Override
    public double[] getLocation() {
        return new double[]{getxPos(), getyPos(), getWidth(), getHeight()};
    }

    @Override
    public void draw(Graphics window) {
        window.setColor(getColor());
        Graphics2D g2d = (Graphics2D) window;
        g2d.fill(new Rectangle2D.Double(getxPos(), getyPos(), getWidth(), getHeight()));
    }

    @Override
    public boolean didCollide(Object obj) {
        Entity other = (Entity) obj;
        double[] bounds = other.getLocation();

        // checks left and right bounds
        if (bounds[0]<=getxPos()+getWidth()&&bounds[0]>=getxPos()
                ||bounds[0]+bounds[2]<=getxPos()+getWidth()&&bounds[0]+bounds[2]>=getxPos()) {
            // checks top and bottom bounds
            if (bounds[1]<=getyPos()+getHeight()&&bounds[1]>=getyPos()
                    ||bounds[1]+bounds[3]<=getyPos()+getHeight()&&bounds[1]+bounds[3]>=getyPos()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInside(double x, double y) {
        return (x>getxPos()&&x<getxPos()+getWidth()&&y>getyPos()&&y<getyPos()+getHeight());
    }
}
