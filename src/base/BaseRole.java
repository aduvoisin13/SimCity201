package base;

import base.interfaces.Person;
import base.interfaces.Role;

public class BaseRole implements Role{
	
	protected Person mPerson;
	
	//NEEDED METHODS
	protected void stateChanged(){
		((PersonAgent)mPerson).stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		
		return false;
	}
	
	//ACCESSORS
	public void setPerson(PersonAgent person){
		mPerson = person;
	}
	
	public PersonAgent getPersonAgent(){
		return ((PersonAgent)mPerson);
	}
	
	public boolean isActive(){
		return true;
		//TODO: Fix this
	}
	
	/* Utilities */
	
	protected void print(String msg) {
	}
	
	public int getSSN(){
		return mPerson.getSSN();
	}
}
