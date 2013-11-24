package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import base.Gui;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.interfaces.Waiter;

public class WaiterGui implements Gui {

	private Waiter agent = null;

	private int xPos, yPos = -20;
	private int xHome, yHome;
	private int xDestination = -20, yDestination = -20;
	private static int WaiterSize = 20;
	private String labelText = "";

	private boolean currentlyAnimating;

	public WaiterGui(Waiter agent, int home) {
		currentlyAnimating = false;
		this.agent = agent;
		xHome = 10;
		yHome = 30*(3+home);
		xPos = xHome;
		yPos = yHome;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos += 2;
		else if (xPos > xDestination)
			xPos -= 2;

		if (yPos < yDestination)
			yPos += 2;
		else if (yPos > yDestination)
			yPos -= 2;
		if (xPos == xDestination && yPos == yDestination && currentlyAnimating) {
			currentlyAnimating = false;
			agent.msgDoneAnimating();
		}
	}

	public void wantsBreak() {
		agent.RequestBreak();
	}

	public boolean isOnBreak() {
		return agent.isOnBreak();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, WaiterSize, WaiterSize);
		g.setColor(Color.BLACK);
		g.drawString(labelText, xPos, yPos);
	}

	public boolean isPresent() {
		return true;
	}

	public void setLabelText(String text) {
		labelText = text;
	}

	public void DoGoToTable(Table table) {
		currentlyAnimating = true;
		xDestination = table.getX() + 20;
		yDestination = table.getY() - 20;
	}

	public void DoGoToCustomer(int loc) {
		currentlyAnimating = true;
		xDestination = loc;
		yDestination = -10;
	}

	public void DoGoToKitchen() {
		currentlyAnimating = true;
		xDestination = 250;
		yDestination = 600;
	}
	
	public void DoGoToFront() {
		currentlyAnimating = true;
		xDestination = xHome;
		yDestination = yHome;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}