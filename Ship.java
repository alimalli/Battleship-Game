public class Ship {

    private String name;
    private int size;
    private int hitCount;

    public Ship(String name, int size){
        this.name = name;
        this.size = size;
        this.hitCount = 0;
    }

    public int getSize(){
        return size;
    }

    public void hit() {
        hitCount++;
    }

    public boolean isSunk() {
        if (hitCount == size){
            return true;
        }
        else return false;
    }

    public String toString(){
        return name;
    }
}
