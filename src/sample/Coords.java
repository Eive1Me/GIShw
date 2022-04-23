package sample;

public class Coords {
    Double x;
    Double y;

    Coords(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
