package common;

import java.util.ArrayList;

import CustomViews.Node;

/**
 * Created by Thiem on 10-May-16.
 */
public class CMST_Tree extends Tree {
    ArrayList<Node> aN;
    ArrayList<Link> aL;
    int C;
    int Wmax;
    public CMST_Tree(ArrayList<Node> listNode, int[][] matrixLink, int c, int _Wmax) {
        super(listNode, matrixLink);
        this.aN = super.get_listNode();
        this.aL = super.get_listLink();
        this.C = c;
        this.Wmax = _Wmax;
    }

    @Override
    public ArrayList<Link> linkTree() {
        ArrayList<Link> reLink = new ArrayList<Link>();
        int n = aN.size();
        //sap xep cac lien ket tang dan theo bubble sort
        Link[] link = Link.bubbleSort(aL);
        //mang kiem tra cac nut da duoc them vao cay chua
        boolean isContain[] = new boolean[n];
        //mang kiem tra cac nut da ket noi voi nhau chua
        boolean isConnect[][] = new boolean[n][n];
        //mang kiem tra cac nut da ket noi voi nhau chua (tinh Wmax)
        boolean isConnectW[][] = new boolean[n][n];
        //mang luu tong W cua cay ma nut dang nam tren do
        int w[] = new int[n];
        //khoi tao cac nut chua duoc them vao cay
        for (int i = 0; i < n; i++)
            isContain[i] = false;
        isContain[C] = true;
        //khoi tao cac nut chua ket noi voi nhau tru chinh no
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (i == j)
                    isConnect[i][j] = true;
                else
                    isConnect[i][j] = false;
            }
        //khoi tao cac nut chua ket noi voi nhau tru chinh no (cho Wmax)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (i == j)
                    isConnectW[i][j] = true;
                else
                    isConnectW[i][j] = false;
            }
        //khoi tao do dai cay
        for (int i = 0; i < n; i++)
            w[i] = aN.get(i).getW();
        w[C] = 0;
        //bat dau thuat toan
        int count = 0;
        for (int i = 0; i < link.length; i++) {
            Link t = link[i];
            //lay chi so cua nut dau va nut cuoi
            int s = t.getStart();
            int e = t.getEnd();
            //ca 2 nut chua dc them -> them lien ket va cap nhat lai cac mang kiem tra
            if ((!isContain[s]) && (!isContain[e])) {
                if (w[s] + w[e] <= 5 * Wmax) { //khong vi pham Wmax
                    reLink.add(new Link(aN.get(t.getStart()).getName(),t.getD(),aN.get(t.getEnd()).getName()));
                    count++;
                    //update
                    isContain[s] = true;
                    isContain[e] = true;
                    //update
                    isConnect[s][e] = true;
                    isConnect[e][s] = true;
                    //update W
                    if (s != C && e != C) {
                        isConnectW[s][e] = true;
                        isConnectW[e][s] = true;
                        w[s] = w[s] + w[e];
                        w[e] = w[s];
                    }
                }
            }
            //nut start da dc them -> them nut end, cap nhat lai cac mang
            else if (isContain[s] && (!isContain[e])) {
                if (w[s] + w[e] <= 5 * Wmax) { //khong vi pham Wmax
                    reLink.add(new Link(aN.get(t.getStart()).getName(),t.getD(),aN.get(t.getEnd()).getName()));
                    count++;
                    //update
                    isContain[e] = true;
                    isConnect[s][e] = true;
                    isConnect[e][s] = true;
                    //update cho cac nut ket noi voi s
                    for (int k = 0; k < n; k++)
                        if (isConnect[s][k] || isConnect[k][s]) {
                            isConnect[e][k] = true;
                            isConnect[k][e] = true;
                        }
                    if (s != C) {
                        //update w
                        w[s] += w[e];
                        w[e] = w[s];
                        //update w cho cac nut lien ket W voi s
                        for (int k = 0; k < n; k++)
                            if (isConnectW[s][k])
                                w[k] = w[s];
                        //update W cho cac nut ket noi voi s
                        for (int k = 0; k < n; k++) {
                            if (k != C)
                                if (isConnectW[s][k] || isConnectW[k][s]) {
                                    isConnectW[e][k] = true;
                                    isConnectW[k][e] = true;
                                }
                        }
                    }
                }
            }
            //nut end da duoc them -> them nut start, cap nhat lai cac mang
            else if ((!isContain[s]) && isContain[e]) {
                if (w[s] + w[e] <= 5 * Wmax) {
                    reLink.add(new Link(aN.get(t.getStart()).getName(),t.getD(),aN.get(t.getEnd()).getName()));
                    count++;
                    //update
                    isContain[s] = true;
                    isConnect[s][e] = true;
                    isConnect[e][s] = true;
                    //update cho cac nut ket noi voi e
                    for (int k = 0; k < n; k++)
                        if (isConnect[e][k] || isConnect[k][e]) {
                            isConnect[s][k] = true;
                            isConnect[k][s] = true;
                        }
                    if (e != C) {
                        //update w
                        w[s] += w[e];
                        w[e] = w[s];
                        //update w cho cac nut lien ket W voi e
                        for (int k = 0; k < n; k++)
                            if (isConnectW[e][k])
                                w[k] = w[e];
                        //update W cho cac nut ket noi voi e
                        for (int k = 0; k < n; k++) {
                            if (k != C)
                                if (isConnectW[e][k] || isConnectW[k][e]) {
                                    isConnectW[s][k] = true;
                                    isConnectW[k][s] = true;
                                }
                        }
                    }
                }
            }
            //ca hai nut da duoc them -> kiem tra 2 nut co tao vong k
            //neu da duoc ket noi voi nhau -> tao vong -> bo qua
            //neu chua duoc ket noi voi nhau -> them vao cay, cap nhat lai cac mang
            else if (!isConnect[s][e] && !isConnect[e][s]) {
                if (w[s] + w[e] <= 5 * Wmax) {
                    reLink.add(new Link(aN.get(t.getStart()).getName(),t.getD(),aN.get(t.getEnd()).getName()));
                    count++;
                    isConnect[s][e] = true;
                    isConnect[e][s] = true;
                    for (int k = 0; k < n; k++) {
                        if (isConnect[s][k])
                            for (int h = 0; h < n; h++)
                                if (isConnect[h][e]) {
                                    isConnect[k][h] = true;
                                    isConnect[h][k] = true;
                                }
                    }
                    if (s != C && e != C) {
                        w[s] = w[s] + w[e];
                        w[e] = w[s];
                        //update W cho cac nut ket noi voi s
                        for (int k = 0; k < n; k++) {
                            if (k != C && isConnectW[s][k])
                                for (int h = 0; h < n; h++)
                                    if (h != C && isConnectW[h][e]) {
                                        isConnectW[k][h] = true;
                                        isConnectW[h][k] = true;
                                    }
                        }
                    }
                }
            }
            if (count == n - 1)
                break;
        }
        return reLink;
    }
}
