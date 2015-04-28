
import java.io.FileNotFoundException;

import java.io.IOException;

import javax.swing.JOptionPane;

public class UNOAboutDialog extends javax.swing.JDialog {

    public UNOAboutDialog(java.awt.Frame parent, boolean modal) throws IOException {
        super(parent, modal);
        initComponents();
    }
    private void initComponents() throws FileNotFoundException, IOException {//GEN-BEGIN:initComponents

        JOptionPane.showMessageDialog(null,"This project was created and produced by CMSC 420 Team 4.\n This team entails of Nick Harrison, Alex Yoon, Ryan Taylor, Nandu Radjakrishnan, Paul Kirchoff, and Keith Seal.\n This project was for CMSC 420 as part of a project management tutorial.\n For information or source code of this product please contact Ryan Taylor at taylorjr7@vcu.edu" , "About Uno", JOptionPane.INFORMATION_MESSAGE);
        
    }
}
