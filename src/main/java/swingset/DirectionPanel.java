/*
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package swingset;

import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;


/**
 * @author Jeff Dinkins
 * @author Chester Rose
 * @author Brian Beck
 */
public class DirectionPanel extends JPanel {

    private ButtonGroup group;

    public DirectionPanel(boolean enable, String selection, ActionListener l) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);
        setAlignmentX(LEFT_ALIGNMENT);

        Box firstThree = Box.createHorizontalBox();
        Box secondThree = Box.createHorizontalBox();
        Box thirdThree = Box.createHorizontalBox();

        if(!enable) {
            selection = "None";
        }

        group = new ButtonGroup();
        DirectionButton b;
        b = (DirectionButton) firstThree.add(new DirectionButton(  tl_dot, tldn_dot, "NW", "Sets the orientation to the North-West", l, group, selection.equals("NW")));
        b.setEnabled(enable);
        b = (DirectionButton) firstThree.add(new DirectionButton(  tm_dot, tmdn_dot, "N",  "Sets the orientation to the North", l, group, selection.equals("N")));
        b.setEnabled(enable);
        b = (DirectionButton) firstThree.add(new DirectionButton(  tr_dot, trdn_dot, "NE", "Sets the orientation to the North-East", l, group, selection.equals("NE")));
        b.setEnabled(enable);
        b = (DirectionButton) secondThree.add(new DirectionButton( ml_dot, mldn_dot, "W", "Sets the orientation to the West", l, group, selection.equals("W")));
        b.setEnabled(enable);
        b = (DirectionButton) secondThree.add(new DirectionButton( c_dot,  cdn_dot,  "C", "Sets the orientation to the Center", l, group, selection.equals("C")));
        b.setEnabled(enable);
        b = (DirectionButton) secondThree.add(new DirectionButton( mr_dot, mrdn_dot, "E", "Sets the orientation to the East", l, group, selection.equals("E")));
        b.setEnabled(enable);
        b = (DirectionButton) thirdThree.add(new DirectionButton(  bl_dot, bldn_dot, "SW", "Sets the orientation to the South-West", l, group, selection.equals("SW")));
        b.setEnabled(enable);
        b = (DirectionButton) thirdThree.add(new DirectionButton(  bm_dot, bmdn_dot, "S", "Sets the orientation to the South", l, group, selection.equals("S")));
        b.setEnabled(enable);
        b = (DirectionButton) thirdThree.add(new DirectionButton(  br_dot, brdn_dot, "SE", "Sets the orientation to the South-East", l, group, selection.equals("SE")));
        b.setEnabled(enable);

        add(firstThree);
        add(secondThree);
        add(thirdThree);
    }

    public String getSelection() {
        return group.getSelection().getActionCommand();
    }

    public void setSelection( String selection  ) {
        Enumeration<?> e = group.getElements();
        while( e.hasMoreElements() ) {
            JRadioButton b = (JRadioButton)e.nextElement();
            if( b.getActionCommand().equals(selection) ) {
               b.setSelected(true);
            }
        }
    }

    // Chester's way cool layout buttons
    public ImageIcon bl_dot   = loadImageIcon("bl.gif","bottom left layout button");
    public ImageIcon bldn_dot = loadImageIcon("bldn.gif","selected bottom left layout button");
    public ImageIcon bm_dot   = loadImageIcon("bm.gif","bottom middle layout button");
    public ImageIcon bmdn_dot = loadImageIcon("bmdn.gif","selected bottom middle layout button");
    public ImageIcon br_dot   = loadImageIcon("br.gif","bottom right layout button");
    public ImageIcon brdn_dot = loadImageIcon("brdn.gif","selected bottom right layout button");
    public ImageIcon c_dot    = loadImageIcon("c.gif","center layout button");
    public ImageIcon cdn_dot  = loadImageIcon("cdn.gif","selected center layout button");
    public ImageIcon ml_dot   = loadImageIcon("ml.gif","middle left layout button");
    public ImageIcon mldn_dot = loadImageIcon("mldn.gif","selected middle left layout button");
    public ImageIcon mr_dot   = loadImageIcon("mr.gif","middle right layout button");
    public ImageIcon mrdn_dot = loadImageIcon("mrdn.gif","selected middle right layout button");
    public ImageIcon tl_dot   = loadImageIcon("tl.gif","top left layout button");
    public ImageIcon tldn_dot = loadImageIcon("tldn.gif","selected top left layout button");
    public ImageIcon tm_dot   = loadImageIcon("tm.gif","top middle layout button");
    public ImageIcon tmdn_dot = loadImageIcon("tmdn.gif","selected top middle layout button");
    public ImageIcon tr_dot   = loadImageIcon("tr.gif","top right layout button");
    public ImageIcon trdn_dot = loadImageIcon("trdn.gif","selected top right layout button");

    public ImageIcon loadImageIcon(String filename, String description) {
    	String path = "/swingset/images/buttons/" + filename;
        return new ImageIcon(getClass().getResource(path), description);
    }


    public class DirectionButton extends JRadioButton {

        /**
         * A layout direction button
         */
        public DirectionButton(Icon icon, Icon downIcon, String direction,
                               String description, ActionListener l,
                               ButtonGroup group, boolean selected)
        {
            super();
            this.addActionListener(l);
            setFocusPainted(false);
            setHorizontalTextPosition(CENTER);
            group.add(this);
            setIcon(icon);
            setSelectedIcon(downIcon);
            setActionCommand(direction);
            getAccessibleContext().setAccessibleName(direction);
            getAccessibleContext().setAccessibleDescription(description);
            setSelected(selected);
        }

        // isFocusTraversable() in java.awt.Component has been deprecated => use isFocusable
        @Deprecated
        public boolean isFocusTraversable() {
            return false;
        }
        public boolean isFocusable() {
            return false;
        }

        public void setBorder(Border b) {
        }
    }
}
