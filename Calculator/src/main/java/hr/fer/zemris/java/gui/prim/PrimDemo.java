package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This program display a window with two lists of prime numbers(including number 1) and
 * a button labeled "Sljedeci" which adds the next prime number to both lists.
 * @author ltomic
 *
 */
public class PrimDemo extends JFrame {

	/** serial version **/
	private static final long serialVersionUID = 1L;
	
	/** initial window location **/
	private static final Point INIT_LOCATION = new Point(100, 100);
	/** initial window title **/ 
	private static final String TITLE = "Prosti brojevi";
	/** initial window dimensions **/
	private static final Dimension INIT_DIMENSION = new Dimension(300, 300);
	
	/** text written on the button that adds the next prime number **/
	private static final String ADD_NEXT_BUTTON_LABEL = "Sljedeci";

	/**
	 * Initializes the window and GUI
	 */
	public PrimDemo() {
		setLocation(INIT_LOCATION);
		setTitle(TITLE);
		setSize(INIT_DIMENSION);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	/**
	 * Initializes window GUI
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel listModel = new PrimListModel();

		JList<Integer> leftList = new JList<>(listModel);
		JList<Integer> rightList = new JList<>(listModel);

		JButton addNext = new JButton(ADD_NEXT_BUTTON_LABEL);
		cp.add(addNext, BorderLayout.PAGE_END);

		addNext.addActionListener(l -> listModel.next());
		
		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(leftList));
		central.add(new JScrollPane(rightList));

		cp.add(central, BorderLayout.CENTER);
	}

	/**
	 * This is a list model that stores and generates prime numbers. JList uses
	 * it to display list of prime numbers. Method {@link PrimListModel#next()} is used
	 * to generate the next prime number end push it to the end of the list.
	 * @author ltomic
	 *
	 */
	public static class PrimListModel implements ListModel<Integer> {
		/** Prime number list storage **/
		private List<Integer> numbers = new ArrayList<>();
		/** ListDataListeners storage **/
		private List<ListDataListener> listeners = new ArrayList<>();
	
		{
			numbers.add(1);
		}

		@Override
		public void addListDataListener(ListDataListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeListDataListener(ListDataListener listener) {
			listeners.remove(listener);
		}

		@Override
		public Integer getElementAt(int index) {
			return numbers.get(index);
		}

		@Override
		public int getSize() {
			return numbers.size();
		}
		
		/**
		 * Adds the next prime number to the end of the list and notifies the list listeners
		 * about the list changes.
		 */
		public void next() {
			int pos = numbers.size();
			numbers.add(nextPrime());
			
			ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
			for(ListDataListener l : listeners) {
				l.intervalAdded(event);
			}
		}
		
		/**
		 * Generates the next prime number that comes after the one that was added last.
		 * @return the next prime number
		 */
		private int nextPrime() {
			for (int i = numbers.get(numbers.size()-1)+1; true; ++i) {
				if (isPrime(i)) return i;
			}
		}
		
		/**
		 * Tests if given integer is a prime number
		 * @param cand - number to test
		 * @return true if given number is a prime, else false
		 */
		private static boolean isPrime(int cand) {
			for (int i = 2; i * i <= cand; ++i) {
				if (cand % i == 0) return false;
			}
			return true;
		}
		
		/**
		 * Returns the generated list of prime numbers.
		 * @return the generated list of prime numbers.
		 */
		public List<Integer> getNumbers() {
			return numbers;
		}

	}

	/**
	 * Method called at the beginning of the program. Invokes the window creation.
	 * Takes no arguments.
	 * @param args - no arguments needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.setVisible(true);
		});
	}
}
