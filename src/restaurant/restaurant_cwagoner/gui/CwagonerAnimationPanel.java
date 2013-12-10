package restaurant.restaurant_cwagoner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import base.Location;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class CwagonerAnimationPanel extends CityCard implements ActionListener {

	int tableSize = 50;
	public static CwagonerRestaurant restaurant;

    static ArrayList<Location> tableLocations = new ArrayList<Location>();

    public CwagonerAnimationPanel(SimCityGui g, CwagonerRestaurant r) {
    	super(g);
    	CwagonerAnimationPanel.restaurant = r;

    	this.setVisible(true);

    	this.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

    	Timer timer = new Timer(Time.cSYSCLK/15, this);
    	timer.addActionListener(this);
    	timer.start();

    	initializeTables();
    	initializeCookingArea();
    }
    
    private void initializeTables() {
        tableLocations.add(new Location(100, 100));
        tableLocations.add(new Location(300, 100));
        tableLocations.add(new Location(100, 200));
        tableLocations.add(new Location(300, 200));
    }

    private void initializeCookingArea() {
		try {
			java.net.URL cookURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/cook.png");
			CwagonerCookGui.cookImg = ImageIO.read(cookURL);
			java.net.URL fridgeURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/fridge.png");
			CwagonerCookGui.fridgeImg = ImageIO.read(fridgeURL);
			java.net.URL stoveURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/stove.png");
			CwagonerCookGui.stoveImg = ImageIO.read(stoveURL);
			java.net.URL tableURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/table.png");
			CwagonerCookGui.tableImg = ImageIO.read(tableURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static Location getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  // Will have paint() called
		
		synchronized(CwagonerRestaurant.guis) {
			for (CwagonerGui gui : CwagonerRestaurant.guis) {
	            gui.updatePosition();
	        }
		}
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the panel
        g2.setColor(getBackground());
        g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // Tables
        g2.setColor(Color.ORANGE);
        
        for (Location iL : tableLocations) {
        	g2.fillRect(iL.mX, iL.mY, tableSize, tableSize);
        }

        // Cook areas

    	// Stove
		g.drawImage(CwagonerCookGui.stoveImg, CwagonerCookGui.cookingPos.mX, CwagonerCookGui.cookingPos.mY, null);
		// Plating area
		g.drawImage(CwagonerCookGui.tableImg, CwagonerCookGui.platingPos.mX, CwagonerCookGui.platingPos.mY, null);
    	// Fridge
		g.drawImage(CwagonerCookGui.fridgeImg, CwagonerCookGui.fridgePos.mX, CwagonerCookGui.fridgePos.mY, null);

		synchronized(CwagonerRestaurant.guis) {
	        for (CwagonerGui gui : CwagonerRestaurant.guis) {
	            gui.draw(g2);
	        }
        }
    }
}
