package common;

import java.util.ArrayList;

import CustomViews.Line;
import CustomViews.Node;

/**
 * Created by Thiem on 10-May-16.
 */
public class MST_Tree extends Tree {
    ArrayList listLinkMST;
    int numberNode;
    public MST_Tree(ArrayList<Node> listNode, int[][] matrixLink) {
        super(listNode, matrixLink);
        this.numberNode = super.get_listNode().size();
    }

    @Override
    public ArrayList<Link> linkTree() {
        listLinkMST = new ArrayList<Link>();
        //sap xep cac lien ket tang dan theo bubble sort
        Link[] link = Link.bubbleSort(super.get_listLink());
        //mang kiem tra cac nut da duoc them vao cay chua
        boolean isContain[] = new boolean[numberNode];
        //mang kiem tra cac nut da ket noi voi nhau chua
        boolean isConnect[][] = new boolean[numberNode][numberNode];
        //khoi tao cac nut chua duoc them vao cay
        for (int i = 0; i < numberNode; i++)
            isContain[i] = false;
        //khoi tao cac nut chua ket noi voi nhau tru chinh no
        for (int i = 0; i < numberNode; i++)
            for (int j = 0; j < numberNode; j++) {
                if (i == j)
                    isConnect[i][j] = true;
                else
                    isConnect[i][j] = false;
            }
        //bat dau thuat toan
        int count = 0;
        for (int i = 0; i < link.length; i++) {
            Link t = link[i];
            //lay chi so cua nut dau va nut cuoi
            int s = t.getStart();
            int e = t.getEnd();
            //ca 2 nut chua dc them -> them lien ket va cap nhat lai cac mang kiem tra
            if ((!isContain[s]) && (!isContain[e])) {
                listLinkMST.add(t);
                count++;
                isContain[s] = true;
                isContain[e] = true;
                isConnect[s][e] = true;
                isConnect[e][s] = true;
            }
            //nut start da dc them -> them nut end, cap nhat lai cac mang
            else if (isContain[s] && !isContain[e]) {
                listLinkMST.add(t);
                count++;
                isContain[e] = true;
                isConnect[s][e] = true;
                isConnect[e][s] = true;
                for (int k = 0; k < numberNode; k++)
                    if (isConnect[s][k]) {
                        isConnect[e][k] = true;
                        isConnect[k][e] = true;
                    }
            }
            //nut end da duoc them -> them nut start, cap nhat lai cac mang
            else if (!isContain[s] && isContain[e]) {
                listLinkMST.add(t);
                count++;
                isContain[s] = true;
                isConnect[s][e] = true;
                isConnect[e][s] = true;
                for (int k = 0; k < numberNode; k++)
                    if (isConnect[k][e]) {
                        isConnect[s][k] = true;
                        isConnect[k][s] = true;
                    }
            }
            //ca hai nut da duoc them -> kiem tra 2 nut co tao vong k
            //neu da duoc ket noi voi nhau -> tao vong -> bo qua
            //neu chua ket noi voi nhau thi tiep tuc
            else if (!isConnect[s][e] && !isConnect[e][s]) {
                listLinkMST.add(t);
                count++;
                isConnect[s][e] = true;
                isConnect[e][s] = true;
                for (int k = 0; k < numberNode; k++)
                    if (isConnect[s][k])
                        for (int h = 0; h < numberNode; h++)
                            if (isConnect[e][h]) {
                                isConnect[h][k] = true;
                                isConnect[k][h] = true;
                            }
            }

            if (count == numberNode - 1)
                break;
        }
        return listLinkMST;
    }
}
