package common;

import java.util.ArrayList;

import CustomViews.Node;

/**
 * Created by Thiem on 10-May-16.
 */
public abstract class Tree {
    private ArrayList<Node> _listNode;
    private ArrayList<Link> _listLink;
    private int _matrixLink[][];

    public Tree(ArrayList<Node> listNode, int[][] matrixLink){
        this._listNode = listNode;
        this._matrixLink = matrixLink;
        makeLinkList();
    }

    private void makeLinkList(){
        for (int i=0;i<_matrixLink.length;i++)
            for (int j=0;j<_matrixLink[i].length;j++)
                this._listLink.add(new Link(i,_matrixLink[i][j],j));
    }

    public abstract ArrayList<Link> linkTree();

    public ArrayList<Node> get_listNode() {
        return _listNode;
    }

    public void set_listNode(ArrayList<Node> _listNode) {
        this._listNode = _listNode;
    }

    public ArrayList<Link> get_listLink() {
        return _listLink;
    }

    public void set_listLink(ArrayList<Link> _listLink) {
        this._listLink = _listLink;
    }

    public int[][] get_matrixLink() {
        return _matrixLink;
    }

    public void set_matrixLink(int[][] _matrixLink) {
        this._matrixLink = _matrixLink;
    }
}
