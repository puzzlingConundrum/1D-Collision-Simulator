import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Immovable extends Entity {
    public Immovable(double x, double y, double w, double h) {
        super(x,y,w,h);
    }

    public void collision(Entity other) {
        if (didCollide(other)) {
            other.setVelocity(-other.getVelocity());
        }
    }

    @Override
    public double[] getLocation() {
        return new double[]{getxPos(), getyPos(), getWidth(), getHeight()};
    }

    @Override
    public boolean didCollide(Object obj) {
        Entity other = (Entity) obj;
        double[] bounds = other.getLocation();
        if (bounds[0]<=getxPos()+getWidth()&&bounds[0]>=getxPos()
                ||bounds[0]+bounds[2]<=getxPos()+getWidth()&&bounds[0]+bounds[2]>=getxPos()) {
            if (bounds[1]<=getyPos()+getHeight()&&bounds[1]>=getyPos()
                    ||bounds[1]+bounds[3]<=getyPos()+getHeight()&&bounds[1]+bounds[3]>=getyPos()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInside(double x, double y) {
        return false;
    }

    @Override
    public void draw(Graphics window) {
        Graphics2D g2d = (Graphics2D) window;
        window.setColor(Color.BLACK);
        g2d.fill(new Rectangle2D.Double(getxPos(), getyPos(), getWidth(), getHeight()));
    }
}
