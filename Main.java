import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Main extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private ArrayList<Entity> objects, boundaries;
    private ArrayList<Double> storedVelocities;
    private Entity ent, entFill, entInfo;
    private JPopupMenu menu;
    private NewBlockPopUp attributes;
    private SimpleButton pause, play;
    private boolean frictionEnable, showInformation, pausedState;
    private int index;
    private Cursor blank;

    private Main() {
        // Supplemental blocks used to show placement of new blocks and block information
        ent = new Block(0,0,0,0,0);
        entFill = new Block(0,0,0,0,0);
        entInfo = new Block(0,0,0,0,0);

        // Walls
        Immovable left = new Immovable(0,0,10,HEIGHT);
        Immovable right = new Immovable(WIDTH-25,0,10,HEIGHT);
        Immovable top = new Immovable(0,0,WIDTH,10);

        // List of objects to be drawn
        objects = new ArrayList<>();
        boundaries = new ArrayList<>(Arrays.asList(left,right,top));

        Timer timer = new Timer(1, this);
        timer.start();

        addMouseListener(this);
        addMouseMotionListener(this);

        // Right-click Menu
        menu = new JPopupMenu();
        JMenu newBlock = new JMenu("New Block");
            JMenuItem defaultBlock = new JMenuItem("Default");
            defaultBlock.addActionListener(e -> createDefaultBlock());
            JMenuItem customBlock = new JMenuItem("Custom");
            customBlock.addActionListener(e -> createNewBlock());
        newBlock.add(defaultBlock);
        newBlock.add(customBlock);

        JCheckBoxMenuItem friction = new JCheckBoxMenuItem("Enable Friction");
        friction.addActionListener(e -> frictionEnable = friction.getState());
        menu.add(newBlock);
        menu.add(friction);

        pause = new PauseButton(WIDTH - 50, 20, 20, 20);
        play = new PlayButton(WIDTH - 50, 20, 20, 20);

        storedVelocities = new ArrayList<>();

        // Construction of a blank cursor
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        blank = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Collision Simulator");
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Main());
        frame.setVisible(true);
    }

    public void paint(Graphics window) {
        super.paint(window);
        window.setColor(Color.BLACK);
        window.fillRect(0,HEIGHT-50, WIDTH,50);
        for (Entity e : objects) {
            e.draw(window);
        }
        for (Entity e : boundaries) {
            e.draw(window);
        }
        drawRect(window, ent);
        fillRect(window, entFill);
        drawInformation(window, entInfo);
        // Determines whether to draw pause or play button
        if (!pausedState)
            pause.draw(window);
        else
            play.draw(window);
    }

    // Draws black highlight around selected block
    private void drawRect(Graphics window, Entity e) {
        Graphics2D g2d = (Graphics2D) window;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new Rectangle2D.Double(e.getxPos(), e.getyPos(), e.getWidth(), e.getHeight()));
    }

    // Draws "tentative" location of new block as user decides the location of the block
    private void fillRect(Graphics window, Entity e) {
        Graphics2D g2d = (Graphics2D) window;
        g2d.setColor(e.getColor());
        g2d.setStroke(new BasicStroke(5));
        g2d.fill(new Rectangle2D.Double(e.getxPos(), e.getyPos(), e.getWidth(), e.getHeight()));
    }

    // Draws the information of block described by Entity e
    private void drawInformation(Graphics window, Entity e) {
        if (showInformation) {
            Graphics2D g2d = (Graphics2D) window;
            g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            g2d.drawString("Mass = " + e.getMass(), (int) e.getxPos(), (int) e.getyPos() - 40);
            double vel = !pausedState ? e.getVelocity() : storedVelocities.get(index);
            g2d.drawString(String.format("Velocity = %.4f", vel), (int) e.getxPos(), (int) e.getyPos() - 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Detects collision of objects against the walls
        for (Entity wall : boundaries) {
            for (Entity obj : objects) {
                ((Immovable) wall).collision(obj);
            }
        }
        // Detects the collision of blocks against other blocks
        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < objects.size(); j++) {
                // If statement included so that each block only collides against each other once
                // Combinations of blocks are selected, not permutations as order does not matter
                if (j > i)
                    objects.get(i).elasticCollision(objects.get(j));
            }
        }
        for (Entity obj : objects) {
            obj.move();
            obj.enableFriction(frictionEnable);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            showInformation = false;
            menu.show(e.getComponent(), e.getX(), e.getY());
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).isInside(e.getX(), e.getY())) {
                    JPopupMenu options = new JPopupMenu();

                    JMenuItem delete = new JMenuItem("delete");
                    delete.setForeground(Color.RED);
                    int finalI = i;
                    delete.addActionListener(event -> removeEntity(finalI));

                    options.add(delete);
                    options.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        if (e.getButton() == 1) {
            boolean inside = false;
            int index = 0;
            for (Entity o : objects) {
                if (o.isInside(e.getX(), e.getY())) {
                    showInformation = true;
                    entInfo = o;
                    ent = o;
                    this.index = index;
                    inside = true;
                }
                index++;
            }
            if (!inside) {
                ent = new Block(0, 0, 0, 0, 0);
                showInformation = false;
            }
        }

        if (e.getButton() == 1) {
            // If paused, then draw still blocks and store the velocities in an ArrayList
            if (pause.isInside(e.getX(), e.getY()) && !pausedState) {
                pausedState = true;
                for (Entity entity : objects) {
                    storedVelocities.add(entity.getVelocity());
                    entity.setVelocity(0);
                }
            }
            // If played, then set the blocks' velocities to the stored velocities
            else if (play.isInside(e.getX(), e.getY())) {
                pausedState = false;
                for (int i = 0; i < objects.size(); i++) {
                    objects.get(i).setVelocity(storedVelocities.get(i));
                }
                storedVelocities.clear();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    // Creates a default block of mass 5, velocity 1, and color black
    private void createDefaultBlock() {
        double[] prevVelocity = new double[objects.size()];
        for (int i = 0; i < prevVelocity.length; i++) {
            prevVelocity[i] = objects.get(i).getVelocity();
            objects.get(i).setVelocity(0.0);
        }

        setCursor(blank);

        MouseMotionListener motion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}
            @Override
            public void mouseMoved(MouseEvent e) {
                Block newLocation = new Block(e.getX(), HEIGHT - 50, 5, 1.0, Color.BLACK);
                entFill = newLocation;
                if (objects.size() > 0) {
                    for (Entity o : objects) {
                        if (!o.didCollide(newLocation) && !newLocation.didCollide(o)) {
                            entFill = newLocation;
                        }
                        else {
                            entFill = new Block(0, 0, 0, 0, 0);
                        }
                    }
                }
            }
        };

        MouseListener click = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    entFill = new Block(0, 0, 0, 0, 0);
                     for (int i = 0; i < prevVelocity.length; i++) {
                         objects.get(i).setVelocity(prevVelocity[i]);
                     }

                     Block newBlock = new Block(e.getX(), HEIGHT - 50, 5, -1.0, Color.BLACK);
                     if (pausedState) {
                         storedVelocities.add(newBlock.getVelocity());
                         newBlock.setVelocity(0.0);
                     }
                     newBlock.enableFriction(false);
                     objects.add(newBlock);

                     removeMouseMotionListener(motion);
                     removeMouseListener(this);
                     setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        addMouseMotionListener(motion);
        addMouseListener(click);
    }

    // Creates a custom block depending on user input
    private void createNewBlock() {
        attributes = new NewBlockPopUp();
        if (attributes.getStatus()) {
            double[] prevVelocity = new double[objects.size()];
            for (int i = 0; i < prevVelocity.length; i++) {
                prevVelocity[i] = objects.get(i).getVelocity();
                objects.get(i).setVelocity(0.0);
            }

            setCursor(blank);

            MouseMotionListener motion = new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {}

                @Override
                public void mouseMoved(MouseEvent e) {
                    Block newLocation = new Block(e.getX(), HEIGHT - 50, attributes.getM(), attributes.getV() * Math.cos(attributes.getDir()), attributes.getCol());
                    entFill = newLocation;
                    if (objects.size() > 0) {
                        for (Entity o : objects) {
                            if (!o.didCollide(newLocation) && !newLocation.didCollide(o)) {
                                entFill = newLocation;
                            }
                            else {
                                entFill = new Block(0, 0, 0, 0, 0);
                            }
                        }
                    }
                }
            };

            MouseListener click = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        entFill = new Block(0, 0, 0, 0, 0);

                        for (int i = 0; i < prevVelocity.length; i++) {
                            objects.get(i).setVelocity(prevVelocity[i]);
                        }

                        Block newBlock = new Block(e.getX(), HEIGHT - 50, attributes.getM(), attributes.getV() * Math.cos(attributes.getDir()), attributes.getCol());
                        if (pausedState) {
                            storedVelocities.add(newBlock.getVelocity());
                            newBlock.setVelocity(0.0);
                        }
                        newBlock.enableFriction(false);
                        objects.add(newBlock);

                        removeMouseMotionListener(motion);
                        removeMouseListener(this);
                        setCursor(Cursor.getDefaultCursor());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            };
            addMouseMotionListener(motion);
            addMouseListener(click);
        }
    }

    // Removes the entity at the given index
    private void removeEntity(int index) {
        objects.remove(index);
    }
}