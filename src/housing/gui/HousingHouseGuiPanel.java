package housing.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import restaurant_davidmca.gui.Gui;

/*
 * @author David Carr
 */

public class HousingHouseGuiPanel extends JPanel implements ActionListener {

	private final int WINDOWX = 300;
	private final int WINDOWY = 400;
	private List<Gui> guis = new ArrayList<Gui>();

	public HousingHouseGuiPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		this.getSize();
		Timer timer = new Timer(20, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(Gui g) {
		guis.add(g);
	}

}