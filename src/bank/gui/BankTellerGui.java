package bank.gui;

import bank.interfaces.BankTeller;
import base.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BankTellerGui implements Gui {

	private BankTeller agent = null;
	private boolean isPresent = true;
	private BankPanel bankPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX1 = 250;
	static final int STARTPOSX2 = 200;
	static final int STARTPOSX3 = 300;
	static final int STARTPOSY = 470;
	
	public BankTellerGui(BankTeller bt, BankPanel bp) {
		agent = bt;
		bankPanel = bp;
		xPos =  235;
		yPos = 0;
		xDestination = STARTPOSX1;
		yDestination = STARTPOSY;
	}
	
	//Animation Upgrades
	private BufferedImage bankTellerImage;
	
	public BankTellerGui(BankTeller bt, BankPanel bp, int position) {
		agent = bt;
		bankPanel = bp;
		xPos = -20;
		yPos = -20;
		yDestination = STARTPOSY;
		
		bankTellerImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/girl1.png");
    		bankTellerImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
		
		switch (position){
		case 1:	xDestination = STARTPOSX1; break;
		case 2: xDestination = STARTPOSX2; break;
		case 3: xDestination = STARTPOSX3; break;
		default: xDestination = STARTPOSX1; break;
		}
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
	}

	public void draw(Graphics2D g) {
		
//		g.setColor(Color.BLUE);
//		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
//		
		g.drawImage(bankTellerImage, xPos, yPos, null);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
	
//	MESSAGES
	public void DoLeaveBank() {
		xDestination = 220;
		yDestination = -50;
	}
}