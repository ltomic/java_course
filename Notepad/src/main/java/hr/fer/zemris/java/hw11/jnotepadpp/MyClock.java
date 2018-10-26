package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Implements a simple clock as a {@link JComponent}. Clock updates its time using thread
 * that wakes up every half a second.
 * @author ltomic
 *
 */
public class MyClock extends JComponent {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	
	/** Rate at which clock is updated **/
	private static final int DELAY = 500;
	/** Current clock time **/
	private volatile String time;
	/** Time formatter **/
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	/** Thread that updates clock time */
	private Thread t;
	/** Is clock running flag **/
	private volatile boolean running;

	/**
	 * Constructs a MyClock and creates a thread that updates its time.
	 */
	public MyClock() {
		updateTime();
		running = true;
		
		t = new Thread(()->{
			while(running) {
				try {
					Thread.sleep(DELAY);
				} catch(Exception ex) {}
				SwingUtilities.invokeLater(()->{
					updateTime();
				});
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	/** 
	 * Stops the clock
	 */
	public void stop() {
		running = false;
		try {
			t.join();
		} catch (InterruptedException ignorable) {}
	}
	
	/**
	 * Updates the clock time.
	 */
	private void updateTime() {
		time = formatter.format(LocalTime.now());
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Insets ins = getInsets();
		Dimension dim = getSize();
		Rectangle r = new Rectangle(
				ins.left, 
				ins.top, 
				dim.width-ins.left-ins.right,
				dim.height-ins.top-ins.bottom);
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		g.setColor(getForeground());
		
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(time);
		int h = fm.getAscent();
		g.drawString(
			time, 
			r.width - w,
			r.y+r.height-(r.height-h)/2
		);
	}
}
