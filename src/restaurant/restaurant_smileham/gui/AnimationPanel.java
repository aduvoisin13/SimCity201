package restaurant.restaurant_smileham.gui;

import javax.swing.*;

import city.gui.CityCard;
import city.gui.SimCityGui;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.WaitingArea;
import restaurant.restaurant_smileham.agents.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends CityCard implements ActionListener {
	// old values: X = 450, Y = RestaurantGui.cWINDOWY
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
//    private Image bufferImage;
//    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    
    
    //CONSTRUCTOR
    public AnimationPanel(SimCityGui city) {
    	super(city);
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
//        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
        
        //draw the tables
        g2.setColor(Table.cTABLE_COLOR);
        for (int iTableNum = 0; iTableNum < HostAgent.cNUM_TABLES; iTableNum++){
        	g2.fillRect(Table.getX(iTableNum), Table.getY(iTableNum), Table.cTABLE_WIDTH, Table.cTABLE_HEIGHT);
        }
        
        //draw waiting area
        g2.setColor(WaitingArea.cWAITINGAREA_COLOR);
        g2.fillRect(WaitingArea.cWAITINGAREA_X, WaitingArea.cWAITINGAREA_Y, WaitingArea.cWAITINGAREA_WIDTH, WaitingArea.cWAITINGAREA_HEIGHT);

        //draw plating area
        g2.setColor(CookGui.cPLATINGAREA_COLOR);
        g2.fillRect(CookGui.cPLATINGAREA_X, CookGui.cPLATINGAREA_Y, CookGui.cPLATINGAREA_WIDTH, CookGui.cPLATINGAREA_HEIGHT);
        
        //draw cooking area
        g2.setColor(CookGui.cCOOKINGAREA_COLOR);
        g2.fillRect(CookGui.cCOOKINGAREA_X, CookGui.cCOOKINGAREA_Y, CookGui.cCOOKINGAREA_WIDTH, CookGui.cCOOKINGAREA_HEIGHT);
        
        //draw fridge
        g2.setColor(CookGui.cFRIDGE_COLOR);
        g2.fillRect(CookGui.cFRIDGE_X, CookGui.cFRIDGE_Y, CookGui.cFRIDGE_WIDTH, CookGui.cFRIDGE_HEIGHT);
        
        //animation
        synchronized (guis) {
        	for(Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.updatePosition();
                }
            }

            for(Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.draw(g2);
                }
            }
		}
        
    }
    
	public void addGui(Gui gui) {
		guis.add(gui);
	}
	
	public void removeGui(Gui gui){
		guis.remove(gui);
	}
}
