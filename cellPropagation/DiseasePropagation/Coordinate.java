public class Coordinate {
    private double xCoord;
    private double yCoord;
    
    public Coordinate(double x, double y) {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    
    public double getX() {
        return this.xCoord;
    }
    
    public double getY() {
        return this.yCoord;
    }
    
    public double[] getCoord() {
        double arr[] = new double[2];
        arr[0] = xCoord;
        arr[1] = yCoord;
        return arr;
    }
    
    
    public void moveCoord(double delta) {
        this.xCoord+=delta;
        this.yCoord+=delta;
    }
    
    public void moveCoord(double xDelta, double yDelta) {
        this.xCoord+=xDelta;
        this.yCoord+=yDelta;
    }
    
    
    public double computeDistance (Coordinate point1, Coordinate point2) {
        double distance = Math.sqrt(Math.pow((point2.getX()-point2.getX()),2)+Math.pow((point2.getY()-point2.getY()),2));
        return distance;
    }
   
    
    
}