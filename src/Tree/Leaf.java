package Tree;

public class Leaf extends Element {

    private char c;

    public Leaf(int occ, char c) {
        super(occ);
        this.c = c;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Leaf{" + c + ',' + this.getOcc() + '}';
    }
}
