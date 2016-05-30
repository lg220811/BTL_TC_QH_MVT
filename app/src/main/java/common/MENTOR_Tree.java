package common;

import com.example.thiem.tcqhmvt.DisplayActivity;

import java.util.ArrayList;

import CustomViews.Line;
import CustomViews.Node;

/**
 * Created by Thiem on 10-May-16.
 */
public class MENTOR_Tree extends Tree {
    int numberNode;
    int[][] maxtrixLink;
    ArrayList<Node> listNode;
    double We, Rr, Pc, A;
    int D, Wmax;
    public MENTOR_Tree(ArrayList<Node> listNode, int[][] matrixLink, double _We, double _Rr, double _Pc, int _D, double _A, int _Wmax) {
        super(listNode, matrixLink);
        this.numberNode = super.get_listNode().size();
        this.maxtrixLink = super.get_matrixLink();
        this.listNode = super.get_listNode();
        this.We = _We;
        this.Rr = _Rr;
        this.Pc = _Pc;
        this.D = _D;
        this.A = _A;
        this.Wmax = _Wmax;
    }

    @Override
    public ArrayList<Link> linkTree() {
        //tim tam C
        int Mi = 0;
        int C = 0;
        int m;
        for (int i = 0; i < numberNode; i++) {
            m = 0;
            for (int j = 0; j < numberNode; j++)
                m += maxtrixLink[i][j] * listNode.get(j).getW();
            if (i == 0)
                Mi = m;
            else if (Mi > m) {
                Mi = m;
                C = i;
            }
        }
        //tim cac nut backbone
        boolean[] isContain = new boolean[numberNode];
        isContain[C] = true;
        ArrayList<Node> bbList = new ArrayList<Node>();
        for (int i = 0; i < numberNode; i++) {
            if (i != C && listNode.get(i).getW() >= We && !isContain[i]) {
                listNode.get(i).setBackBone(true);
                bbList.add(listNode.get(i));
                isContain[i] = true;
            }
        }
        //tim cac nut truy nhap
        ArrayList<Node> acList[] = new ArrayList[bbList.size()];
        for (int i = 0; i < bbList.size(); i++) {
            acList[i] = new ArrayList<Node>();
            int bbName = bbList.get(i).getName();
            for (int j = 0; j < numberNode; j++)
                if (j != C && !isContain[j] && maxtrixLink[bbName][j] <= Rr) {
                    acList[i].add(listNode.get(j));
                    isContain[j] = true;
                }
        }
        //cho cac nut chua duoc ket noi vao mang
        ArrayList<Link> F = new ArrayList<Link>();
        //tinh Fi
        for (int i = 0; i < numberNode; i++)
            if (!isContain[i]) {
                double f = Pc * maxtrixLink[i][C] / (D * 1.0) + (1 - Pc) * listNode.get(i).getW() / (We * 1.0);
                Link s = new Link(i, f, i);
                F.add(s);
            }
        ArrayList<ArrayList<Node>> newAcList = new ArrayList<ArrayList<Node>>();
        //them cac nut vao mang
        Link sort[] = Link.bubbleSort(F);
        int i = F.size() - 1;
        while (i >= 0) {
            int ni = sort[i].getStart();
            if (ni != C && !isContain[ni]) {
                isContain[ni] = true;
                ArrayList<Node> x = new ArrayList<Node>();
                x.add(listNode.get(ni));
                listNode.get(ni).setBackBone(true);
                for (int j = 0; j < numberNode; j++) {
                    if (j != C && !isContain[j] && maxtrixLink[ni][j] <= Rr) {
                        x.add(listNode.get(j));
                        isContain[j] = true;
                    }
                }
                newAcList.add(x);
            }
            i--;
        }
        //tao cac ket noi backbone voi access
        int mm = newAcList.size();
        ArrayList<Link> linkBackbone[] = new ArrayList[bbList.size() + mm];//day la final list
        for (i = 0; i < bbList.size() + mm; i++) {
            if (i < bbList.size()) {
                acList[i].add(0, bbList.get(i));
                CMST_Tree cmst_tree = new CMST_Tree(acList[i], DisplayActivity.createLinks(acList[i]),C,Wmax);
                linkBackbone[i] = createCMSTLink(0, acList[i], createLinks(acList[i]));
            } else
                linkBackbone[i] = createCMSTLink(0, newAcList.get(i - bbList.size()), createLinks(newAcList.get(i - bbList.size())));
        }
    }
}
