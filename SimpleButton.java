import java.awt.*;

public abstract class SimpleButton {
    private double xPos, yPos, w, h;

    public SimpleButton() {
        xPos = yPos = w = h = 0;
    }

    public SimpleButton(double x, double y, double width, double height) {
        xPos = x;
        yPos = y;
        w = width;
        h = height;
    }

    public abstract void draw(Graphics window);

    public boolean isInside(double x, double y) {
        if (x >= xPos && x <= xPos+w && y >= yPos && y <= yPos+h)
            return true;
        return false;
    }

    public void setxPos(double x) {
        xPos = x;
    }

    public void setyPos(double y) {
        yPos = y;
    }

    public void setWidth(double w) {
        this.w  = w;
    }

    public void setHeight(double h) {
        this.h = h;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }
}
