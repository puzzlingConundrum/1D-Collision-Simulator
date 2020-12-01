import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Hashtable;

public class NewBlockPopUp {
    private boolean createNew;
    private double m;
    private double v;
    private double dir;
    private Color c;
    private JSlider mass = new JSlider();
    private JSlider velocity = new JSlider();
    private JComboBox direction = new JComboBox(new String[]{"Left (Pi radians)", "Right (0 radians)"});
    private JColorChooser colChooser = new JColorChooser();

    public NewBlockPopUp() {
        mass.setMinimum(0);
        mass.setMaximum(200);
        mass.setMajorTickSpacing(50);
        Hashtable<Integer, JLabel> massMarks = new Hashtable<>();
        for (int i = 0; i <= 200; i += 50) {
            massMarks.put(new Integer(i), new JLabel(i/10.0+""));
        }
        mass.setLabelTable(massMarks);
        mass.setPaintLabels(true);

        velocity.setMinimum(0);
        velocity.setMaximum(20);
        velocity.setMajorTickSpacing(5);
        Hashtable<Integer, JLabel> velocityMarks = new Hashtable<>();
        for (int i = 0; i <= 20; i += 5) {
            velocityMarks.put(new Integer(i), new JLabel(i/10.0+""));
        }
        velocity.setLabelTable(velocityMarks);
        velocity.setPaintLabels(true);

        AbstractColorChooserPanel[] colorPanel = colChooser.getChooserPanels();
        for (int i = 0; i < colorPanel.length; i++) {
            if (i == 1 || i == 2 || i == 3 || i == 4) {
                colChooser.removeChooserPanel(colorPanel[i]);
            }
        }
        colChooser.setPreviewPanel(new JPanel());
        colChooser.setColor(Color.BLACK);

        Object[] message = {"Mass:",mass,"Velocity:",velocity,"Direction:",direction,"Color:",colChooser};
        int options = JOptionPane.showConfirmDialog(null,message,"Attributes",JOptionPane.OK_CANCEL_OPTION);
        if (options == JOptionPane.OK_OPTION) {
            m = mass.getValue()/10.0;
            v = velocity.getValue()/10.0;
            dir = direction.getSelectedIndex() == 0 ? Math.PI : 0.0;
            c = colChooser.getColor();
            createNew = true;
        }
    }

    public double getM() {
        return m;
    }

    public double getV() {
        return v;
    }

    public double getDir() {
        return dir;
    }

    public Color getCol() {
        return c;
    }

    public boolean getStatus() {
        return createNew;
    }
}
