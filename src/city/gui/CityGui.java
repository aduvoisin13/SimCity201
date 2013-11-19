package city.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class CityGui extends JFrame implements ActionListener{
	JFrame cityFrame = new JFrame("SimCity Animation");
	CityPanel cityPanel = new CityPanel(this);
    private CityPanel CityPanel = new CityPanel(this);
	
	static final int hSpacing = 30;
	static final int vSpacing = 0;
	static final int xIndexing = 50;
	static final int yIndexing = 50;
	
	public CityGui() {
		int WINDOWX = 1000;
        int WINDOWY =700;

        cityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cityFrame.setBounds(xIndexing, yIndexing , WINDOWX, WINDOWY);
        cityFrame.setVisible(false);
    	add(cityPanel);
    	
    	setBounds(xIndexing, yIndexing, WINDOWX, WINDOWY);
    	
    	setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
    	
    	Dimension cityDim = new Dimension(WINDOWX, (int) (WINDOWY * .5));
    	CityPanel.setPreferredSize(cityDim);
    	
    	
    	
	}
	
	public static void main(String[] args) {
        CityGui gui = new CityGui();
        gui.setTitle("Team 28 City");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}