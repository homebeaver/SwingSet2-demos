/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.accessibility.AccessibleRelation;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * JComboBox Demo
 *
 * @author Jeff Dinkins
 */
public class ComboBoxDemo extends DemoModule implements ActionListener {

    public static final String ICON_PATH = "toolbar/JComboBox.gif";

    Face face;
    JLabel faceLabel;

    JComboBox hairCB;
    JComboBox eyesCB;
    JComboBox mouthCB;

    JComboBox presetCB;

    Hashtable parts = new Hashtable();

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        ComboBoxDemo demo = new ComboBoxDemo(null);
        demo.mainImpl();
    }

    /**
     * ComboBoxDemo Constructor
     */
    public ComboBoxDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
        super(swingset, "ComboBoxDemo", ICON_PATH);

        createComboBoxDemo();
    }

    public void createComboBoxDemo() {
        JPanel demo = getDemoPanel();

        JPanel demoPanel = getDemoPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

        demoPanel.add(Box.createRigidArea(VGAP20));
        demoPanel.add(innerPanel);
        demoPanel.add(Box.createRigidArea(VGAP20));

        innerPanel.add(Box.createRigidArea(HGAP20));

        // Create a panel to hold buttons
        JPanel comboBoxPanel = new JPanel() {
                public Dimension getMaximumSize() {
                    return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
                }
        };
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));

        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        JLabel l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.presets")));
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        presetCB = (JComboBox) comboBoxPanel.add(createPresetComboBox());
        presetCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(presetCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP30));

        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.hair_description")));
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        hairCB = (JComboBox) comboBoxPanel.add(createHairComboBox());
        hairCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(hairCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.eyes_description")));
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        eyesCB = (JComboBox) comboBoxPanel.add(createEyesComboBox());
        eyesCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(eyesCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.mouth_description")));
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        mouthCB = (JComboBox) comboBoxPanel.add(createMouthComboBox());
        mouthCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(mouthCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        // Fill up the remaining space
        comboBoxPanel.add(new JPanel(new BorderLayout()));

        // Create and place the Face.

        face = new Face();
        JPanel facePanel = new JPanel();
        facePanel.setLayout(new BorderLayout());
        facePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        faceLabel = new JLabel(face);
        facePanel.add(faceLabel, BorderLayout.CENTER);
        // Indicate that the face panel is controlled by the hair, eyes and
        // mouth combo boxes.
        Object [] controlledByObjects = new Object[3];
        controlledByObjects[0] = hairCB;
        controlledByObjects[1] = eyesCB;
        controlledByObjects[2] = mouthCB;
        AccessibleRelation controlledByRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLED_BY_PROPERTY,
                                   controlledByObjects);
        facePanel.getAccessibleContext().getAccessibleRelationSet().add(controlledByRelation);

        // Indicate that the hair, eyes and mouth combo boxes are controllers
        // for the face panel.
        AccessibleRelation controllerForRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR_PROPERTY,
                                   facePanel);
        hairCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        eyesCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        mouthCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);

        // add buttons and image panels to inner panel
        innerPanel.add(comboBoxPanel);
        innerPanel.add(Box.createRigidArea(HGAP30));
        innerPanel.add(facePanel);
        innerPanel.add(Box.createRigidArea(HGAP20));

        // load up the face parts
        addFace("brent",     getString("ComboBoxDemo.brent"));
        addFace("georges",   getString("ComboBoxDemo.georges"));
        addFace("hans",      getString("ComboBoxDemo.hans"));
        addFace("howard",    getString("ComboBoxDemo.howard"));
        addFace("james",     getString("ComboBoxDemo.james"));
        addFace("jeff",      getString("ComboBoxDemo.jeff"));
        addFace("jon",       getString("ComboBoxDemo.jon"));
        addFace("lara",      getString("ComboBoxDemo.lara"));
        addFace("larry",     getString("ComboBoxDemo.larry"));
        addFace("lisa",      getString("ComboBoxDemo.lisa"));
        addFace("michael",   getString("ComboBoxDemo.michael"));
        addFace("philip",    getString("ComboBoxDemo.philip"));
        addFace("scott",     getString("ComboBoxDemo.scott"));

        // set the default face
        presetCB.setSelectedIndex(0);
    }

    void addFace(String name, String i18n_name) {
        ImageIcon i;
        String i18n_hair = getString("ComboBoxDemo.hair");
        String i18n_eyes = getString("ComboBoxDemo.eyes");
        String i18n_mouth = getString("ComboBoxDemo.mouth");

        parts.put(i18n_name, name); // i18n name lookup
        parts.put(name, i18n_name); // reverse name lookup

        i = createImageIcon("combobox/" + name + "hair.jpg", i18n_name + i18n_hair);
        parts.put(name +  "hair", i);

        i = createImageIcon("combobox/" + name + "eyes.jpg", i18n_name + i18n_eyes);
        parts.put(name +  "eyes", i);

        i = createImageIcon("combobox/" + name + "mouth.jpg", i18n_name + i18n_mouth);
        parts.put(name +  "mouth", i);
    }

    Face getFace() {
        return face;
    }

    JComboBox createHairComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createEyesComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createNoseComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createMouthComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createPresetComboBox() {
        JComboBox cb = new JComboBox();
        cb.addItem(getString("ComboBoxDemo.preset1"));
        cb.addItem(getString("ComboBoxDemo.preset2"));
        cb.addItem(getString("ComboBoxDemo.preset3"));
        cb.addItem(getString("ComboBoxDemo.preset4"));
        cb.addItem(getString("ComboBoxDemo.preset5"));
        cb.addItem(getString("ComboBoxDemo.preset6"));
        cb.addItem(getString("ComboBoxDemo.preset7"));
        cb.addItem(getString("ComboBoxDemo.preset8"));
        cb.addItem(getString("ComboBoxDemo.preset9"));
        cb.addItem(getString("ComboBoxDemo.preset10"));
        cb.addActionListener(this);
        return cb;
    }

    void fillComboBox(JComboBox cb) {
        cb.addItem(getString("ComboBoxDemo.brent"));
        cb.addItem(getString("ComboBoxDemo.georges"));
        cb.addItem(getString("ComboBoxDemo.hans"));
        cb.addItem(getString("ComboBoxDemo.howard"));
        cb.addItem(getString("ComboBoxDemo.james"));
        cb.addItem(getString("ComboBoxDemo.jeff"));
        cb.addItem(getString("ComboBoxDemo.jon"));
        cb.addItem(getString("ComboBoxDemo.lara"));
        cb.addItem(getString("ComboBoxDemo.larry"));
        cb.addItem(getString("ComboBoxDemo.lisa"));
        cb.addItem(getString("ComboBoxDemo.michael"));
        cb.addItem(getString("ComboBoxDemo.philip"));
        cb.addItem(getString("ComboBoxDemo.scott"));
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == hairCB) {
            String name = (String) parts.get((String) hairCB.getSelectedItem());
            face.setHair((ImageIcon) parts.get(name + "hair"));
            faceLabel.repaint();
        } else if(e.getSource() == eyesCB) {
            String name = (String) parts.get((String) eyesCB.getSelectedItem());
            face.setEyes((ImageIcon) parts.get(name + "eyes"));
            faceLabel.repaint();
        } else if(e.getSource() == mouthCB) {
            String name = (String) parts.get((String) mouthCB.getSelectedItem());
            face.setMouth((ImageIcon) parts.get(name + "mouth"));
            faceLabel.repaint();
        } else if(e.getSource() == presetCB) {
            String hair = null;
            String eyes = null;
            String mouth = null;
            switch(presetCB.getSelectedIndex()) {
               case 0:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("howard");
                   mouth = (String) parts.get("jeff");
                   break;
               case 1:
                   hair = (String) parts.get("jeff");
                   eyes = (String) parts.get("larry");
                   mouth = (String) parts.get("philip");
                   break;
               case 2:
                   hair = (String) parts.get("howard");
                   eyes = (String) parts.get("scott");
                   mouth = (String) parts.get("hans");
                   break;
               case 3:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("jeff");
                   mouth = (String) parts.get("hans");
                   break;
               case 4:
                   hair = (String) parts.get("brent");
                   eyes = (String) parts.get("jon");
                   mouth = (String) parts.get("scott");
                   break;
               case 5:
                   hair = (String) parts.get("lara");
                   eyes = (String) parts.get("larry");
                   mouth = (String) parts.get("lisa");
                   break;
               case 6:
                   hair = (String) parts.get("james");
                   eyes = (String) parts.get("philip");
                   mouth = (String) parts.get("michael");
                   break;
               case 7:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("lisa");
                   mouth = (String) parts.get("brent");
                   break;
               case 8:
                   hair = (String) parts.get("james");
                   eyes = (String) parts.get("philip");
                   mouth = (String) parts.get("jon");
                   break;
               case 9:
                   hair = (String) parts.get("lara");
                   eyes = (String) parts.get("jon");
                   mouth = (String) parts.get("scott");
                   break;
            }
            if(hair != null) {
                hairCB.setSelectedItem(hair);
                eyesCB.setSelectedItem(eyes);
                mouthCB.setSelectedItem(mouth);
                faceLabel.repaint();
            }
        }
    }

    class Face implements Icon {
        ImageIcon hair;
        ImageIcon eyes;
        ImageIcon mouth;

        void setHair(ImageIcon i) {
            hair = i;
        }

        void setEyes(ImageIcon i) {
            eyes = i;
        }

        void setMouth(ImageIcon i) {
            mouth = i;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int height = y;
            x = c.getWidth()/2 - getIconWidth()/2;

            if(hair != null) {
                hair.paintIcon(c, g, x, height);   height += hair.getIconHeight();
            }

            if(eyes != null) {
                eyes.paintIcon(c, g, x, height);   height += eyes.getIconHeight();
            }

            if(mouth != null) {
                mouth.paintIcon(c, g, x, height);
            }
        }

        public int getIconWidth() {
            return 344;
        }

        public int getIconHeight() {
            return 455;
        }
    }
}
