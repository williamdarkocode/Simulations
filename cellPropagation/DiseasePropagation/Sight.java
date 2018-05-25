import java.util.*;
import java.awt.Color;
public class Sight {
    private Organism person;
    private Map<Coordinate, Organism> mapOfLife;
    private Coordinate centre;
    private double radius;
    private Map<Coordinate, Organism> view;
    public Sight(Organism person, Map<Coordinate, Organism> mapOfLife) {
        this.person = person;
        this.mapOfLife = mapOfLife;
        this.centre = person.getPosition();
        this.radius = this.person.getRadius()*3;
        resetSight();
    }
    
    public void resetSight() {
        this.radius = this.person.getRadius()*5;
        StdDraw.setPenRadius(0.005);
        //Color c = this.person.getColour();
        if(this.person.getColour().equals("BLUE")) {
            StdDraw.setPenColor(Color.getColor(this.person.getColour()));
        }
        else  {
            StdDraw.setPenColor(Color.getColor(this.person.getColour()));
        }
        
        StdDraw.circle(this.person.getPosition().getX(), this.person.getPosition().getY(), this.radius);
        StdDraw.show();
    }
    
    
    public Map<Coordinate, Organism> getInSight() {
        this.view = new HashMap<Coordinate, Organism>();
        for(Coordinate c: this.mapOfLife.keySet()) {
            boolean inSight = (c.getX() <= centre.getX()+radius && c.getY() <= centre.getY()+radius) || (c.getX() >= centre.getX()-radius && c.getY() <= centre.getY()+radius)
                               || (c.getX() >= centre.getX()-radius && c.getY() >= centre.getY()-radius) || (c.getX() <= centre.getX()+radius && c.getY() >= centre.getY()-radius) ;
                               
                               
            if(inSight) {
                view.put(c,mapOfLife.get(c));
            }
        }
        
        return this.view;
    }
    
    public double getScope() {
        return radius;
    }
    
    public void calibrate() {
        this.centre = this.person.getPosition();
    }
    
    public void expandScope(double delta) {
        this.radius+=delta;
    }
}