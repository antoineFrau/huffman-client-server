package Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tree {

    private Map<String, Character> charWithCode;
    private ArrayList<Element> elList;

    public Tree(HashMap<Character, Integer> fromFileList) {
        this.charWithCode = new HashMap<>();
        this.elList = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : fromFileList.entrySet())
            this.elList.add(new Leaf(entry.getValue(), entry.getKey()));
        generateTree();
        generateCode(this.elList.get(0), "");
    }

    private void generateTree(){

        while(this.elList.size() > 1){
            Element curEl = this.elList.get(0);
            Element nextEl = this.elList.get(1);
            int sum = curEl.getOcc() + nextEl.getOcc();
            Node n;
            // Ables to know if we put it on the left or right
            if(curEl.compareTo(nextEl)>=0)
                n = new Node(sum, curEl, nextEl);
            else
                n = new Node(sum, nextEl, curEl);
            this.elList.remove(curEl);
            this.elList.remove(nextEl);
            this.elList.add(n);
            Collections.sort(this.elList);
        }
    }

    private void generateCode(Element el, String code){
        if(el instanceof Leaf) {
            int nbzero = 8-code.length();
            String r = code;
            if(nbzero>0)
                r += String.join("", Collections.nCopies(nbzero, "0"));
            this.charWithCode.put(r, ((Leaf) el).getC());
        } else{
            code += "0";
            generateCode(((Node)el).getLeft(), code);
            code = code.substring(0,code.length()-1);
            code += "1";
            generateCode(((Node)el).getRight(), code);
            code = code.substring(0, code.length()-1);
        }
    }

    public Map<String, Character> getCharWithCode() { return charWithCode; }

}
