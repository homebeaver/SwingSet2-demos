/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Table demo
 *
 * @author Philip Milne
 * @author Steve Wilson
 */
public class TableDemo extends DemoModule {
	
    public static final String ICON_PATH = "toolbar/JTable.gif";

    JTable      tableView;
    JScrollPane scrollpane;
    Dimension   origin = new Dimension(0, 0);

    JCheckBox   isColumnReorderingAllowedCheckBox;
    JCheckBox   showHorizontalLinesCheckBox;
    JCheckBox   showVerticalLinesCheckBox;

    JCheckBox   isColumnSelectionAllowedCheckBox;
    JCheckBox   isRowSelectionAllowedCheckBox;

    JLabel      interCellSpacingLabel;
    JLabel      rowHeightLabel;

    JSlider     interCellSpacingSlider;
    JSlider     rowHeightSlider;

    JComboBox   selectionModeComboBox = null;
    JComboBox   resizeModeComboBox = null;

    JLabel      headerLabel;
    JLabel      footerLabel;

    JTextField  headerTextField;
    JTextField  footerTextField;

    JCheckBox   fitWidth;
    JButton     printButton;

    JPanel      controlPanel;
    JScrollPane tableAggregate;

    String path = "ImageClub/food/";

    final int INITIAL_ROWHEIGHT = 33;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        TableDemo demo = new TableDemo(null);
        demo.mainImpl();
    }

    /**
     * TableDemo Constructor
     */
    public TableDemo(SwingSet2 swingset) {
        super(swingset, "TableDemo", ICON_PATH);

        getDemoPanel().setLayout(new BorderLayout());
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        JPanel cbPanel = new JPanel(new GridLayout(3, 2));
        JPanel labelPanel = new JPanel(new GridLayout(2, 1)) {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        JPanel sliderPanel = new JPanel(new GridLayout(2, 1)) {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        JPanel comboPanel = new JPanel(new GridLayout(2, 1));
        JPanel printPanel = new JPanel(new ColumnLayout());

        getDemoPanel().add(controlPanel, BorderLayout.NORTH);
        Vector relatedComponents = new Vector();


        // check box panel
        isColumnReorderingAllowedCheckBox = new JCheckBox(getString("TableDemo.reordering_allowed"), true);
        isColumnReorderingAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.getTableHeader().setReorderingAllowed(flag);
                tableView.repaint();
            }
        });

        showHorizontalLinesCheckBox = new JCheckBox(getString("TableDemo.horz_lines"), true);
        showHorizontalLinesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setShowHorizontalLines(flag); ;
                tableView.repaint();
            }
        });

        showVerticalLinesCheckBox = new JCheckBox(getString("TableDemo.vert_lines"), true);
        showVerticalLinesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setShowVerticalLines(flag); ;
                tableView.repaint();
            }
        });

        // Show that showHorizontal/Vertical controls are related
        relatedComponents.removeAllElements();
        relatedComponents.add(showHorizontalLinesCheckBox);
        relatedComponents.add(showVerticalLinesCheckBox);
        buildAccessibleGroup(relatedComponents);

        isRowSelectionAllowedCheckBox = new JCheckBox(getString("TableDemo.row_selection"), true);
        isRowSelectionAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setRowSelectionAllowed(flag); ;
                tableView.repaint();
            }
        });

        isColumnSelectionAllowedCheckBox = new JCheckBox(getString("TableDemo.column_selection"), false);
        isColumnSelectionAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setColumnSelectionAllowed(flag); ;
                tableView.repaint();
            }
        });

        // Show that row/column selections are related
        relatedComponents.removeAllElements();
        relatedComponents.add(isColumnSelectionAllowedCheckBox);
        relatedComponents.add(isRowSelectionAllowedCheckBox);
        buildAccessibleGroup(relatedComponents);

        cbPanel.add(isColumnReorderingAllowedCheckBox);
        cbPanel.add(isRowSelectionAllowedCheckBox);
        cbPanel.add(showHorizontalLinesCheckBox);
        cbPanel.add(isColumnSelectionAllowedCheckBox);
        cbPanel.add(showVerticalLinesCheckBox);


        // label panel
        interCellSpacingLabel = new JLabel(getString("TableDemo.intercell_spacing_colon"));
        labelPanel.add(interCellSpacingLabel);

        rowHeightLabel = new JLabel(getString("TableDemo.row_height_colon"));
        labelPanel.add(rowHeightLabel);


        // slider panel
        interCellSpacingSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 1);
        interCellSpacingSlider.getAccessibleContext().setAccessibleName(getString("TableDemo.intercell_spacing"));
        interCellSpacingLabel.setLabelFor(interCellSpacingSlider);
        sliderPanel.add(interCellSpacingSlider);
        interCellSpacingSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int spacing = ((JSlider)e.getSource()).getValue();
                tableView.setIntercellSpacing(new Dimension(spacing, spacing));
                tableView.repaint();
            }
        });

        rowHeightSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, INITIAL_ROWHEIGHT);
        rowHeightSlider.getAccessibleContext().setAccessibleName(getString("TableDemo.row_height"));
        rowHeightLabel.setLabelFor(rowHeightSlider);
        sliderPanel.add(rowHeightSlider);
        rowHeightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int height = ((JSlider)e.getSource()).getValue();
                tableView.setRowHeight(height);
                tableView.repaint();
            }
        });

        // Show that spacing controls are related
        relatedComponents.removeAllElements();
        relatedComponents.add(interCellSpacingSlider);
        relatedComponents.add(rowHeightSlider);
        buildAccessibleGroup(relatedComponents);


        // Create the table.
        tableAggregate = createTable();
        getDemoPanel().add(tableAggregate, BorderLayout.CENTER);


        // ComboBox for selection modes.
        JPanel selectMode = new JPanel();
        selectMode.setLayout(new BoxLayout(selectMode, BoxLayout.X_AXIS));
        selectMode.setBorder(new TitledBorder(getString("TableDemo.selection_mode")));


        selectionModeComboBox = new JComboBox() {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        selectionModeComboBox.addItem(getString("TableDemo.single"));
        selectionModeComboBox.addItem(getString("TableDemo.one_range"));
        selectionModeComboBox.addItem(getString("TableDemo.multiple_ranges"));
        selectionModeComboBox.setSelectedIndex(tableView.getSelectionModel().getSelectionMode());
        selectionModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox source = (JComboBox)e.getSource();
                tableView.setSelectionMode(source.getSelectedIndex());
            }
        });

        selectMode.add(Box.createHorizontalStrut(2));
        selectMode.add(selectionModeComboBox);
        selectMode.add(Box.createHorizontalGlue());
        comboPanel.add(selectMode);

        // Combo box for table resize mode.
        JPanel resizeMode = new JPanel();
        resizeMode.setLayout(new BoxLayout(resizeMode, BoxLayout.X_AXIS));
        resizeMode.setBorder(new TitledBorder(getString("TableDemo.autoresize_mode")));


        resizeModeComboBox = new JComboBox() {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        resizeModeComboBox.addItem(getString("TableDemo.off"));
        resizeModeComboBox.addItem(getString("TableDemo.column_boundaries"));
        resizeModeComboBox.addItem(getString("TableDemo.subsequent_columns"));
        resizeModeComboBox.addItem(getString("TableDemo.last_column"));
        resizeModeComboBox.addItem(getString("TableDemo.all_columns"));
        resizeModeComboBox.setSelectedIndex(tableView.getAutoResizeMode());
        resizeModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox source = (JComboBox)e.getSource();
                tableView.setAutoResizeMode(source.getSelectedIndex());
            }
        });

        resizeMode.add(Box.createHorizontalStrut(2));
        resizeMode.add(resizeModeComboBox);
        resizeMode.add(Box.createHorizontalGlue());
        comboPanel.add(resizeMode);

        // print panel
        printPanel.setBorder(new TitledBorder(getString("TableDemo.printing")));
        headerLabel = new JLabel(getString("TableDemo.header"));
        footerLabel = new JLabel(getString("TableDemo.footer"));
        headerTextField = new JTextField(getString("TableDemo.headerText"), 15);
        footerTextField = new JTextField(getString("TableDemo.footerText"), 15);
        fitWidth = new JCheckBox(getString("TableDemo.fitWidth"), true);
        printButton = new JButton(getString("TableDemo.print"));
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printTable();
            }
        });

        printPanel.add(headerLabel);
        printPanel.add(headerTextField);
        printPanel.add(footerLabel);
        printPanel.add(footerTextField);

        JPanel buttons = new JPanel();
        buttons.add(fitWidth);
        buttons.add(printButton);

        printPanel.add(buttons);

        // Show that printing controls are related
        relatedComponents.removeAllElements();
        relatedComponents.add(headerTextField);
        relatedComponents.add(footerTextField);
        relatedComponents.add(printButton);
        buildAccessibleGroup(relatedComponents);

        // wrap up the panels and add them
        JPanel sliderWrapper = new JPanel();
        sliderWrapper.setLayout(new BoxLayout(sliderWrapper, BoxLayout.X_AXIS));
        sliderWrapper.add(labelPanel);
        sliderWrapper.add(sliderPanel);
        sliderWrapper.add(Box.createHorizontalGlue());
        sliderWrapper.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JPanel leftWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        leftWrapper.add(cbPanel);
        leftWrapper.add(sliderWrapper);

        // add everything
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        controlPanel.add(leftWrapper);
        controlPanel.add(comboPanel);
        controlPanel.add(printPanel);

        setTableControllers(); // Set accessibility information

        getDemoPanel().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("ctrl P"), "print");

        getDemoPanel().getActionMap().put("print", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                printTable();
            }
        });

    } // TableDemo()

    /**
     * Sets the Accessibility MEMBER_OF property to denote that
     * these components work together as a group. Each object
     * is set to be a MEMBER_OF an array that contains all of
     * the objects in the group, including itself.
     *
     * @param components The list of objects that are related
     */
    void buildAccessibleGroup(Vector components) {

        AccessibleContext context = null;
        int numComponents = components.size();
        Object[] group = components.toArray();
        Object object = null;
        for (int i = 0; i < numComponents; ++i) {
            object = components.elementAt(i);
            if (object instanceof Accessible) {
                context = ((Accessible)components.elementAt(i)).
                                                 getAccessibleContext();
                context.getAccessibleRelationSet().add(
                    new AccessibleRelation(
                        AccessibleRelation.MEMBER_OF, group));
            }
        }
    } // buildAccessibleGroup()

    /**
     * This sets CONTROLLER_FOR on the controls that manipulate the
     * table and CONTROLLED_BY relationships on the table to point
     * back to the controllers.
     */
    private void setTableControllers() {

        // Set up the relationships to show what controls the table
        setAccessibleController(isColumnReorderingAllowedCheckBox,
                                tableAggregate);
        setAccessibleController(showHorizontalLinesCheckBox,
                                tableAggregate);
        setAccessibleController(showVerticalLinesCheckBox,
                                tableAggregate);
        setAccessibleController(isColumnSelectionAllowedCheckBox,
                                tableAggregate);
        setAccessibleController(isRowSelectionAllowedCheckBox,
                                tableAggregate);
        setAccessibleController(interCellSpacingSlider,
                                tableAggregate);
        setAccessibleController(rowHeightSlider,
                                tableAggregate);
        setAccessibleController(selectionModeComboBox,
                                tableAggregate);
        setAccessibleController(resizeModeComboBox,
                                tableAggregate);
    } // setTableControllers()

    /**
     * Sets up accessibility relationships to denote that one
     * object controls another. The CONTROLLER_FOR property is
     * set on the controller object, and the CONTROLLED_BY
     * property is set on the target object.
     */
    private void setAccessibleController(JComponent controller,
                                        JComponent target) {
        AccessibleRelationSet controllerRelations =
            controller.getAccessibleContext().getAccessibleRelationSet();
        AccessibleRelationSet targetRelations =
            target.getAccessibleContext().getAccessibleRelationSet();

        controllerRelations.add(
            new AccessibleRelation(
                AccessibleRelation.CONTROLLER_FOR, target));
        targetRelations.add(
            new AccessibleRelation(
                AccessibleRelation.CONTROLLED_BY, controller));
    } // setAccessibleController()

    public JScrollPane createTable() {

        // final
        final String[] names = {
          getString("TableDemo.first_name"),
          getString("TableDemo.last_name"),
          getString("TableDemo.favorite_color"),
          getString("TableDemo.favorite_movie"),
          getString("TableDemo.favorite_number"),
          getString("TableDemo.favorite_food")
        };

        ImageIcon apple        = createImageIcon("ImageClub/food/apple.jpg",      getString("TableDemo.apple"));
        ImageIcon asparagus    = createImageIcon("ImageClub/food/asparagus.jpg",  getString("TableDemo.asparagus"));
        ImageIcon banana       = createImageIcon("ImageClub/food/banana.jpg",     getString("TableDemo.banana"));
        ImageIcon broccoli     = createImageIcon("ImageClub/food/broccoli.jpg",   getString("TableDemo.broccoli"));
        ImageIcon cantaloupe   = createImageIcon("ImageClub/food/cantaloupe.jpg", getString("TableDemo.cantaloupe"));
        ImageIcon carrot       = createImageIcon("ImageClub/food/carrot.jpg",     getString("TableDemo.carrot"));
        ImageIcon corn         = createImageIcon("ImageClub/food/corn.jpg",       getString("TableDemo.corn"));
// intentionally added : MissingResource: Couldn't find value for: TableDemo.donut and image
        ImageIcon donut        = createImageIcon("ImageClub/food/donut.jpg",      getString("TableDemo.donut"));
        ImageIcon grapes       = createImageIcon("ImageClub/food/grapes.jpg",     getString("TableDemo.grapes"));
        ImageIcon grapefruit   = createImageIcon("ImageClub/food/grapefruit.jpg", getString("TableDemo.grapefruit"));
        ImageIcon kiwi         = createImageIcon("ImageClub/food/kiwi.jpg",       getString("TableDemo.kiwi"));
        ImageIcon onion        = createImageIcon("ImageClub/food/onion.jpg",      getString("TableDemo.onion"));
        ImageIcon pear         = createImageIcon("ImageClub/food/pear.jpg",       getString("TableDemo.pear"));
        ImageIcon peach        = createImageIcon("ImageClub/food/peach.jpg",      getString("TableDemo.peach"));
        ImageIcon pepper       = createImageIcon("ImageClub/food/pepper.jpg",     getString("TableDemo.pepper"));
        ImageIcon pickle       = createImageIcon("ImageClub/food/pickle.jpg",     getString("TableDemo.pickle"));
        ImageIcon pineapple    = createImageIcon("ImageClub/food/pineapple.jpg",  getString("TableDemo.pineapple"));
        ImageIcon raspberry    = createImageIcon("ImageClub/food/raspberry.jpg",  getString("TableDemo.raspberry"));
        ImageIcon sparegrass   = createImageIcon("ImageClub/food/asparagus.jpg",  getString("TableDemo.sparegrass"));
        ImageIcon strawberry   = createImageIcon("ImageClub/food/strawberry.jpg", getString("TableDemo.strawberry"));
        ImageIcon tomato       = createImageIcon("ImageClub/food/tomato.jpg",     getString("TableDemo.tomato"));
        ImageIcon watermelon   = createImageIcon("ImageClub/food/watermelon.jpg", getString("TableDemo.watermelon"));

        NamedColor aqua        = new NamedColor(new Color(127, 255, 212), getString("TableDemo.aqua"));
        NamedColor beige       = new NamedColor(new Color(245, 245, 220), getString("TableDemo.beige"));
        NamedColor black       = new NamedColor(Color.black, getString("TableDemo.black"));
        NamedColor blue        = new NamedColor(new Color(0, 0, 222), getString("TableDemo.blue"));
        NamedColor eblue       = new NamedColor(Color.blue, getString("TableDemo.eblue"));
        NamedColor jfcblue     = new NamedColor(new Color(204, 204, 255), getString("TableDemo.jfcblue"));
        NamedColor jfcblue2    = new NamedColor(new Color(153, 153, 204), getString("TableDemo.jfcblue2"));
        NamedColor cybergreen  = new NamedColor(Color.green.darker().brighter(), getString("TableDemo.cybergreen"));
        NamedColor darkgreen   = new NamedColor(new Color(0, 100, 75), getString("TableDemo.darkgreen"));
        NamedColor forestgreen = new NamedColor(Color.green.darker(), getString("TableDemo.forestgreen"));
        NamedColor gray        = new NamedColor(Color.gray, getString("TableDemo.gray"));
        NamedColor green       = new NamedColor(Color.green, getString("TableDemo.green"));
        NamedColor orange      = new NamedColor(new Color(255, 165, 0), getString("TableDemo.orange"));
// intentionally added : MissingResource: Couldn't find value for: TableDemo.pink
        NamedColor pink        = new NamedColor(Color.PINK, getString("TableDemo.pink"));
        NamedColor purple      = new NamedColor(new Color(160, 32, 240),  getString("TableDemo.purple"));
        NamedColor red         = new NamedColor(Color.red, getString("TableDemo.red"));
        NamedColor rustred     = new NamedColor(Color.red.darker(), getString("TableDemo.rustred"));
        NamedColor sunpurple   = new NamedColor(new Color(100, 100, 255), getString("TableDemo.sunpurple"));
        NamedColor suspectpink = new NamedColor(new Color(255, 105, 180), getString("TableDemo.suspectpink"));
        NamedColor turquoise   = new NamedColor(new Color(0, 255, 255), getString("TableDemo.turquoise"));
        NamedColor violet      = new NamedColor(new Color(238, 130, 238), getString("TableDemo.violet"));
        NamedColor yellow      = new NamedColor(Color.yellow, getString("TableDemo.yellow"));

        // Create the dummy data (a few rows of names)
        //  "First Name", "Last Name", "Favorite Color", Movie from props, "image", "No.", "Vegetarian" 
        final Object[][] data = {
          {"Mike", "Albers",      green,       getString("TableDemo.brazil"), Double.valueOf(44.0), strawberry},
          {"Mark", "Andrews",     blue,        getString("TableDemo.curse"), Double.valueOf(3), grapes},
          {"Brian", "Beck",       black,       getString("TableDemo.bluesbros"), Double.valueOf(2.7182818285), raspberry},
          {"Lara", "Bunni",       red,         getString("TableDemo.airplane"), Double.valueOf(15), strawberry},
          {"Roger", "Brinkley",   blue,        getString("TableDemo.man"), Double.valueOf(13), peach},
          {"Brent", "Christian",  black,       getString("TableDemo.bladerunner"), Double.valueOf(23), broccoli},
          {"Mark", "Davidson",    darkgreen,   getString("TableDemo.brazil"), Double.valueOf(27), asparagus},
          {"Jeff", "Dinkins",     blue,        getString("TableDemo.ladyvanishes"), Double.valueOf(8), kiwi},
          {"Ewan", "Dinkins",     yellow,      getString("TableDemo.bugs"), Double.valueOf(2), strawberry},
          {"Amy", "Fowler",       violet,      getString("TableDemo.reservoir"), Double.valueOf(3), raspberry},
          {"Hania", "Gajewska",   purple,      getString("TableDemo.jules"), Double.valueOf(5), raspberry},
          {"David", "Geary",      blue,        getString("TableDemo.pulpfiction"), Double.valueOf(3), watermelon},
// intentionally aktivated : MissingResource: Couldn't find value for: TableDemo.tennis, TableDemo.donut, ...
        {"James", "Gosling",    pink,        getString("TableDemo.tennis"), Double.valueOf(21), donut},
          {"Eric", "Hawkes",      blue,        getString("TableDemo.bladerunner"), Double.valueOf(.693), pickle},
          {"Shannon", "Hickey",   green,       getString("TableDemo.shawshank"), Double.valueOf(2), grapes},
          {"Earl", "Johnson",     green,       getString("TableDemo.pulpfiction"), Double.valueOf(8), carrot},
          {"Robi", "Khan",        green,       getString("TableDemo.goodfellas"), Double.valueOf(89), apple},
          {"Robert", "Kim",       blue,        getString("TableDemo.mohicans"), Double.valueOf(655321), strawberry},
          {"Janet", "Koenig",     turquoise,   getString("TableDemo.lonestar"), Double.valueOf(7), peach},
          {"Jeff", "Kesselman",   blue,        getString("TableDemo.stuntman"), Double.valueOf(17), pineapple},
          {"Onno", "Kluyt",       orange,      getString("TableDemo.oncewest"), Double.valueOf(8), broccoli},
          {"Peter", "Korn",       sunpurple,   getString("TableDemo.musicman"), Double.valueOf(12), sparegrass},
          {"Rick", "Levenson",    black,       getString("TableDemo.harold"), Double.valueOf(1327), raspberry},
          {"Brian", "Lichtenwalter", jfcblue,  getString("TableDemo.fifthelement"), Double.valueOf(22), pear},
          {"Malini", "Minasandram", beige,     getString("TableDemo.joyluck"), Double.valueOf(9), corn},
          {"Michael", "Martak",   green,       getString("TableDemo.city"), Double.valueOf(3), strawberry},
          {"David", "Mendenhall", forestgreen, getString("TableDemo.schindlerslist"), Double.valueOf(7), peach},
          {"Phil", "Milne",       suspectpink, getString("TableDemo.withnail"), Double.valueOf(3), banana},
          {"Lynn", "Monsanto",    cybergreen,  getString("TableDemo.dasboot"), Double.valueOf(52), peach},
          {"Hans", "Muller",      rustred,     getString("TableDemo.eraserhead"), Double.valueOf(0), pineapple},
          {"Joshua", "Outwater",  blue,        getString("TableDemo.labyrinth"), Double.valueOf(3), pineapple},
          {"Tim", "Prinzing",     blue,        getString("TableDemo.firstsight"), Double.valueOf(69), pepper},
          {"Raj", "Premkumar",    jfcblue2,    getString("TableDemo.none"), Double.valueOf(7), broccoli},
          {"Howard", "Rosen",     green,       getString("TableDemo.defending"), Double.valueOf(7), strawberry},
          {"Ray", "Ryan",         black,       getString("TableDemo.buckaroo"),
           Double.valueOf(3.141592653589793238462643383279502884197169399375105820974944), banana},
          {"Georges", "Saab",     aqua,        getString("TableDemo.bicycle"), Double.valueOf(290), cantaloupe},
          {"Tom", "Santos",       blue,        getString("TableDemo.spinaltap"), Double.valueOf(241), pepper},
          {"Rich", "Schiavi",     blue,        getString("TableDemo.repoman"), Double.valueOf(0xFF), pepper},
          {"Nancy", "Schorr",     green,       getString("TableDemo.fifthelement"), Double.valueOf(47), watermelon},
          {"Keith", "Sprochi",    darkgreen,   getString("TableDemo.2001"), Double.valueOf(13), watermelon},
          {"Matt", "Tucker",      eblue,       getString("TableDemo.starwars"), Double.valueOf(2), broccoli},
          {"Dmitri", "Trembovetski", red,      getString("TableDemo.aliens"), Double.valueOf(222), tomato},
          {"Scott", "Violet",     violet,      getString("TableDemo.raiders"), Double.valueOf(-97), banana},
          {"Kathy", "Walrath",    darkgreen,   getString("TableDemo.thinman"), Double.valueOf(8), pear},
          {"Nathan", "Walrath",   black,       getString("TableDemo.chusingura"), Double.valueOf(3), grapefruit},
          {"Steve", "Wilson",     green,       getString("TableDemo.raiders"), Double.valueOf(7), onion},
          {"Kathleen", "Zelony",  gray,        getString("TableDemo.dog"), Double.valueOf(13), grapes}
        };

        // Create a model of the data.
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { return names.length; }
            public int getRowCount() { return data.length;}
            public Object getValueAt(int row, int col) {return data[row][col];}
            public String getColumnName(int column) {return names[column];}
            public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
            public boolean isCellEditable(int row, int col) {return col != 5;}
            public void setValueAt(Object aValue, int row, int column) { data[row][column] = aValue; }
         };


        // Create the table
        tableView = new JTable(dataModel);
        TableRowSorter sorter = new TableRowSorter(dataModel);
        tableView.setRowSorter(sorter);

        // Show colors by rendering them in their own color.
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                if (value instanceof NamedColor) {
                    NamedColor c = (NamedColor) value;
                    setBackground(c);
                    setForeground(c.getTextColor());
                    setText(c.toString());
                } else {
                    super.setValue(value);
                }
            }
        };

        // Create a combo box to show that you can use one in a table.
        JComboBox comboBox = new JComboBox();
        comboBox.addItem(aqua);
        comboBox.addItem(beige);
        comboBox.addItem(black);
        comboBox.addItem(blue);
        comboBox.addItem(eblue);
        comboBox.addItem(jfcblue);
        comboBox.addItem(jfcblue2);
        comboBox.addItem(cybergreen);
        comboBox.addItem(darkgreen);
        comboBox.addItem(forestgreen);
        comboBox.addItem(gray);
        comboBox.addItem(green);
        comboBox.addItem(orange);
        comboBox.addItem(purple);
        comboBox.addItem(red);
        comboBox.addItem(rustred);
        comboBox.addItem(sunpurple);
        comboBox.addItem(suspectpink);
        comboBox.addItem(turquoise);
        comboBox.addItem(violet);
        comboBox.addItem(yellow);

        TableColumn colorColumn = tableView.getColumn(getString("TableDemo.favorite_color"));
        // Use the combo box as the editor in the "Favorite Color" column.
        colorColumn.setCellEditor(new DefaultCellEditor(comboBox));

        colorRenderer.setHorizontalAlignment(JLabel.CENTER);
        colorColumn.setCellRenderer(colorRenderer);

        tableView.setRowHeight(INITIAL_ROWHEIGHT);

        scrollpane = new JScrollPane(tableView);
        return scrollpane;
    }

    private void printTable() {
        MessageFormat headerFmt;
        MessageFormat footerFmt;
        JTable.PrintMode printMode = fitWidth.isSelected() ?
                                     JTable.PrintMode.FIT_WIDTH :
                                     JTable.PrintMode.NORMAL;

        String text;
        text = headerTextField.getText();
        if (text != null && text.length() > 0) {
            headerFmt = new MessageFormat(text);
        } else {
            headerFmt = null;
        }

        text = footerTextField.getText();
        if (text != null && text.length() > 0) {
            footerFmt = new MessageFormat(text);
        } else {
            footerFmt = null;
        }

        try {
            boolean status = tableView.print(printMode, headerFmt, footerFmt);

            if (status) {
                JOptionPane.showMessageDialog(tableView.getParent(),
                                              getString("TableDemo.printingComplete"),
                                              getString("TableDemo.printingResult"),
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tableView.getParent(),
                                              getString("TableDemo.printingCancelled"),
                                              getString("TableDemo.printingResult"),
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            String errorMessage = MessageFormat.format(getString("TableDemo.printingFailed"),
                                                       new Object[] {pe.getMessage()});
            JOptionPane.showMessageDialog(tableView.getParent(),
                                          errorMessage,
                                          getString("TableDemo.printingResult"),
                                          JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException se) {
            String errorMessage = MessageFormat.format(getString("TableDemo.printingFailed"),
                                                       new Object[] {se.getMessage()});
            JOptionPane.showMessageDialog(tableView.getParent(),
                                          errorMessage,
                                          getString("TableDemo.printingResult"),
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    class NamedColor extends Color {
        String name;
        public NamedColor(Color color, String name) {
            super(color.getRGB());
            this.name = name;
        }

        public Color getTextColor() {
            int r = getRed();
            int g = getGreen();
            int b = getBlue();
            if(r > 240 || g > 240) {
                return Color.black;
            } else {
                return Color.white;
            }
        }

        public String toString() {
            return name;
        }
    }

    class ColumnLayout implements LayoutManager {
        int xInset = 5;
        int yInset = 5;
        int yGap = 2;

        public void addLayoutComponent(String s, Component c) {}

        public void layoutContainer(Container c) {
            Insets insets = c.getInsets();
            int height = yInset + insets.top;

            Component[] children = c.getComponents();
            Dimension compSize = null;
            for (int i = 0; i < children.length; i++) {
                compSize = children[i].getPreferredSize();
                children[i].setSize(compSize.width, compSize.height);
                children[i].setLocation( xInset + insets.left, height);
                height += compSize.height + yGap;
            }

        }

        public Dimension minimumLayoutSize(Container c) {
            Insets insets = c.getInsets();
            int height = yInset + insets.top;
            int width = 0 + insets.left + insets.right;

            Component[] children = c.getComponents();
            Dimension compSize = null;
            for (int i = 0; i < children.length; i++) {
                compSize = children[i].getPreferredSize();
                height += compSize.height + yGap;
                width = Math.max(width, compSize.width + insets.left + insets.right + xInset*2);
            }
            height += insets.bottom;
            return new Dimension( width, height);
        }

        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        public void removeLayoutComponent(Component c) {}
    }

    void updateDragEnabled(boolean dragEnabled) {
        tableView.setDragEnabled(dragEnabled);
        headerTextField.setDragEnabled(dragEnabled);
        footerTextField.setDragEnabled(dragEnabled);
    }

}
