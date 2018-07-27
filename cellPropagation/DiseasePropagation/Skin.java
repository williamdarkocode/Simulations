public class Skin extends Thread{
    private Organism person;
    public Skin(Organism p) {
        this.person = p;
        this.start();
    }
    
    
    public void run() {
        this.person.move();
    }
}