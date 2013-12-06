package city.gui;

//import housing.gui.HousingHouseGuiPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import city.gui.trace.*;
import base.ConfigParser;

@SuppressWarnings("serial")
public class CityControlPanel extends JPanel implements ActionListener{
	
	SimCityGui city;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 600;
	public static final int TABBED_HEIGHT_ADJ = 42;
	public static final int numConfigs = 5;
	JButton addRestaurant, addBank, housingGUIButton;
	JPanel configList;
	List<JButton> configOptions;
	
	// Title & Pause Button
	JLabel title = new JLabel("Control Panel");
//	JButton startButton = new JButton("Start SimCity201");
	
	/* ANDRE JERRY No days of week - discuss w/Shane
	// Select Day
	JLabel dayTitle = new JLabel("Day of Week:");
	String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	JComboBox currentDay = new JComboBox(days);
	*/

	// Scenarios
	JLabel scenarioTitle = new JLabel("Load Selected Scenario:");
	JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JScrollPane NormsPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JScrollPane NonNormsPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	JPanel view = new JPanel();
    
    // Tabs
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel RestaurantTab = new JPanel();
    JPanel CommercialTab = new JPanel();
    JPanel ScenariosTab = new JPanel();
    JPanel TraceTab = new JPanel();
    
    // Trace Panel
    TracePanel tracePanel;
    
    // Selection for Trace Panel
    JPanel traceSelectionPanel;
    // Levels
    JCheckBox CBLevel_ERROR;
    JCheckBox CBLevel_WARNING;
    JCheckBox CBLevel_INFO;
    JCheckBox CBLevel_MESSAGE;
    JCheckBox CBLevel_DEBUG;
    // Tags
    JComboBox tags;
    String[] tagList = { "All", "Person", "Bank Teller", "Bank Customer", "Bus Stop", "Restaurant", "Bank", "General City" };
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Title
		Font font = title.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		attributes.put(TextAttribute.SIZE, 20);
		title.setFont(font.deriveFont(attributes));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
		
		// Pause Button
//		Dimension pauseBtnDim = new Dimension(CP_WIDTH - 10, 30);
//		startButton.setPreferredSize(pauseBtnDim);
//		startButton.setMinimumSize(pauseBtnDim);
//		startButton.setMaximumSize(pauseBtnDim);
//		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//		startButton.addActionListener(this);
//		add(startButton);
		
//		// Select Day
//		dayTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(dayTitle);
//		
//		Dimension dayCBDim = new Dimension(CP_WIDTH - 10, 25);
//		currentDay.setPreferredSize(dayCBDim);
//		currentDay.setMinimumSize(dayCBDim);
//		currentDay.setMaximumSize(dayCBDim);
//		add(currentDay);
		
		scenarioTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scenarioTitle);
		
		// Scenario
		view.setLayout(new BoxLayout((Container)view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        Dimension scrollPaneDim = new Dimension(CP_WIDTH - 10, 550);
        pane.setPreferredSize(scrollPaneDim);
        pane.setMinimumSize(scrollPaneDim);
        pane.setMaximumSize(scrollPaneDim);
        
     // Tabs

        JLabel filler2 = new JLabel("Panel #2");
        filler2.setHorizontalAlignment(JLabel.CENTER);
        RestaurantTab.setLayout(new GridLayout(1, 2));
        ScenariosTab.setLayout(new GridLayout(1, 2));
        
        Dimension tabbedPaneDim = new Dimension(CP_WIDTH, CP_HEIGHT - TABBED_HEIGHT_ADJ);
        tabbedPane.setPreferredSize(tabbedPaneDim);
        tabbedPane.setMinimumSize(tabbedPaneDim);
        tabbedPane.setMaximumSize(tabbedPaneDim);
        
        //tabbedPane.setLayout(new GridLayout(1,3));
        tabbedPane.addTab("Trace", TraceTab);
        tabbedPane.addTab("Commercial", CommercialTab);
        tabbedPane.addTab("Scenarios", ScenariosTab);
        tabbedPane.addTab("Restaurants ", RestaurantTab);
        add(tabbedPane);
        
        // Trace Panel
        initTrace();
        TraceTab.setLayout(new BorderLayout(0, 5));
        TraceTab.add(traceSelectionPanel, BorderLayout.NORTH);
        TraceTab.add(tracePanel, BorderLayout.CENTER);
        
        //tabbedPane.addTab("Tab 1", icon, panel1, "Does nothing");
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
        //Add Buttons
//		addRestaurant = new JButton("Add Restaurant");
//		addRestaurant.addActionListener(this);
//		addRestaurant.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(addRestaurant);
//		addBank = new JButton("Add Bank");
//		addBank.addActionListener(this);
//		addBank.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(addBank);
        
        // Scenario
        view.setLayout(new BoxLayout((Container)view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        /*
         * Choose Config File
         */
        Dimension panelSize = new Dimension(scrollPaneDim.width - 20, scrollPaneDim.height);
    	configList = new JPanel();
    	configOptions = new ArrayList<JButton>();
    	configList.setPreferredSize(panelSize);
    	configList.setMinimumSize(panelSize);
    	configList.setMaximumSize(panelSize);
    	configList.setLayout(new FlowLayout());
    	
    	Dimension buttonDim = new Dimension(panelSize.width, 20);
    	/*JButton allBtn = new JButton("Simulate All");
    	allBtn.addActionListener(this);
    	allBtn.setPreferredSize(buttonDim);
    	allBtn.setMinimumSize(buttonDim);
    	allBtn.setMaximumSize(buttonDim);
    	configList.add(allBtn);
    	configOptions.add(allBtn);*/
    	//view.add(configList);
    	//tab1.add(configList);
    	
    	//Restaurant Config tab
    	//for (int i=0; i<8; i++) {
    		JButton Restaurant_Config = new JButton("Restaurant " + 0);
    		Restaurant_Config.addActionListener(this);
    		Restaurant_Config.setPreferredSize(buttonDim);
	    	Restaurant_Config.setMinimumSize(buttonDim);
	    	Restaurant_Config.setMaximumSize(buttonDim);
	    	configList.add(Restaurant_Config);
	    	configOptions.add(Restaurant_Config);
    	//}
    	
    	view.add(configList);
    	RestaurantTab.add(pane);
    	
    	
    	
    	
    	
    	//Commercial Config Tab (bank and markets)
    	JButton bankBtn = new JButton("Bank");
    	bankBtn.addActionListener(this);
    	bankBtn.setPreferredSize(buttonDim);
    	bankBtn.setMinimumSize(buttonDim);
    	bankBtn.setMaximumSize(buttonDim);
    	CommercialTab.add(bankBtn);
    	configOptions.add(bankBtn);
    	
    	JButton marketBtn = new JButton("Food Market");
    	marketBtn.addActionListener(this);
    	marketBtn.setPreferredSize(buttonDim);
    	marketBtn.setMinimumSize(buttonDim);
    	marketBtn.setMaximumSize(buttonDim);
    	CommercialTab.add(marketBtn);
    	configOptions.add(marketBtn);
    	
    	//Scenarios Config Tab
    	JLabel NormLabel = new JLabel("Norms");
    	JLabel NonNormLabel = new JLabel("Non Norms");
    	
    	JButton partyBtn = new JButton("Party");
    	partyBtn.addActionListener(this);
    	partyBtn.setPreferredSize(buttonDim);
    	partyBtn.setMinimumSize(buttonDim);
    	partyBtn.setMaximumSize(buttonDim);
    	//ScenariosTab.add(NormLabel);
    	//ScenariosTab.add(NonNormLabel);
    	ScenariosTab.add(partyBtn);
    	configOptions.add(partyBtn);
    	//view.add(configList);

    	//pane.add(configList);
    	/*
    	JButton houseBtn = new JButton("Housing");
    	houseBtn.addActionListener(this);
    	houseBtn.setPreferredSize(buttonDim);
    	houseBtn.setMinimumSize(buttonDim);
    	houseBtn.setMaximumSize(buttonDim);
    	configList.add(houseBtn);
    	configOptions.add(houseBtn);
    	view.add(configList);

    	view.add(configList);
    	 */
        //add(pane);
        
        	
	}
	
	public void initTrace() {
		tracePanel = new TracePanel();
		
		// Show/Hide Alerts (defaults set here)
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		
		// Action Listener
		AlertLog.getInstance().addAlertListener(tracePanel);
		
		// Trace Selection Panel Panel
		traceSelectionPanel = new JPanel();
		traceSelectionPanel.setLayout(new BorderLayout(0, 5));
		
		// NOTE: Must do SOUTH before NORTH or ComboBox gets overlapped. lol
	    // South: JCheckBoxES
		JPanel holdLevels = new JPanel();
		holdLevels.setLayout(new BorderLayout());
		
		JLabel levelsTitle = new JLabel("Select Levels to View:");
		
		JPanel holdCBs = new JPanel();
		holdCBs.setLayout(new GridLayout(3, 2));
	    CBLevel_ERROR = new JCheckBox("Errors", true);
	    CBLevel_WARNING = new JCheckBox("Warnings", true);
	    CBLevel_INFO = new JCheckBox("Info", true);
	    CBLevel_MESSAGE = new JCheckBox("Messages", true);
	    CBLevel_DEBUG = new JCheckBox("Debugs", true);
	    holdCBs.add(CBLevel_ERROR);
	    holdCBs.add(CBLevel_WARNING);
	    holdCBs.add(CBLevel_INFO);
	    holdCBs.add(CBLevel_MESSAGE);
	    holdCBs.add(CBLevel_DEBUG);
	    
	    holdLevels.add(levelsTitle, BorderLayout.NORTH);
	    holdLevels.add(holdCBs, BorderLayout.CENTER);
	    traceSelectionPanel.add(holdLevels, BorderLayout.CENTER);
	    
		// North: COMBOBOX
		JPanel holdTags = new JPanel();
		holdTags.setLayout(new BorderLayout());
		
		JLabel tagsTitle = new JLabel("Select Tags to View:");
		
	    tags = new JComboBox(tagList);
	    tags.setSelectedIndex(0);
	    
	    holdTags.add(tagsTitle, BorderLayout.NORTH);
	    holdTags.add(tags, BorderLayout.CENTER);
	    traceSelectionPanel.add(holdTags, BorderLayout.NORTH);
	    
	    // Add action listeners.
	    CBLevel_ERROR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_ERROR.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
				}
			}
		});
	    CBLevel_WARNING.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_WARNING.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.WARNING);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.WARNING);
				}
			}
		});
	    CBLevel_INFO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_INFO.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.INFO);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
				}
			}
		});
	    CBLevel_MESSAGE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_MESSAGE.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
				}
			}
		});
	    CBLevel_DEBUG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CBLevel_DEBUG.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.DEBUG);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
				}
			}
		});
	    
	    tags.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tracePanel.hideAlertsForAllTags();
				switch((String)tags.getSelectedItem()) {
					case "All":
						tracePanel.showAlertsForAllTags();
						break;
					case "Person":
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
						break;
					case "Bank Teller":
						tracePanel.showAlertsWithTag(AlertTag.BANK_TELLER);
						break;
					case "Bank Customer":
						tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
						break;
					case "Bus Stop":
						tracePanel.showAlertsWithTag(AlertTag.BUS_STOP);
						break;
					case "Restaurant":
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
						break;
					case "Bank":
						tracePanel.showAlertsWithTag(AlertTag.BANK);
						break;
					case "General City":
						tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
						break;
				}
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {		
		// JButton
		if(e.getSource() instanceof JButton) {
			for (int i = 0; i < 8; i++) {
				if (((JButton) e.getSource()).getText()
						.equals("Restaurant " + i)) {
					ConfigParser config = ConfigParser.getInstanceOf();
					try {
						config.readFileCreatePersons(city, "restConfig"+i+".txt");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
			if (((JButton) e.getSource()).getText().equals("Bank")) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "BankConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}/*
			if (((JButton) e.getSource()).getText().equals("Housing")) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "HouseConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if (((JButton) e.getSource()).getText().equals("Food Market")) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "marketConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}*/
			if (((JButton) e.getSource()).getText().equals("Party")) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "PartyConfig"+".txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}/*
			if (((JButton) e.getSource()).getText().equals("Simulate All")) {
				ConfigParser config = ConfigParser.getInstanceOf();
				try {
					config.readFileCreatePersons(city, "config1.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}*/
			for (JButton b : configOptions) {
				b.setEnabled(false);
			}
		}
	}
}
