import java.util.*;

public class Population {
    private Map<Coordinate, Organism> sampleGroup;
    private Map<Coordinate, Organism> infectedGroup;
    private Map<Coordinate, Organism> suceptibleGroup;
    private Map<Coordinate, Organism> deadGroup;
    private Map<Coordinate, Organism> medicGroup;
    private Map<Coordinate, Organism> aliveGroup;
    private Map<Coordinate, Organism> greenGroup;
    private int totalPopulation;
    private int[] civilianBounds;
    private int[] rescuerBounds;
    private int worldBounds[];
    private String[] personType = new String[] {"REGULAR", "RESCUE"};
    
    public Population(int total, int[] worldBounds) {
        this.totalPopulation = total;
        this.worldBounds =  worldBounds;
        this.civilianBounds = worldBounds;
        this.rescuerBounds = new int[] {worldBounds[0]/2,worldBounds[1]/2};
        for(int i = 0; i < total; i++) {
            //double x = Math.random() * civilianB
            //this.sampleGroup.put(new Person());
        }
    }
}