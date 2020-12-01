import java.awt.*;

public abstract class Entity {
    private double xPos, yPos, width, height, mass, velocity;
    private Color color;
    private boolean friction;
    private double mu;
    private final double G = 0.01;

    public Entity() {
        // default values
        xPos = yPos = 0;
        width = height = 50;
        mass = 5;
        velocity = 1.0;
        color = Color.BLACK;
        mu = 0.1;
    }

    public Entity(double x, double y) {
        this();
        xPos = x;
        yPos = y;
    }

    public Entity(double x, double y, double w, double h) {
        this(x,y);
        width = w;
        height = h;
    }

    public Entity(double x, double y, double w, double h, double m) {
        this(x,y,w,h);
        mass = m;
    }

    public Entity(double x, double y, double w, double h, double m, double v) {
        this(x,y,w,h,m);
        velocity = v;
    }

    public Entity(double x, double y, double w, double h, double m, double v, Color col) {
        this(x,y,w,h,m,v);
        color = col;
    }

    public Entity(double x, double y, double w, double h, double m, double v, Color col, double mu) {
        this(x,y,w,h,m,v,col);
        this.mu = mu;
    }

    // returns a list of doubles to represent the object's boundaries
    public abstract double[] getLocation();

    public abstract boolean didCollide(Object obj);

    public abstract boolean isInside(double x, double y);

    public abstract void draw(Graphics window);

    public void move() {
        xPos += velocity;
        if (friction) {
            double acceleration = mu * G;
            if (Math.abs(velocity) > acceleration) {
                setVelocity(velocity < 0 ? velocity+acceleration : velocity-acceleration);
            } else {
                velocity = 0.0;
            }
        }
    }

    public void elasticCollision(Entity other) {
        if (didCollide(other)) {
            double v1 = getVelocity();
            double v2 = other.getVelocity();
            double m1 = getMass();
            double m2 = other.getMass();

            setVelocity(v1*((m1-m2)/(m1+m2))+(v2*(2*m2/(m1+m2))));
            other.setVelocity(v1*(2*m1/(m1+m2))+(v2*((m2-m1)/(m1+m2))));
            move();
            other.move();
        }
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getMass() {
        return mass;
    }

    public double getVelocity() {
        return velocity;
    }

    public Color getColor() {
        return color;
    }

    public double getMu() {
        return mu;
    }

    public void setxPos(double x) {
        xPos = x;
    }

    public void setyPos(double y) {
        yPos = y;
    }

    public void setWidth(double w) {
        width = w;
    }

    public void setHeight(double h) {
        height = h;
    }

    public void setMass(double m) {
        mass = m;
    }

    public void setVelocity(double v) {
        velocity = v;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public void enableFriction(boolean b) {
        friction = b;
    }
}
