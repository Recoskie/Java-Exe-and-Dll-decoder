package Window;

import javax.swing.*;
import swingIO.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import RandomAccessFileV.*;
import WindowComponents.*;

public class Window
{
  //Main application Window.

  public static RandomAccessFileV file;

  //Main application Window.

  public static JFrame f;

  //File chooser menu bar.

  public static JMenuBar fcBar;

  //Binary tool options, and views.

  public static JMenuBar bdBar;

  //Menu item actions.

  public static JPopupMenu pm;

  //File chooser tree, or data from a file format reader.

  public static JTree tree;
  public static JScrollPane stree;

  //Additional detailed information output. For data in table cells, or section.
  //Also disassembly output.

  public static JTextPane infoData = new JTextPane();
  public static JScrollPane iData;

  //Hex editor.

  public static VHex Virtual, Offset;

  //Data type inspector tool.

  public static dataInspector di;

  //Data descriptor tool.

  public static dataDescriptor ds;

  //Component layout system.

  public static JCellPane tools;

  //Once hex editor is initialized. Then the target is set afterwards for new files.

  public static boolean HInit = false;

  //Hex editor view options.

  public static boolean textV = true;

  //The current file reader. Used for handling events to decode for the section of interest in the tree.

  public static Object UsedDecoder;

  //Additional info text.

  public static void info( String s ) { infoData.setText(s); }

  public static void createGUI(String w, ActionListener app, TreeWillExpandListener app2, MouseListener app3, TreeSelectionListener app4)
  {
    f = new JFrame(w); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Tool window.

    infoData.setContentType("text/html");
    infoData.setBackground( new Color(238,238,238) );
    infoData.setEditable(false);
    javax.swing.text.DefaultCaret caret = (javax.swing.text.DefaultCaret) infoData.getCaret();
    caret.setUpdatePolicy(javax.swing.text.DefaultCaret.NEVER_UPDATE);
    iData = new JScrollPane( infoData );

    //File chooser controls.

    fcBar = new JMenuBar();

    JMenuItem Back = new JMenuItem( "Back", new ImageIcon( Window.class.getResource( "AppPictures/back.png" ) ) );
    JMenuItem Home = new JMenuItem( "User", new ImageIcon( Window.class.getResource( "AppPictures/home.png" ) ) );
    JMenuItem Go = new JMenuItem( "Forward", new ImageIcon( Window.class.getResource( "AppPictures/go.png" ) ) );
    JMenuItem Up = new JMenuItem( "Up a Folder", new ImageIcon( Window.class.getResource( "AppPictures/up.png" ) ) );
    JMenuItem Computer = new JMenuItem( "My Computer", new ImageIcon( Window.class.getResource( "AppPictures/computer.png" ) ) );
    JMenuItem OpenDisk = new JMenuItem( "Open Disk", new ImageIcon( Window.class.getResource( "AppPictures/OpenDisk.png" ) ) );

    fcBar.add( Computer ); fcBar.add( Back ); fcBar.add( Home ); fcBar.add( Go ); fcBar.add( Up ); fcBar.add( OpenDisk );
  
    //Action commands.
  
    Back.setActionCommand( "B" ); Back.addActionListener(app);
    Go.setActionCommand( "G" ); Go.addActionListener(app);
    Computer.setActionCommand( "C" ); Computer.addActionListener(app);
    Home.setActionCommand( "H" ); Home.addActionListener(app);
    Up.setActionCommand( "U" ); Up.addActionListener(app);
    OpenDisk.setActionCommand( "O" ); OpenDisk.addActionListener(app);

    //Binary tools menu bar.

    bdBar = new JMenuBar();

    JMenu fm = new JMenu("File");
    JMenu vm = new JMenu("View");
    JMenu tm = new JMenu("Tools");
 
    JMenuItem f1 = new JMenuItem("Open new File");

    //View options.

    JMenuItem v1 = new JMenuItem("Toggle text View");
    JMenuItem v2 = new JMenuItem("Toggle virtual space View");
    JMenuItem v3 = new JMenuItem("Toggle offset View");
    JMenuItem v4 = new JMenuItem("Toggle Data Inspector");

    //Tools.

    JMenuItem t1 = new JMenuItem("Goto Offset");
    JMenuItem t2 = new JMenuItem("Goto Virtual");

    //Hex editor operations.

    pm = new JPopupMenu("Selected bytes.");

    JMenuItem p1 = new JMenuItem("Copy as hex");
    JMenuItem p2 = new JMenuItem("Copy raw data");
    JMenuItem p3 = new JMenuItem("Save as file");

    //Set the action commands.

    p1.setActionCommand("CP"); p2.setActionCommand("CPR");

    //Create tool bar.

    fm.add(f1);
    
    vm.add(v1); vm.add(v2); vm.add(v3); vm.add(v4);

    tm.add(t1); tm.add(t2);

    bdBar.add(fm); bdBar.add(vm); bdBar.add(tm);

    //Create the pop up menu.

    pm.add( p1 ); pm.add( p2 ); pm.add( p3 );
  
    //add ActionListener to menuItems.
    
    f1.addActionListener(app);
    
    v1.addActionListener(app); v2.addActionListener(app);
    v3.addActionListener(app); v4.addActionListener(app);

    t1.addActionListener(app); t2.addActionListener(app);

    p1.addActionListener(app); p2.addActionListener(app); p3.addActionListener(app);

    //The tree is used for file chooser, and for decoded data view.

    tree = new JTree();

    //tree properties.

    tree.setRootVisible(false); tree.setShowsRootHandles(false);
    tree.addTreeWillExpandListener(app2); tree.addMouseListener(app3);
    tree.addTreeSelectionListener(app4);
  
    //Custom file Icon manager.
  
    tree.setCellRenderer(new FileIconManager());
    stree = new JScrollPane( tree );

    //Simple grid layout, for the tree.

    f.setLayout(new GridLayout(1,0));
    
    //Initialize IO components.

    try
    {
      file = new RandomAccessFileV( new byte[16] );

      di = new dataInspector( file ); ds = new dataDescriptor( di );
          
      Virtual = new VHex( file, di, true ); Offset = new VHex( file, di, false );
    }
    catch(Exception e){ }

    Virtual.setComponentPopupMenu(pm); Offset.setComponentPopupMenu(pm);

    Offset.enableText( textV ); Virtual.enableText( textV ); HInit = true;

    //Set visibility to tree only.

    ds.setVisible(false); iData.setVisible(false);
    Virtual.setVisible(false); Offset.setVisible(false);
    di.setVisible(false);
    
    //Add all the tools to window.

    tools = new JCellPane();

    //Data display tools.

    tools.add( stree ); tools.add( ds ); tools.add( iData ); tools.rowEnd();

    //Binary tools.

    tools.add( Virtual ); tools.add( Offset ); tools.add( di ); tools.rowEnd();

    //scroll bar for the tree.

    f.add(tools);

    //set the menu bar controls for file chooser.

    f.setJMenuBar(fcBar);

    //Set application icon image.

    f.setIconImage( new ImageIcon( Window.class.getResource( "AppPictures/app.png" ) ).getImage() );

    //Display the window.

    f.pack(); f.setLocationRelativeTo(null);
  }
}