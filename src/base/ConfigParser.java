package base;

import java.io.FileNotFoundException;
import java.util.Scanner;

import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import city.gui.SimCityGui;

/*
 * Reads in a config file filled with new people to instantiate
 * @author David Carr, Shane Mileham
 */
public class ConfigParser {

	private static ConfigParser instance = null;

	public void readFileCreatePersons(SimCityGui simcitygui, int fileNum) throws FileNotFoundException {
		Scanner scanFile = new Scanner(getClass().getResourceAsStream("/runconfig/config"+fileNum+".txt"));
		
		//Instantiate the base roles before creating the people
		SortingHat.InstantiateBaseRoles();
		
		while (scanFile.hasNext()) {
			//Order of Inputs: Job Type (BANK, MARKET, RESTAURANT, NONE), Cash, Name
			Scanner scanPerson = new Scanner(scanFile.nextLine()); //separate by person
			
			//Job
			String jobString = scanPerson.next();

			EnumJobType jobType = null;
			if (jobString.equals("BANK")) {
				jobType = EnumJobType.BANK;
			}
			if (jobString.equals("RESTAURANT")) {
				jobType = EnumJobType.RESTAURANT;
			}
			if (jobString.equals("NONE")) {
				jobType = EnumJobType.NONE;
			}
			if (jobString.equals("TRANSPORTATION")) {
				jobType = EnumJobType.TRANSPORTATION;
			}
			//EnumJobType jobType = EnumJobType.valueOf(jobString);	
			

//			EnumJobType jobType = null;
//			if (jobString.equals("BANK")) {
//				jobType = EnumJobType.BANK;
//			}
//			if (jobString.equals("RESTAURANT")) {
//				jobType = EnumJobType.RESTAURANT;
//			}
//			if (jobString.equals("NONE")) {
//				jobType = EnumJobType.NONE;
//			}
			jobType = EnumJobType.valueOf(jobString);

			//Cash
			String cashString = scanPerson.next();
			double cash = Double.valueOf(cashString);
			
			//Name
			String name = scanPerson.next();
			
			//Scenario
//			String scenarioString = scanPerson.next();
//			EnumScenarioType scenarioType = EnumScenarioType.valueOf(scenarioString);
			
			//Person
			Person person = new PersonAgent(jobType, cash, name); //adds role automatically
			synchronized (person) {
				simcitygui.citypanel.masterPersonList.add(person);
				simcitygui.citypanel.addMoving(person.getPersonGui()); //allow to move
				((PersonAgent) person).startThread();
			}
			
			scanPerson.close();
		}
		scanFile.close();

	}

	private ConfigParser() {
	}

	public static ConfigParser getInstanceOf() {
		if (instance == null) {
			instance = new ConfigParser();
		}
		return instance;
	}

}
