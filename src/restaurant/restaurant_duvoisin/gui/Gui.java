package restaurant.restaurant_duvoisin.gui;

import java.awt.Graphics2D;

public interface Gui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();

}
