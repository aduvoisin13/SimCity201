package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class LabelGui implements Gui{
	
	private String mLabel;
	private int mPosX;
	private int mPosY;
	private RestaurantGui mGUI;

	public LabelGui(String label, int x, int y, RestaurantGui gui){
		mLabel = label;
		mPosX = x;
		mPosY = y;
		mGUI = gui;
		
		mGUI.getAnimationPanel().addGui(this);
	}
	
	@Override
	public void updatePosition() {
		//none
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(mLabel, mPosX, mPosY);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void remove(){
		mGUI.getAnimationPanel().removeGui(this);
	}
	
	public void setLabel(String label){
		mLabel = label;
	}

}