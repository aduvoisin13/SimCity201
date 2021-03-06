package test.mock;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.text.Position;

import base.Location;

/**
 * MockPersonGui built to test Person
 * 
 * @author Rex Xu
 * 
 */
public class MockPersonGui extends Mock implements PersonGuiInterface {

	@Override
	public void setUpAStar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToDestination(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToDestination(Location location) {
		log.add(new LoggedEvent("DoGoToDestination"));
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		log.add(new LoggedEvent("setPresent: "+state));
	}

	@Override
	public void guiMoveFromCurrentPostionTo(Position to) {
		// TODO Auto-generated method stub
		
	}
	

}
