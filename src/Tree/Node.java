package Tree;

public class Node extends Element {

    private Element right, left;


    public Node(int occ, Element right, Element left) {
        super(occ);
        this.right = right;
        this.left = left;
    }

    public Element getRight() {
        return right;
    }

    public void setRight(Element right) {
        this.right = right;
    }

    public Element getLeft() {
        return left;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    @Override
    public String toString() {
        //return String.format("(%1$s) - (%2$s)", left, right );
        return String.format("(%1$s)", getOcc());
    }
}
