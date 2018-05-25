import java.util.*;
import java.awt.Color;

public class Person implements Organism {
    //colours

    public static final Color BLACK      = Color.BLACK;
    public static final Color BLUE       = Color.BLUE;
    public static final Color CYAN       = Color.CYAN;
    public static final Color DARK_GRAY  = Color.DARK_GRAY;
    public static final Color GRAY       = Color.GRAY;
    public static final Color GREEN      = Color.GREEN;
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    public static final Color MAGENTA    = Color.MAGENTA;
    public static final Color ORANGE     = Color.ORANGE;
    public static final Color PINK       = Color.PINK;
    public static final Color RED        = Color.RED;
    public static final Color WHITE      = Color.WHITE;
    public static final Color YELLOW     = Color.YELLOW;

    //colours

    private double xPos;
    private double yPos;
    private Coordinate position;
    private double velocity;
    private Sight eye;
    private String typeOfPerson;
    private boolean isInfected;
    private boolean isRescue;
    private boolean isSuceptible;
    private boolean isDead;
    private boolean isAlive;
    private String type;
    private Map<Coordinate, Organism> mapOfLife;
    private String colour;
    private double[] direction;
    private int radius;
    private int[] worldBounds;
    private double velocityConstant = 5.0;
    private double healthPercentage = 100.0;

    public Person(double x, double y, String type, Map<Coordinate, Organism> mapOfLife, double velocity, Color c, int radius, int[] worldBounds) {
        direction = new double[] {0.0, Math.PI*2};
        this.mapOfLife = mapOfLife;

        this.xPos = x;
        this.yPos = y;
        this.position = new Coordinate(xPos,yPos);

        this.type = type;

        this.eye = new Sight(this, mapOfLife);

        this.isInfected = false;
        this.isDead = false;
        this.isAlive = !this.isDead;
        this.isSuceptible = false;
        this.isRescue = this.type.equals("RESCUE");

        this.velocity = velocity;
        this.radius = radius;
        this.worldBounds = worldBounds;
        instantiateSelf();
        move();
    }

    public String getColour() {
        if(this.type.equals("RESCUE")) {
            this.colour = "BLUE";
            return "BLUE";
        }
        if(this.type.equals("REGULAR") && !this.isInfected && !this.isSuceptible && this.isAlive) {
            this.colour = "GREEN";
            return "GREEN";
        }
        return "GREEN";
    }

    public void instantiateSelf() {
        if(this.type.equals("RESCUE")) {
            StdDraw.setPenColor(this.BLUE);
            this.colour = "BLUE";
        }
        if(this.type.equals("REGULAR") && !this.isInfected && !this.isSuceptible && this.isAlive) {
            StdDraw.setPenColor(this.GREEN);
            this.colour = "GREEN";
        }

        StdDraw.clear();
        StdDraw.setPenRadius(0.05);

        StdDraw.filledCircle(xPos,yPos,this.radius);
        this.eye.resetSight();
        StdDraw.show();
    }

    public double getRadius() {
        return this.radius;
    }

    public double[] getDirection() {
        return this.direction;
    }

    public void setDirection() {
        //left top corner
        if(this.xPos-eye.getScope() <= 0 && this.yPos + eye.getScope() >= this.worldBounds[1]) {
            direction = new double[] {-5*(Math.PI/6),(Math.PI/3)};
        }
        //top right corner
        else if(this.xPos+eye.getScope() >= this.worldBounds[0] && this.yPos + eye.getScope() >= this.worldBounds[1]) {
            direction = new double[] {2*(Math.PI/3),11*(Math.PI/6)};
        }
        //Bottom right corner
        else if(this.xPos+eye.getScope() >= this.worldBounds[0] && this.yPos - eye.getScope() <= 0) {
            direction = new double[] {Math.PI/6,4*(Math.PI/3)};
        }
        //Bottom left Corner
        else if(this.xPos-eye.getScope() <= 0 && this.yPos - eye.getScope() <= 0) {
            direction = new double[] {-3*(Math.PI/6),5*(Math.PI/6)};
        }
        //right half off
        else if(this.xPos+eye.getScope() >= this.worldBounds[0]) {
            direction = new double[] {Math.PI/2,3*(Math.PI/2)};
        }
        //left half off
        else if(this.xPos-eye.getScope() <=0) {
            direction = new double[] {-Math.PI/2, Math.PI/2};
        }
        //bottom half off
        else if(this.yPos-eye.getScope() <=0) {
            direction = new double[] {0,Math.PI};
        }
        //top half off
        else if(this.yPos+eye.getScope() >= this.worldBounds[1]) {
            direction = new double[] {Math.PI,2*Math.PI};
        }
        //default 360 degree view
        else {
            direction = new double[] {0.0, Math.PI*2};
        }

    }

    public Coordinate cartesianTarget(){
        setDirection();
        double twoPI = Math.PI * 2;
        double angle = (Math.random()*direction[1])+direction[0];

        double cartesianX = this.velocity*(Math.cos(angle));

        double cartesianY = this.velocity*(Math.sin(angle));

        System.out.println(cartesianX+"  ****INITAL CART X*****");
        System.out.println(cartesianY+"  ****INITAL CART Y*****");

        Coordinate target = new Coordinate(this.xPos+cartesianX, this.yPos+cartesianY);
        System.out.println("New Set-------------------------------------------------------");
        System.out.println("ANGLE: "+ angle + "Cartesian X: " + cartesianX + ",  Cartesian Y: " + cartesianY);
        System.out.println("M: " + computeSlope(this.position,target)[0] + ",  B: " + computeSlope(this.position,target)[1]);
        System.out.println("SELF: "+position.getX()+ " :X, "+ position.getY() + " :Y");
        System.out.println("TARGET: "+target.getX()+ " :X, "+ target.getY() + " :Y");
        System.out.println("");
        return target;
    }

    public double[] computeSlope(Coordinate point1, Coordinate point2) {
        double slope = (point2.getY() - point1.getY())/(point2.getX()-point1.getX());
        double yIntercept = point1.getY() - (slope*point1.getX());
        double[] slopeInterceptForm = {slope, yIntercept};
        return slopeInterceptForm;
    }

    public void interpolate(){
        Coordinate target = cartesianTarget();
        if(target.getX() < 0 || target.getX() > this.worldBounds[0] || target.getY() < 0 || target.getY() > this.worldBounds[1]) {
            do{
                target = cartesianTarget();
            }while(target.getX() < 0 || target.getX() > this.worldBounds[0] || target.getY() < 0 || target.getY() > this.worldBounds[1]);
        }

        int numIntervals = (int)(Math.abs(target.getX()-this.position.getX())/velocity);
        double deltaX = Math.abs(target.getX()-this.xPos)/velocity * 5; // multiply this result by some rate that exponentially grows based on health;
        double[] slopeIntercept = computeSlope(this.position, target);
        /*

        while(this.xPos != target.getX() && this.yPos != target.getY()) {
        if(this.xPos == target.getX() && this.yPos == target.getY()){
        this.position = target;
        target = cartesianTarget();
        }

        if(target.getX() < this.xPos) {
        this.xPos-=deltaX;
        }
        else {
        this.xPos+=deltaX;
        }

        System.out.println("START: " + this.xPos+ " X" +" "+ this.yPos + " Y");
        this.yPos = (slopeIntercept[0]*this.xPos)+slopeIntercept[1];
        this.position = new Coordinate(this.xPos,this.yPos);
        System.out.println("END: "+this.xPos+ " X" +" "+ this.yPos + " Y");
        instantiateSelf();
        }
        this.position = target;
         */
        if((slopeIntercept[0] < 0 && target.getX() < this.xPos) || (slopeIntercept[0] > 0 && target.getX() < this.xPos)) {
            deltaX*=-1;
        }

        double i = this.xPos;

        boolean cond;
        if((slopeIntercept[0] < 0 && target.getX() < this.xPos) || (slopeIntercept[0] > 0 && target.getX() < this.xPos)) {
            for(i = this.xPos; i>target.getX(); i+=deltaX) {
                slopeIntercept = computeSlope(this.position, target);
                System.out.println("START: " + this.xPos+ " X" +" "+ this.yPos + " Y" + "DELTA: "+ deltaX);
                this.xPos = i;
                this.yPos = (slopeIntercept[0]*this.xPos)+slopeIntercept[1];
                this.position = new Coordinate(this.xPos,this.yPos);
                System.out.println("END: "+this.xPos+ " X" +" "+ this.yPos + " Y");
                instantiateSelf();
            }
        }
        else {
            for(i = this.xPos; i<target.getX(); i+=deltaX) {
                slopeIntercept = computeSlope(this.position, target);
                System.out.println("START: " + this.xPos+ " X" +" "+ this.yPos + " Y" + "DELTA: "+ deltaX);
                this.xPos = i;
                this.yPos = (slopeIntercept[0]*this.xPos)+slopeIntercept[1];
                this.position = new Coordinate(this.xPos,this.yPos);
                System.out.println("END: "+this.xPos+ " X" +" "+ this.yPos + " Y");
                instantiateSelf();
            }
        }

        //return new Coordinate(50,50);
    }

    public void customiseSelf(String colour){

    }

    public void move(){
        /*
        for(int i = 0; i  < 500; i++) {
        interpolate();
        }*/

        interpolate();
        move();
    }

    public void checkNeighbours(){
    }

    public Coordinate getPosition(){
        return this.position;
    }

}