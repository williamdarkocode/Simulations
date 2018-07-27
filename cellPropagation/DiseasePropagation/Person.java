import java.util.*;
import java.awt.Color;

public class Person implements Organism {

    private double xPos;
    private double yPos;
    private Coordinate position;
    private double velocity;
    private Sight eye;
    private boolean isInfected;
    private boolean isRescue;
    private boolean isSuceptible;
    private boolean isDead;
    private boolean isAlive;
    private String type;
    private String colour;
    private double[] direction;
    private int radius;
    private int[] worldBounds;
    private double velocityConstant;
    private double healthPercentage = 100.0;

    public Person(double x, double y, String type, int radius, int[] worldBounds) {
        this.velocity = 100.0;
        direction = new double[] {0.0, Math.PI*2};

        this.xPos = x;
        this.yPos = y;
        this.position = new Coordinate(xPos,yPos);

        this.type = type;

        this.radius = radius;
        this.worldBounds = worldBounds;

        
        setInitialState();

        instantiateSelf();
        move();
    }

    

    public void changeStateInfected() {
        this.isInfected = true;
    }

    public void changeStateSuceptible() {
        this.isSuceptible = true;
    }

    public void changeStateDead() {
        this.isDead = true;
    }

    public void setInitialState() {
        switch(this.type) {
            case "RESCUE":
            this.colour = "BLUE";
            this.isInfected = false;
            this.isSuceptible = false;
            this.isDead = false;
            this.isAlive = true;
            this.velocityConstant = 10.0;
            this.eye = new Sight(this, new HashMap<Coordinate, Organism>());
            this.eye.expandScope(this.radius);
            break;
            case "REGULAR":
            this.colour = "GREEN";
            this.isInfected = false;
            this.isSuceptible = false;
            this.isDead = false;
            this.isAlive = true;
            this.velocityConstant = 5;
            this.eye = new Sight(this, new HashMap<Coordinate, Organism>());
            break;
        }
    }

    public void updateColour() {
        if(!this.type.equals("RESCUE")) {
            if(this.isInfected) {
                this.colour = "RED";
                StdDraw.setPenColor(StdDraw.RED);
            }
            else if(this.isSuceptible) {
                this.colour = "YELLOW";
                StdDraw.setPenColor(StdDraw.YELLOW);
            }
            else if(this.isDead) {
                this.colour = "BLACK";
                StdDraw.setPenColor(StdDraw.BLACK);
            }
            else {
                this.colour = "GREEN";
                StdDraw.setPenColor(StdDraw.GREEN);
            }
        }
    }

    public String getColour() {
        updateColour();
        return this.colour;
    }


    
    public void instantiateSelf() {
        //colour set goes here
        updateColour();
        StdDraw.clear();
        StdDraw.setPenRadius(0.05);

        StdDraw.filledCircle(xPos,yPos,this.radius);
        this.eye.resetSight();
        //StdDraw.show();
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
        double deltaX = Math.abs(target.getX()-this.xPos)/velocity * this.velocityConstant; // multiply this result by some rate that exponentially grows based on health;
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
        interpolate();
        //move();
    }

    public void checkNeighbours(){
    }

    public Coordinate getPosition(){
        return this.position;
    }

}