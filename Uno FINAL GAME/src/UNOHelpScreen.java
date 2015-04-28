

import javax.swing.JFrame;
import javax.swing.event.*;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UNOHelpScreen extends javax.swing.JFrame
{

    public UNOHelpScreen(JFrame parent, String file) 
    {
        initComponents();
        
        this.parent = parent;
        
    }

    private void initComponents() 
    {
        try 
        {
        
            okPanel = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            helpScrollPane = new javax.swing.JScrollPane();
            helpHtmlPane = new javax.swing.JTextArea();
        
            setTitle("UNO Help Screen");
            addWindowListener(new java.awt.event.WindowAdapter() 
            {
                public void windowClosing(java.awt.event.WindowEvent evt) 
                {
                    exitForm(evt);
                }
            });
        
        okPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        
        jLabel1.setText("UNO for Java Help Screen");
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        okPanel.add(jLabel1);
        
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        okPanel.add(jButton1);
        
        getContentPane().add(okPanel, java.awt.BorderLayout.SOUTH);
        
        helpScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        helpScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        helpScrollPane.setPreferredSize(new java.awt.Dimension(610, 24));
        helpHtmlPane.setEditable(false);
        String sCurrentLine;
        URL url = getClass().getClassLoader().getResource("\\resources\\help.txt");
        
        
        BufferedReader br = null;
        
        br = new BufferedReader(new InputStreamReader(url.openStream()));

        while ((sCurrentLine = br.readLine()) != null)
        {
            sCurrentLine = br.readLine();
            helpHtmlPane.append(sCurrentLine+"\n");
        }
        
        helpScrollPane.setViewportView(helpHtmlPane);
        
        getContentPane().add(helpScrollPane, java.awt.BorderLayout.CENTER);
        
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(610, 450));
        setLocation((screenSize.width-610)/2,(screenSize.height-450)/2);
        } catch (IOException ex) {
            Logger.getLogger(UNOHelpScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        setVisible(false);
        dispose();
        parent.repaint();
    }

    private void exitForm(java.awt.event.WindowEvent evt) 
    {
        setVisible(false);
        dispose();
        parent.repaint();
    }

	 

    private javax.swing.JPanel okPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane helpScrollPane;
    private javax.swing.JTextArea helpHtmlPane;
    private javax.swing.JFrame parent;
}
