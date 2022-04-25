package sample;

public class Coords {
    Double x;
    Double y;

    Coords(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public Double distanceTo(Coords coords){
        return Math.sqrt((coords.y - this.y) * (coords.y - this.y) + (coords.x - this.x) * (coords.x - this.x));
    }

    public Double xDist(Coords coords){
        return Math.abs(coords.x-this.x);
    }

    public Double yDist(Coords coords){
        return Math.abs(coords.y-this.y);
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
