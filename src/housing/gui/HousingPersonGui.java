package housing.gui;

import housing.roles.HousingBaseRole;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Gui;
import city.gui.CityHousing;

/*
 * @author David Carr
 */

public class HousingPersonGui implements Gui {

	public HousingBaseRole housingrole;
	
	//Initial Positions
	private int xPos = 250; 
	private int yPos = -50;
	private int xDestination = 75;
	private int yDestination = 265;
	private boolean currentlyAnimating;
	private boolean present;

	//Furniture Positions
	private int CHAIRXPOS = 280; 
	private int CHAIRYPOS = 210; 
	private int DiningTableDim = 50; 

	//Person Positions
	private int COUCHXPOS = 75;
	private int COUCHYPOS = 265;
	
	//Animation Images
	private BufferedImage image;
	
	//----Person Positions----
	
	//Dining table chair Position
	private int eatingXPos = 280; 
	private int eatingYPos = 210; 
	
	//Couch or Rest Position
	private int restingXPos = 75;
	private int restingYPos = 265; 
	
	//Maintenance Positions
	private int CORNERONEX = 115; 
	private int CORNERONEY = 120;
	private int CORNER2X = 420; 
	private int CORNER2Y = 110;
	private int CORNER3X = 445; 
	private int CORNER3Y = 275;
	
	private int maintenanceXPos = 30; 
	private int maintenanceYPos = 150; 
	
	private static int GUISIZE = 20;

	public HousingPersonGui(){ 
		super(); 
		
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/person.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
	}
	
	public void updatePosition() {
		if (xPos < xDestination)
			xPos += 1;
		else if (xPos > xDestination)
			xPos -= 1;

		if (yPos < yDestination)
			yPos += 1;
		else if (yPos > yDestination)
			yPos -= 1;
		
		if (xPos == xDestination && yPos == yDestination && currentlyAnimating) {
			currentlyAnimating = false;
			housingrole.msgDoneAnimating();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, xPos, yPos, null);
//		g.setColor(Color.BLUE);
//		g.fillRect(xPos, yPos, GUISIZE, GUISIZE);
	}

	@Override
	public boolean isPresent() {
		return present;
	}

	public void DoGoToHouse(CityHousing h) {
		xDestination = h.xLocation;
		yDestination = h.yLocation;
		currentlyAnimating = true;
	}

	public void DoLeaveHouse(CityHousing h) {
		xDestination = -20;
		yDestination = -20;
		currentlyAnimating = true;
	}

	public void DoCookAndEatFood() {
		xDestination = eatingXPos; 
		yDestination = eatingYPos; 
		currentlyAnimating = true;
	}

	public void DoMaintainHouse() {
		xDestination = maintenanceXPos;
		yDestination = maintenanceYPos; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC1() {
		xDestination = CORNERONEX;
		yDestination = CORNERONEY; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC2() {
		xDestination = CORNER2X;
		yDestination = CORNER2Y; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC3() {
		xDestination = CORNER3X;
		yDestination = CORNER3Y; 
		currentlyAnimating = true;
	}

	@Override
	public void setPresent(boolean state) {
		present = state;
	}
	
	public void DoGoRelax(){
		xDestination = restingXPos; 
		yDestination = restingYPos; 
			
		currentlyAnimating = true; 
	}
}
