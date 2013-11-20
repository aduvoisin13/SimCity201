package bank;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import base.Time;
import base.interfaces.Person;
import city.gui.CityGui;
import city.gui.CityPanel;
import city.gui.Gui;
import bank.BankGui;


public class BankPanel extends JPanel implements ActionListener{
	private static BankPanel instance = null;
	private int WINDOWX = 600;
	private int WINDOWY = 600;
	
	private Image bufferImage;
    private Dimension bufferSize;
    public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	static final int timerCount = 5;
	
	private BankGui gui;
	private List<Gui> guis = new ArrayList<Gui>();
	
		
	
	public BankPanel(BankGui gui) {
		this.gui = gui;
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		Time mTime = new Time();
	}


	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	

}
