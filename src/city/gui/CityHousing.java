package city.gui;

import housing.gui.HousingGuiPanel;
import housing.interfaces.HousingRenter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityHousing extends CityComponent {
	private String houseName = "";
	public int xLocation, yLocation;
	public double mRent;
	public HousingRenter mOccupant;
	public enum HousingType{Apartment, House}; 
	public HousingType type; 
	public final double maxAptRent = 450.00;  
	public HousingGuiPanel mPanel;
	
	public CityHousing(SimCityGui city, int x, int y, String ID, double rent) {
		super(x, y, Color.blue, ID);
		houseName = ID;
		xLocation = x;
		yLocation = y;
		mOccupant = null;
		mRent = rent;
		rectangle = new Rectangle(x, y, 20, 20);
		mPanel = new HousingGuiPanel(city);
		
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
		}	
	}

	//For unit testing
	public CityHousing(int x, int y, String ID, double rent) {
		xLocation = x;
		yLocation = y;
		mRent = rent;
		mOccupant = null;
		houseName = ID;
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
		}	
	}
	
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
	
		g.setColor(color);
		g.fillRect(x, y, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString(houseName,x + 7 , y + 17);
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}	

}
