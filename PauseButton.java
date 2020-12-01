import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class PauseButton extends SimpleButton {

    public PauseButton(double x, double y, double w, double h) {
        super(x,y,w,h);
    }

    @Override
    public void draw(Graphics window) {
        Graphics2D g2d = (Graphics2D) window;
        g2d.setColor(Color.BLACK);
        g2d.fill(new Rectangle2D.Double(getxPos(), getyPos(), getWidth()/3, getHeight()));
        g2d.fill(new Rectangle2D.Double(getxPos()+getWidth()/2, getyPos(), getWidth()/3, getHeight()));
    }
}
