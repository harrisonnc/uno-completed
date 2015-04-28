

public class UNOColorChooser extends javax.swing.JDialog 
{

    private int colorChosen;
    
    public UNOColorChooser(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        initComponents();
        colorChosen = 0;
    }


    private void initComponents() 
    {
        buttonsPane = new javax.swing.JPanel();
        redButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();
        yellowButton = new javax.swing.JButton();
        labelPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        
        setTitle("Choose the new color");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("colorChooser");
        setModal(true);
        setResizable(true);
        
        redButton.setMnemonic('R');
        redButton.setForeground(java.awt.Color.white);
        redButton.setText("Red");
        redButton.setBackground(new java.awt.Color(204, 0, 0));
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });
        
        buttonsPane.add(redButton);
        
        greenButton.setMnemonic('G');
        greenButton.setForeground(java.awt.Color.white);
        greenButton.setText("Green");
        greenButton.setBackground(new java.awt.Color(0, 102, 0));
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });
        
        buttonsPane.add(greenButton);
        
        blueButton.setMnemonic('B');
        blueButton.setForeground(java.awt.Color.white);
        blueButton.setText("Blue");
        blueButton.setBackground(new java.awt.Color(0, 0, 204));
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });
        
        buttonsPane.add(blueButton);
        
        yellowButton.setMnemonic('Y');
        yellowButton.setForeground(java.awt.Color.white);
        yellowButton.setText("Yellow");
        yellowButton.setBackground(new java.awt.Color(255, 204, 0));
        yellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellowButtonActionPerformed(evt);
            }
        });
        
        buttonsPane.add(yellowButton);
        
        getContentPane().add(buttonsPane, java.awt.BorderLayout.CENTER);
        
        labelPane.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel1.setText("Choose a new color:");
        labelPane.add(jLabel1);
        
        getContentPane().add(labelPane, java.awt.BorderLayout.NORTH);
        
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-getWidth())/2,(screenSize.height-getHeight())/2);
    }
	
	public int getColor() {
		this.setVisible(true);
		
		return colorChosen;
	}
	
    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
        colorChosen = Card.BLUE;
        closeDialog();
    }

    private void yellowButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
        colorChosen = Card.YELLOW;
        closeDialog();
    }

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
        colorChosen = Card.GREEN;
        closeDialog();
    }

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
        colorChosen = Card.RED;
        closeDialog();
    }

    private void closeDialog() 
    {
        setVisible(false);
        dispose();
    }

    private javax.swing.JPanel buttonsPane;
    private javax.swing.JButton redButton;
    private javax.swing.JButton greenButton;
    private javax.swing.JButton blueButton;
    private javax.swing.JButton yellowButton;
    private javax.swing.JPanel labelPane;
    private javax.swing.JLabel jLabel1;
}
