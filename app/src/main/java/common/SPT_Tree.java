package common;

import java.util.ArrayList;

import CustomViews.Node;

/**
 * Created by Thiem on 10-May-16.
 */
public class SPT_Tree extends Tree{
    int numberNode;
    int C;
    ArrayList<Node> nodes;
    double A;
    double r[][];
    public SPT_Tree(ArrayList<Node> listNode, int[][] matrixLink, int c, double A, double r[][]) {
        super(listNode, matrixLink);
        this.C = c;
        this.numberNode = super.get_listNode().size();
        this.nodes = super.get_listNode();
        this.A = A;
        this.r = r;
    }

    @Override
    public ArrayList<Link> linkTree() {
        ArrayList<Link> reLink = new ArrayList<Link>();
        int n = nodes.size();
        boolean M[] = new boolean[n]; //cac nut da them vao cay
        double L[] = new double[n]; //gia cua duong dan ngan nhat
        //khoi tao
        for (int i = 0; i < n; i++)
            M[i] = false;
        M[C] = true;
        L[C] = 0;
        //bat dau thuat toan
        int count = 0;
        while (true) { //sau moi vong lap nay se tim duoc 1 nut de them vao cay
            int s = 0, e = 0; //diem dau va diem cuoi
            boolean first = true; //kiem tra co min hay chua
            double min = -1;
            //tim Lmin
            for (int j = 0; j < n; j++) {
                if (M[j]) { //xac dinh nut co trong tap M
                    for (int k = 0; k < n; k++) {
                        int nj = nodes.get(j).getName();
                        int nk = nodes.get(k).getName();
                        if (!M[k])//neu nut k chua them vao M
                            if (first) {//neu chua co min
                                min = L[j] * A + r[nj][nk];
                                s = j;
                                e = k;
                                first = false;
                            } else if ((L[j] * A + r[nj][nk]) < min) { //neu da co min
                                min = L[j] * A + r[nj][nk];
                                s = j;
                                e = k;
                            }
                    }
                }
            }
            //tim dc s, e
            M[e] = true;
            L[e] = min;
            int ns = nodes.get(s).getName();
            int ne = nodes.get(e).getName();
            reLink.add(new Link(ns, super.get_matrixLink()[ns][ne], ne));
            count++;
            if (count == n-1)
                break;
        }
        return  reLink;
    }
}
