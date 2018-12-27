package Tree;

public class Element implements Comparable {

    private int occ;

    public Element(int occ) {
        this.occ = occ;
    }

    public int getOcc() {
        return occ;
    }

    public void setOcc(int occ) {
        this.occ = occ;
    }

    @Override
    public String toString() {
        return String.format("(%1$s)", occ);
    }

    @Override
    public int compareTo(Object o) {
        int compareOcc = ((Element)o).getOcc();
        if(o instanceof Leaf &&
                this instanceof Leaf &&
                compareOcc == this.occ){
            int compareChar = ((Leaf)o).getC();
            int currChar = ((Leaf)this).getC();
            //Result negative curr is lower a(101) < e(106) so
            return currChar-compareChar;
        }

        return this.occ-compareOcc;
    }
}
