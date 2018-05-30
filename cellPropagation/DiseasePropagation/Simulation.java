import java.util.*;
import java.awt.Color;
public class Simulation {
    private int domain;
    private int range;
    private int gridSize;
    public Simulation(int domain, int range, int gridSize) {
        this.domain = domain;
        this.range = range;
        this.gridSize = gridSize;
        setGrid(gridSize, gridSize);
        
    }
    
    
    public void setGrid(int width, int height) {
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, domain);
        StdDraw.setYscale(0, range);
        //StdDraw.enableDoubleBuffering();
    }
    
    
    
    public int getDomain() {
        return this.domain;
    }
    
    public int getRange() {
        return this.range;
    }
    
    
    public void setRange(int r) {
        this.range = r;
    }
    
    public void setDomain(int d) {
        this.domain = d;
    }
    
    public void expandGraph(double delta) {
        int del = (int)delta;
        this.domain+=delta;
        this.range+= delta;
    }
    
    
    public static void main(String[] args) {
        int[] bounds = new int[] {700,700};
        Simulation gridTest = new Simulation(bounds[0],bounds[1], 1600);
        Map life = new HashMap<Coordinate, Organism>();
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(100,100,10);
        StdDraw.show();
        Person p = new Person(350, 350, "RESCUE", 5, bounds);
        
        //Person v = new Person(500,500, "Regular", life, 50, StdDraw.GREEN, 10, bounds);
        //Person n = new Person(500,600, "Regular", life, 50, StdDraw.GREEN, 10, bounds);
        
    }
}