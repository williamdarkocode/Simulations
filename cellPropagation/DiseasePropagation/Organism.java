import java.awt.Color;

public interface Organism
{
    Coordinate cartesianTarget();
    
    void interpolate();
    
    void customiseSelf(String colour);
    
    void move();
    
    void checkNeighbours();
    
    Coordinate getPosition();
    
    
    void instantiateSelf();
    
    String getColour();
    
    double getRadius();
    
    double[] computeSlope(Coordinate point1, Coordinate point2);
    
    void setDirection();
    
    double[] getDirection();
}
