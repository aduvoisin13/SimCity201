//package restaurant.restaurant_maggiyan.gui;
//
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JCheckBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
//import city.gui.CityCard;
//import city.gui.SimCityGui;
///**
// * Main GUI class.
// * Contains the main frame and subsequent panels
// */
//public class MaggiyanRestaurantGui extends CityCard implements ActionListener {
//    /* The GUI has two frames, the control frame (in variable gui) 
//     * and the animation frame, (in variable animationFrame within gui)
//     */
//	//JFrame animationFrame = new JFrame("Restaurant Animation");
//	MaggiyanAnimationPanel animationPanel = new MaggiyanAnimationPanel();
//	
//    /* restPanel holds 2 panels
//     * 1) the staff listing, menu, and lists of current customers all constructed
//     *    in RestaurantPanel()
//     * 2) the infoPanel about the clicked Customer (created just below)
//     */   
//    public MaggiyanRestaurantPanel restPanel;
//    
//    /* infoPanel holds information about the clicked customer, if there is one*/
//    private JPanel infoPanel;
//    private JPanel myPanel; 
//    private JLabel myLabel; 
//    private JLabel infoLabel; //part of infoPanel
//    private JCheckBox stateCB;//part of infoLabel
//
//    private Object currentPerson;/* Holds the agent that the info is about.
//    								Seems like a hack */
//
//    /**
//     * Constructor for RestaurantGui class.
//     * Sets up all the gui components.
//     */
//    public MaggiyanRestaurantGui(SimCityGui city) {
//        super(city); 
//        restPanel = new MaggiyanRestaurantPanel(this);
//    	int WINDOWX = 500;
//        int WINDOWY = 500;
//
//        
//        setBounds(0, 0, WINDOWX, WINDOWY);
//        setLayout(new GridLayout(1,2));
//
////        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));
////        restPanel.setBackground(Color.white);
////        restPanel.setPreferredSize(restDim);
////        restPanel.setMinimumSize(restDim);
////        restPanel.setMaximumSize(restDim); 
////        add(restPanel);
//        
//        add(animationPanel); 
//        
//    }
//    
//    /**
//     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
//     * changes the information panel to hold that person's info.
//     *
//     * @param person customer (or waiter) object
//     */
//    public void updateInfoPanel(Object person) {
////        stateCB.setVisible(true);
////        currentPerson = person;
////
////        if (person instanceof CustomerAgent) {
////            CustomerAgent customer = (CustomerAgent) person;
////            stateCB.setText("Hungry?");
////          //Should checkmark be there? 
////            stateCB.setSelected(customer.getGui().isHungry());
////          //Is customer hungry? Hack. Should ask customerGui
////            stateCB.setEnabled(!customer.getGui().isHungry());
////          // Hack. Should ask customerGui
////            infoLabel.setText(
////               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
////        }
////        infoPanel.validate();
//    }
//    /**
//     * Action listener method that reacts to the checkbox being clicked;
//     * If it's the customer's checkbox, it will make him hungry
//     * For v3, it will propose a break for the waiter.
//     */
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == stateCB) {
//            if (currentPerson instanceof MaggiyanCustomerRole) {
//                MaggiyanCustomerRole c = (MaggiyanCustomerRole) currentPerson;
//                //c.getGui().setHungry();
//                stateCB.setEnabled(false);
//            }
//        }
//      
//    }
//    /**
//     * Message sent from a customer gui to enable that customer's
//     * "I'm hungry" checkbox.
//     *
//     * @param c reference to the customer
//     */
////    public void setCustomerEnabled(MaggiyanCustomerRole c) {
////    	for(int i = 0; i<restPanel.getCustPanel().getCheckBoxList().size(); i++){
////    		JCheckBox tempBox = restPanel.getCustPanel().getCheckBoxList().get(i); 
////    		if(c.getName() == tempBox.getText()){
////    			tempBox.setEnabled(true);
////    			tempBox.setSelected(false);
////    		}
////    	}
////    	
////        if (currentPerson instanceof MaggiyanCustomerRole) {
////            MaggiyanCustomerRole cust = (MaggiyanCustomerRole) currentPerson;
////            if (c.equals(cust)) {
////                stateCB.setEnabled(true);
////                stateCB.setSelected(false);
////            }
////        }
////    }
//    
////    public void setWaiterEnabled(MaggiyanWaiter w){
////    	for(int i = 0; i<restPanel.getCustPanel().getWaiterCBList().size(); i++){
////    		JCheckBox tempBox = restPanel.getCustPanel().getWaiterCBList().get(i); 
////    		if(w.getName() == tempBox.getText()){
////    			tempBox.setEnabled(true);
////    			tempBox.setSelected(false);
////    		}
////    	}
////    }
//    
//    /**
//     * Main routine to get gui started
//     */
////    public static void main(String[] args) {
////        MaggiyanRestaurantGui gui = new MaggiyanRestaurantGui();
////        gui.setTitle("csci201 Restaurant");
////        gui.setVisible(true);
////        gui.setResizable(false);
////        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////    }
//}
