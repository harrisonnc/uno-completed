

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class UNOWindow extends JFrame {

	public  void main(String[] args) 
        {
            new UNOWindow().setVisible(true);
	}


    public UNOWindow() 
    {
    	options = new Options();
        game = new UNOGame(options);

        initComponents();
        repaint();
    }


    private void initComponents() {//GEN-BEGIN:initComponents
        menuBar           = new JMenuBar();
        fileMenu          = new JMenu();
        newGameMenuItem   = new JMenuItem();
        newSeriesMenuItem   = new JMenuItem();
        optionsMenuItem   = new JMenuItem();
        separator1        = new JSeparator();
        separator1        = new JSeparator();
        exitUnoMenuItem   = new JMenuItem();
        helpMenu          = new JMenu();
        helpUnoMenuItem   = new JMenuItem();
        helpMenuSeparator = new JSeparator();
        aboutUnoMenuItem  = new JMenuItem();
        optionsDialog     = new OptionsDialog(this, true, options);
        
        fileMenu.setText("File");
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        newGameMenuItem.setText("New game");
        newGameMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game?", "New Game?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    newGameMenuItemActionPerformed(evt);
                    UNOPanel.played.clear();
                }
                
            }
        });
        
        fileMenu.add(newGameMenuItem);
        
        newSeriesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        newSeriesMenuItem.setText("New Series");
        newSeriesMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new series?", "New Game?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    newGameMenuItemActionPerformed(evt);
                    for(int lcv=0;lcv<6;lcv++)
                    {
                        UNOPanel.model.setValueAt(0, lcv, 1);
                        UNOPanel.scores[lcv]=0;
                    }    
                    UNOPanel.played.clear();
                }
                
            }
        });
        
        fileMenu.add(newSeriesMenuItem);
        
        optionsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
        optionsMenuItem.setText("Options...");
        optionsMenuItem.setMnemonic('O');
        optionsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        
        fileMenu.add(optionsMenuItem);
        fileMenu.add(separator1);
        exitUnoMenuItem.setText("Exit UNO");
        exitUnoMenuItem.setMnemonic('X');
        exitUnoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Game?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    exitUnoMenuItemActionPerformed(evt);
                }
            }
        });
        
        fileMenu.add(exitUnoMenuItem);
        menuBar.add(fileMenu);
        
        helpMenu.setText("Help");
        fileMenu.setMnemonic('H');
        helpUnoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpUnoMenuItem.setText("Help...");
        helpUnoMenuItem.setMnemonic('H');
        helpUnoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                helpUnoMenuItemActionPerformed(evt);
            }
        });
        
        helpMenu.add(helpUnoMenuItem);
        helpMenu.add(helpMenuSeparator);
        aboutUnoMenuItem.setText("About UNO...");
        aboutUnoMenuItem.setMnemonic('A');
        aboutUnoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    aboutUnoMenuItemActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(UNOWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
        });
        
        helpMenu.add(aboutUnoMenuItem);
        menuBar.add(helpMenu);
        
        setTitle("UNO for Java");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("unoFrameWindow");
        setResizable(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        setJMenuBar(menuBar);
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(610, 450));
        setLocation((screenSize.width-610)/2,(screenSize.height-450)/2);
        
        setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("\\resources\\unoicon.png")));
        
        gamePane = new UNOPanel(this, game, options);
        getContentPane().add(gamePane, java.awt.BorderLayout.CENTER);
    }

    private void helpUnoMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
    {
        helpScreen.setVisible(true);
    }

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
    {
        optionsDialog.setVisible(true);
        gamePane.repaint();
    }

    private void aboutUnoMenuItemActionPerformed(java.awt.event.ActionEvent evt) throws IOException 
    {
        new UNOAboutDialog(new javax.swing.JFrame(), true);
        gamePane.repaint();
    }

    private void exitUnoMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
    {
        System.exit(0);
    }

    public void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
    {
        options = optionsDialog.getOptions();
        gamePane.newGame(options);
    }

    public static void newGameMenuItemActionPerformed() 
    {
	options = optionsDialog.getOptions();
        gamePane.newGame(options);
    }


    private void exitForm(java.awt.event.WindowEvent evt) 
    {    
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Game?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
    }
    
    private javax.swing.JMenuBar menuBar;
    public javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem newGameMenuItem;
    public javax.swing.JMenuItem newSeriesMenuItem;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private javax.swing.JMenuItem exitUnoMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpUnoMenuItem;
    private javax.swing.JSeparator helpMenuSeparator;
    private javax.swing.JMenuItem aboutUnoMenuItem;
    private static UNOPanel gamePane;
    
    
    private UNOHelpScreen helpScreen = new UNOHelpScreen(this, "/resources/UNOHelp.htm");
    private static UNOGame game;
    private static OptionsDialog optionsDialog;
    private static Options options;
}
