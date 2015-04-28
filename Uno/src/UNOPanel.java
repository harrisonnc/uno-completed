
 
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.Timer;

public class UNOPanel extends javax.swing.JPanel {

    /** Creates new form UNOPanel */
    static ArrayList<String> played = new ArrayList<String>();
    public UNOPanel(javax.swing.JFrame parentFrame, UNOGame game, Options options) {
        this.parentFrame = parentFrame;
        
        initComponents();
        
        this.game = game;

        screen[0] = getCardPositions(game.getNumberPlayers() - 1, 0, 3);
        screen[1] = getCardPositions(2, 1, 3);
        screen[2] = getCardPositions(game.getDeck(0).getSize(), 2, 3);
        
        humanListener = new UNOMouseListener();
        
        this.addMouseListener(humanListener);

        computerTimer = new Timer(2000, new UNOAIListener(new AIEasyPlayer(game)));
        
        unoTimer = new Timer(4000, new UNOTimerEffect());
        unoTimer.setRepeats(false);

        newGame(options);
        repaint();
    }
    
    public void newGame(Options options) {
    	game.initGame(options);
    	
    	computerTimer.stop();
    	
		screen[0] = getCardPositions(game.getNumberPlayers() - 1, 0, 3);
		screen[1] = getCardPositions(2, 1, 3);
		screen[2] = getCardPositions(game.getDeck(0).getSize(), 2, 3);
    	
    	repaint();
    }

    private void initComponents() {

        unoButton = new javax.swing.JButton();
        colorButton = new javax.swing.JButton();
        playedCards = new javax.swing.JButton();
        
        setLayout(null);
        
        setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        setBackground(new java.awt.Color(0, 153, 51));
        setPreferredSize(new java.awt.Dimension(610, 450));
        setMinimumSize(new java.awt.Dimension(610, 450));
        
        unoButton.setText("UNO!");
        unoButton.setBounds(10, 160 , 70, 30);
        add(unoButton);
        
        playedCards.setText("Played");
        playedCards.setBounds(10, 100 , 80, 30);
        add(playedCards);
        
        colorButton.setText("");
	colorButton.setEnabled(false);
        colorButton.setBounds(27, 200 , 30, 30);
        colorButton.setBackground(Color.black);
	add(colorButton);

        unoButton.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                unoButtonActionPerformed(evt);
            }
        });
        
        playedCards.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                playedCardsActionPerformed(evt);
            }
        });
            
        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));
        setBackground(new java.awt.Color(0, 153, 51));
        setPreferredSize(new java.awt.Dimension(610, 450));
        setMinimumSize(new java.awt.Dimension(610, 450));
    }//GEN-END:initComponents

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		setBackground(new java.awt.Color(0, 153, 51));
		
		screen[0] = getCardPositions(game.getNumberPlayers() - 1, 0, 3);
		screen[1] = getCardPositions(2, 1, 3);
		screen[2] = getCardPositions(game.getDeck(0).getSize(), 2, 3);
		
		colorButton.setBackground(colorCode[game.getCurrentColor()]);
		
		drawDirection(g, game.getDirection());
		
		//The other players
		for (int i = 1; i != game.getNumberPlayers(); i += 1) {
			drawCard(g, null, i-1, 0, "Player " + (i+1) + " ("+ game.getDeck(i).getSize()+")", (game.getCurrentPlayer() == i));
		}
		
		//The drawing and discard deck
		int numCards = game.getDeck(UNOGame.DECK).getSize();
		
		if (numCards < 10)
			drawPile(g, null, 0, 1, 1);
		else if (numCards >= 10 && numCards < 20)
			drawPile(g, null, 0, 1, 2);
		else if (numCards >= 20 && numCards < 30)
			drawPile(g, null, 0, 1, 3);
		else
			drawPile(g, null, 0, 1, 4);
			
		numCards = game.getDeck(UNOGame.DISCARD).getSize();
		
		if (numCards < 10)
			drawCard(g, game.getLastCardPlayed(), 1, 1);
		else if (numCards < 20)
			drawPile(g, game.getLastCardPlayed(), 1, 1, 2);
		else if (numCards < 30)
			drawPile(g, game.getLastCardPlayed(), 1, 1, 3);
		else
			drawPile(g, game.getLastCardPlayed(), 1, 1, 4);
		
		UNODeck deck = game.getDeck(0);
		
		//Your cards
		if (deck.getSize() >= 1)
			drawCard(g, deck.showCard(0), 0, 2, "Player 1 ("+deck.getSize()+")", (game.getCurrentPlayer() == 0));
		else
			drawCard(g, null, 0, 2, "Player 1 (0)", (game.getCurrentPlayer() == 0));
		
		if (deck.getSize() > 1) {
			for (int i = 1; i != deck.getSize(); i += 1) {
				drawCard(g, deck.showCard(i), i, 2);
			}
		}
		drawStatusMessage(g);
	}
        
        private void playedCardsActionPerformed(ActionEvent evt) 
        {   
            computerTimer.stop();
            JFrame frame = new JFrame("Last Cards Played");
            JPanel panel = new JPanel();
            JList playedList=new JList(played.toArray());
            JScrollPane pane = new JScrollPane(playedList);
            panel.add(pane);
            frame.setBounds(parentFrame.getX()+50,parentFrame.getY()+50,parentFrame.getWidth()/2,parentFrame.getHeight()/2);
            //frame.add(panel);
            //frame.pack();
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
            repaint();
            frame.addWindowListener(new WindowEventHandler());
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            
        }
	
	private void unoButtonActionPerformed(ActionEvent evt) {
		if (!unoTimer.isRunning()) return;
		computerTimer.stop();
		
		new JOptionPane("Player 1 called Uno!", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog(this, "Someone has called Uno!").show();
		unoTimer.stop();
		repaint();
		
		computerTimer.start();
	}
	
	private void drawCard(Graphics g, Card card, int cardPos, int cardLevel) {
		Image i = toImage(card);
		
		Point p = screen[cardLevel][cardPos].getLocation();
		
		g.drawImage(i, p.x, p.y, this);
	}

	private void drawCard(Graphics g, Card card, int cardPos, int cardLevel, 
	                      String message, boolean isHighlighted) {
		Image i = toImage(card);
		
		Point p = screen[cardLevel][cardPos].getLocation();
		
		g.drawImage(i, p.x, p.y, this);
		
		Graphics2D g2 = (Graphics2D)g;
		
		Font font = new Font("Helvetica", Font.BOLD, 12);
		g2.setColor(isHighlighted? Color.white: Color.darkGray);
		g2.setFont(font);
		
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(message, context);
		
		Point p2 = new Point(p.x + (int)(cardWidth - bounds.getWidth())/2, p.y - 8);
		
		g2.drawString(message, p2.x, p2.y);
		
		if (isHighlighted) {
			
			p2.x -= 30;
			if (p2.x < 0) p2.x = 0;
			
			g2.drawImage(arrow, p2.x, p2.y, this);
		}
	}
		
	private void drawPile(Graphics g, Card card, int cardPos, int cardLevel, int height) {
		Image i = toImage(card);
		
		Point p = screen[cardLevel][cardPos].getLocation();
		
		int x = p.x;
		int y = p.y;
		
		for (int j = 0; j != height; j += 1) {
			g.drawImage(i, x, y, this);
			x += 3;
			y -= 3;
		}
	}
	
	private void setStatusMessage(String message, int time) {
		statusMessage = message;
		statusMsgTime = time;
	}
	
	private void drawStatusMessage(Graphics g) {
		
		Font font = new Font("Helvetica", Font.BOLD, 12);
		g.setColor(Color.white);
		
		if (statusMsgTime != 0) {
			g.drawString(statusMessage, 12, this.getHeight() - 10);
			statusMsgTime--;
		}
	}
	
	private void drawDirection(Graphics g, int direction) {
		int x = (getWidth() - directions[0].getWidth(null))/2 + 7;
		int y = (getHeight()- directions[0].getHeight(null))/2 - 10;
		
		if (direction == Turn.CLOCKWISE) {
			g.drawImage(directions[0], x, y, this);
		} else {
			g.drawImage(directions[1], x, y, this);
		}
	}
	private Rectangle[] getCardPositions(int cardsWide, int cardLevel, int cardMaxLevels) {
		if (cardsWide == 0) cardsWide = 1;
		
		Rectangle[] rect = new Rectangle[cardsWide];
		
		int xSpace = fitCardSpace(cardWidth, cardHSpace, parentFrame.getWidth(), cardsWide, 10);
		int ySpace = fitCardSpace(cardHeight, cardVSpace, parentFrame.getHeight(), cardMaxLevels, 30);
		
		int x = (parentFrame.getWidth() - (cardsWide-1)*xSpace - cardsWide*cardWidth)/2;
		int y = (parentFrame.getHeight() - cardMaxLevels*ySpace - cardMaxLevels*cardHeight)/2
		        + cardLevel*ySpace + cardLevel*cardHeight - 10; //The extra 10 pixels are to give space to the status message
		
		
		for (int i = 0; i != rect.length; i += 1) {
			rect[i] = new Rectangle(x, y, cardWidth, cardHeight);
			
			x += (xSpace + cardWidth);
		}
		
		return rect;
	}
	
	private int fitCardSpace(int cardWidth, int cardSpace, int maxWidth, int cardsWide, int step) {
		
		boolean outOfScreen = maxWidth - (cardsWide-1)*cardSpace - cardsWide*cardWidth < 10;
		
		while (outOfScreen) {
			cardSpace -= step;
			outOfScreen = maxWidth - (cardsWide-1)*cardSpace - cardsWide*cardWidth < 10;
		}
		
		return cardSpace;
	}
	
	private Image toImage(Card card) {
		if (card == null) return this.card[4][0];
		
		else if (!card.isWild()) return this.card[card.getColor()][card.getValue()];
		
		else if (card.isWild() && !card.isWildFour()) return this.card[4][1];
		
		else if (card.isWildFour()) return this.card[4][2];
		
		else return this.card[card.getColor()][card.getValue()];
	}

	private int chooseColor() {
		UNOColorChooser ucc = new UNOColorChooser(parentFrame, true);
		return ucc.getColor();
	}	
	
	private void announceWinner(int winner) {
		
		String s = "Player " + winner + " has won this match";
		new JOptionPane(s, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog(this, "Someone has won").setVisible(true);
		setStatusMessage(s, 2);
		
		computerTimer.stop();
		computerTimer.stop();

		repaint();
	}
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame parentFrame;
    private javax.swing.JButton unoButton;
    private javax.swing.JButton colorButton;
    private javax.swing.JButton playedCards;
    private javax.swing.JList playedList;
    
    private UNOMouseListener humanListener;
    private UNOAIListener computerListener;
    
    public static Timer computerTimer;
    private Timer unoTimer;
    
    private UNOGame game;
    
    private static int cardHSpace = 50;
    private static int cardVSpace = 25;
    private static int cardWidth  = 65;
    private static int cardHeight = 100;
    
    
    private String statusMessage = "";
    private int statusMsgTime = 1;
    
    private Rectangle[][] screen = new Rectangle[3][];    //This is a tool I use both to draw images and detect their clicks
    
    private static final Color[] colorCode = { (new Color(204, 0  , 0  )),
                                               (new Color(0  , 102, 0  )),
                                               (new Color(0  , 0  , 204)),
                                               (new Color(255, 204, 0  )),
                                               (new Color(0  , 0  , 0  )) };
    
    private final Image[][] card = new Image[5][]; //The image repository
    private final Image[] directions = new Image[2]; 
    private final Image   arrow = Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("\\resources\\flecha.png"));
    
    // End of variables declaration//GEN-END:variables

	 {
		UNOSplash splash = new UNOSplash();
		
		splash.setVisible(true);
		
		loadCards();
		
		splash.setVisible(false);
	}
	
	private void loadCards() {
		
		MediaTracker tracker = new MediaTracker(new Container());
		//URL temp = null;
		
		int id = 0;
		
		String[] color = {"red", "green", "blue", "yellow"};
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		for (int i = 0; i != 5; i += 1) {
			card[i] = new Image[13];
		}
		card[4] = new Image[3];
		
		for (int i = 0; i != 4; i += 1) {
			for (int j = 0; j != 13; j += 1) {
				//temp = UNOWindow.class.getResource(color[i]+"card"+j+".png");
				card[i][j] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\"+color[i]+" "+j+".png"));
				tracker.addImage(card[i][j], id++);
			}

		}
		
		//temp = UNOWindow.class.getResource("back.png");
		card[4][0] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\back.png"));
		//temp = UNOWindow.class.getResource("cardW.png");
		card[4][1] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\cardW.png"));
		//temp = UNOWindow.class.getResource("cardW+4.png");
		card[4][2] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\cardW+4.png"));
	
		//temp = UNOWindow.class.getResource("cw.png");
		directions[0] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\cw.png"));
		//temp = UNOWindow.class.getResource("ccw.png");
		directions[1] = tk.createImage(getClass().getClassLoader().getResource("\\resources\\ccw.png"));
		
		tracker.addImage(card[4][0], id++);
		tracker.addImage(card[4][1], id++);
		tracker.addImage(card[4][2], id++);
		tracker.addImage(directions[0], id++);
		tracker.addImage(directions[1], id++);
		
		tracker.addImage(arrow, id++);
		
		try {
			tracker.waitForAll();
		}
		catch (Exception e) {
			System.out.println("Ooops in loading card images...");
			System.exit(1);
		}
	}
	
	private String toColor(int color) {
		switch (color) {
			case Card.RED:    return "Red";
			case Card.YELLOW: return "Yellow";
			case Card.GREEN:  return "Green";
			case Card.BLUE:   return "Blue";
		}
		return "ERROR";
	}
	
	//INNER CLASSES
	
	private class UNOMouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent evt) {
			
			int cardToPlay = detectCardSelected(evt);
			
			if (cardToPlay == -1) return;
			int result = game.playCard(0, cardToPlay);
                        
                        if(played.isEmpty())
                        {
                            //do nothing
                        }
                        else
                        {
                            int card = game.getLastCardPlayed().getValue();
                            if(card<10)
                            {
                                if(game.getLastCardPlayed().getColor()==0)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red "+card);
                                else if(game.getLastCardPlayed().getColor()==1)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green "+card);
                                else if(game.getLastCardPlayed().getColor()==2)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue "+card);
                                else if(game.getLastCardPlayed().getColor()==3)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow "+card);
                                else if(game.getLastCardPlayed().getColor()==4)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a Wild "+card);
                            }
                        else
                        {
                            if(game.getLastCardPlayed().getColor()==0)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==1)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==2)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==3)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==4)
                            {
                                if(card==13)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a wild");
                                if(card==14)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a wild draw 4");
                            }
                        }
                    }   
			
			if (result >= 28) {
				result -= 35;
				
				announceWinner(game.getLastPlayer() + 1);
				return;
			} 
                        else if (result >= 13) {
				result -= 20;
				activateUnoTimer();
				//There's an Uno, quick!!
			}
			
			switch (result) {
				case -6:
					break;
				case -1:
					setStatusMessage("That's not a valid card!", 1);
					break;
				case -5:
					throw new RuntimeException("This must not happen");
				case -4:
					setStatusMessage("You can't draw more cards!", 1);
					break;
				case -3:
					setStatusMessage("You drew a card you can play", 1);
					break;
				case -2:
					setStatusMessage("You drew a card, and got nuthin'", 1);
					break;
				case 0:
				case 1:
				case 2:
				case 3:
					break;
				case 4:
				case 5:
					game.setColor(chooseColor());
					setStatusMessage("The color chosen was: " + toColor(game.getCurrentColor()), 1);
					break;
				case 6:
					setStatusMessage("You've passed your hand to the next player", 1);
					break;
				case 8: 
					game.setColor(chooseColor());
				case 7:
					setStatusMessage("Player " + (game.getCurrentPlayer()+1) +" won't draw any cards.", 1);
					break;
			}
			
			repaint();
			
			if (game.getCurrentPlayer() != 0) {
				computerTimer.start();
			}
		}
		
		private int detectCardSelected(MouseEvent evt) {
			Point p = evt.getPoint();
			
			if (screen[1][0].contains(p)) return game.getDeck(0).getSize();
			
			for (int i = screen[2].length-1; i != -1; i -= 1) {
				if (screen[2][i].contains(p)) return i;
			}
			
			return -1;
		}
	}
	
	private class UNOAIListener implements ActionListener {
		
		public UNOAIListener(AIPlayer player) {
			this.player = player;
		}
		
		public void actionPerformed(ActionEvent evt) {
			
			UNODeck myDeck = game.getCurrentDeck();
			int cardToPlay = player.playMyTurn(myDeck);
			int result = game.playCard(game.getCurrentPlayer(), cardToPlay);
                        int card = game.getLastCardPlayed().getValue();
                        if(card<10)
                        {
                            if(game.getLastCardPlayed().getColor()==0)
                                played.add("Player"+(game.getLastPlayer()+1)+" played a red "+card);
                            else if(game.getLastCardPlayed().getColor()==1)
                                played.add("Player"+(game.getLastPlayer()+1)+" played a green "+card);
                            else if(game.getLastCardPlayed().getColor()==2)
                                played.add("Player"+(game.getLastPlayer()+1)+" played a blue "+card);
                            else if(game.getLastCardPlayed().getColor()==3)
                                played.add("Player"+(game.getLastPlayer()+1)+" played a yellow "+card);
                            else if(game.getLastCardPlayed().getColor()==4)
                                played.add("Player"+(game.getLastPlayer()+1)+" played a Wild "+card);
                        }
                        else
                        {
                            if(game.getLastCardPlayed().getColor()==0)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a red draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==1)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a green draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==2)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a blue draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==3)
                            {
                                if(card==10)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow reverse");
                                if(card==11)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow skip");
                                if(card==12)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a yellow draw two");
                            }
                            else if(game.getLastCardPlayed().getColor()==4)
                            {
                                if(card==13)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a wild");
                                if(card==14)
                                    played.add("Player"+(game.getLastPlayer()+1)+" played a wild draw 4");
                            }
                        }
			if (result > 28) {
				result -= 35;
				//There's a winner, dammit!
				announceWinner(game.getLastPlayer() + 1);
				return;
			} else if (result > 13) {	
				result -= 20;
				
				new JOptionPane("Player " + (game.getLastPlayer()+1) + " called Uno!", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog(parentFrame, "Someone has called Uno!").show();
				setStatusMessage("Player " + (game.getLastPlayer()+1) + " called Uno!", 1);
				//There's an Uno, quick!!
			}
			
			switch (result) {
				case -7:
					setStatusMessage("Game's finished.", 1);
				case -6:
				case -1:
					setStatusMessage("That's not a valid card.", 1);
					break;
				case -5:
					throw new RuntimeException("This must not happen");
				case -4:
					setStatusMessage("Player "+ (game.getCurrentPlayer()+1) +" can't draw more cards!", 1);
					break;
				case -3:
					setStatusMessage("Player "+ (game.getCurrentPlayer()+1) +" drew a card he can play", 1);
					break;
				case -2:
					setStatusMessage("Player "+ (game.getCurrentPlayer()+1) +" drew a card, and got nuthin'", 1);
					break;
				case 0:
				case 1:
				case 2:
				case 3:
					break;
				case 4:
				case 5:
					game.setColor(player.chooseColor(game.getCurrentDeck()));
					setStatusMessage("The color chosen was: " + toColor(game.getCurrentColor()), 1);
					break;
				case 6:
					setStatusMessage("Player "+ (game.getCurrentPlayer()+1) +" has passed your hand to the next player", 1);
					break;
				case 8:
					game.setColor(player.chooseColor(game.getCurrentDeck()));
				case 7:
					setStatusMessage("Player "+ (game.getCurrentPlayer()+1) +" won't draw any cards.", 1);
					break;
			}
			
			repaint();
						
			if (game.getCurrentPlayer() == 0) computerTimer.stop();
		}
		private AIPlayer player;
	}
	
	private void activateUnoTimer() {
		computerTimer.stop();
		unoTimer.start();
	}
	
	private class UNOTimerEffect implements ActionListener {
		
		public void actionPerformed(ActionEvent evt) {
			setStatusMessage("Someone called Uno for you. You draw 6 cards.", 2);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			
			game.restartHand(0);
			repaint();
			
			computerTimer.start();
		}
	}
}

class WindowEventHandler extends WindowAdapter 
{
  public void windowClosing(WindowEvent evt) 
  {
    UNOPanel.computerTimer.start();
  }
}

