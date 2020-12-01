import java.awt.*;

public class PlayButton extends SimpleButton {

    public PlayButton(double x, double y, double w, double h) {
        super(x,y,w,h);
    }

    @Override
    public void draw(Graphics window) {
        Graphics2D g2d = (Graphics2D) window;
        g2d.setColor(Color.BLACK);
        int[] xCoordinates = {(int)getxPos(), (int)getxPos(), (int)(getxPos()+getWidth())};
        int[] yCoordinates = {(int)getyPos(), (int)(getyPos()+getHeight()), (int)(getyPos()+getHeight()/2)};
        g2d.fill(new Polygon(xCoordinates, yCoordinates, 3));
    }
}
