import java.awt.*;
import javax.swing.*;

class UNOSplash extends JWindow{
	
	public UNOSplash() {
		super();
		
		SplashPane splashPane = new SplashPane();
		MediaTracker t = new MediaTracker(this);
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		screen = tk.getImage("resources/splash.png");
		t.addImage(screen, 0);
		try {
			t.waitForAll();
		}
		catch (Exception e) {
			System.out.println("Ooops in loading splash image");
			System.exit(1);
		}
		
		Dimension screenSize = tk.getScreenSize();
		setSize(new Dimension(screen.getWidth(this), screen.getHeight(this)));
		setLocation((screenSize.width-320)/2,(screenSize.height-100)/2);
		
		getContentPane().add(splashPane);
		
		setVisible(true);
	}
	
	private class SplashPane extends JPanel {
		public SplashPane() {
			super();
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		
			g.drawImage(screen, 0, 0, this);
		}
	}
	
	
	private Image screen;
}