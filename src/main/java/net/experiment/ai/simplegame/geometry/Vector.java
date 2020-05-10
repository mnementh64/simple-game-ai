package net.experiment.ai.simplegame.geometry;

public class Vector {
    double row;
    double col;

    public Vector(double row, double col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
