package com.example.thiem.tcqhmvt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Random;

import CustomViews.Line;
import CustomViews.Node;
import common.Link;

/**
 * Created by Thiem on 02-Apr-16.
 */
public class DisplayActivity extends AppCompatActivity {
    //hinh anh
    RelativeLayout mother;
    //so lieu
    private int numberNode, Wmin, Wmax, We;
    private double Pc, A;
    private ArrayList<Node> listNode = new ArrayList<Node>();
    private ArrayList<Link> listLink, listLinkMST, listLinkSPT, listLinkCMST, listLinkMENTOR;
    private static int[][] maxtrixLink;
    private int r[][], Rn, Rmin, D, Rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mother = (RelativeLayout) findViewById(R.id.mother);

        Intent intent = getIntent();
        setupParam(intent);
        createNode();
        createLink();
    }

    private void createLink() {
        this.listLink = new ArrayList<Link>();
        this.maxtrixLink = new int[numberNode][numberNode];
        this.r = new int[numberNode][numberNode];
        this.D = 0;
        this.Rmin = 9999;
        Random rd = new Random();
        for (int i = 0; i < numberNode; i++)
            for (int j = 0; j < numberNode; j++) {
                int d = getDistance(listNode.get(i), listNode.get(j));
                if (i != j)
                    listLink.add(new Link(i, d, j));
                maxtrixLink[i][j] = d;
                if (d > D)
                    D = d;
                if (d < Rmin && d != 0)
                    Rmin = d;
                if (i == j)
                    r[i][j] = 0;
                else
                    r[i][j] = rd.nextInt(Rn-1)+1;
            }
        Wmin = 0;
        Wmax = 0;
        for (int i = 0; i < numberNode; i++) {
            int w = 0;
            for (int j = 0; j < numberNode; j++)
                w += r[i][j] + r[j][i];
            if (i == 0) {
                Wmin = w;
                Wmax = w;
            } else {
                if (Wmin > w && w != 0)
                    Wmin = w;
                if (Wmax < w)
                    Wmax = w;
            }
            listNode.get(i).setW(w);
        }
    }

    private void createNode() {
        Random rd = new Random();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        for (int i = 0; i < numberNode; i++) {
            int x = rd.nextInt(width-30)+15;
            int y = rd.nextInt(height*8/10)+30;
            Node n = new Node(getApplicationContext(), x, y, false, i);
            mother.addView(n);
            listNode.add(n);
        }
        mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Node n : listNode)
                    n.setShow(!n.isShow());
            }
        });
    }

    private void setupParam(Intent intent) {
        this.numberNode = intent.getIntExtra("N", 0);
        this.Pc = intent.getDoubleExtra("Pc", 0);
        this.A = intent.getDoubleExtra("A", 0);
        this.Rn = intent.getIntExtra("R", 10);
    }

    private int getDistance(Node a, Node b) {
        return (int) Math.sqrt(Math.pow(a.getXm() - b.getXm(), 2) + Math.pow((a.getYm() - b.getYm()), 2));
    }

    //su dung thuat toan kruskal (done)
    private void createMSTLink() {
        listLinkMST = new ArrayList<Link>();
        //sap xep cac lien ket tang dan theo bubble sort
        Link[] link = Link.bubbleSort(listLink);
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
        for (Link t : listLinkMST) {
            mother.addView(new Line(getApplicationContext(),
                    listNode.get(t.getStart()),
                    listNode.get(t.getEnd()),
                    false));
        }
    }

    //su dung thuat toan dijkstras (done)
    private ArrayList<Link> createSPTLink(int C, ArrayList<Node> nodes, double A, boolean root) {
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
            reLink.add(new Link(ns, maxtrixLink[ns][ne], ne));
            count++;
            if (count == n-1)
                break;
        }
        for (Link t : reLink) {
            int s = t.getStart(), e = t.getEnd();
            mother.addView(new Line(getApplicationContext(),
                    listNode.get(s),
                    listNode.get(e),
                    root));
        }
        return reLink;
    }

    //su dung thuat toan Kruskal (done)

    /**
     * @param C  vị trí tâm trong mảng aN
     * @param aN mảng các node kết nối với backbone, bao gồm cả backbone
     * @param aL mảng các liên kết của các node trong backbone
     * @return mảng các liên kết đã được duyệt
     */
    private ArrayList<Link> createCMSTLink(int C, ArrayList<Node> aN, ArrayList<Link> aL) {
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
            if (count == numberNode - 1)
                break;
        }
        return reLink;
    }

    //mentor
    private void createMENTORLink() {
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
            if (i!=C&&listNode.get(i).getW() >= We&&!isContain[i]) {
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
                if (j!=C&&!isContain[j] && maxtrixLink[bbName][j] <= Rr) {
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
            if (ni!=C&&!isContain[ni]) {
                isContain[ni] = true;
                ArrayList<Node> x = new ArrayList<Node>();
                x.add(listNode.get(ni));
                listNode.get(ni).setBackBone(true);
                for (int j = 0; j < numberNode; j++) {
                    if (j!=C&&!isContain[j] && maxtrixLink[ni][j] <= Rr) {
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
                linkBackbone[i] = createCMSTLink(0, acList[i], createLinks(acList[i]));
            } else
                linkBackbone[i] = createCMSTLink(0, newAcList.get(i - bbList.size()), createLinks(newAcList.get(i - bbList.size())));
        }
        //ve len tren man hinh
        for (i = 0; i < linkBackbone.length; i++)
            for (int j = 0; j < linkBackbone[i].size(); j++)
                mother.addView(new Line(getApplicationContext(),
                        listNode.get(linkBackbone[i].get(j).getStart()),
                        listNode.get(linkBackbone[i].get(j).getEnd()),
                        false));
        //tao cac ket noi tu nut goc den backbone
        for (int k=0;k<newAcList.size();k++)
            bbList.add(newAcList.get(k).get(0));
        bbList.add(0, listNode.get(C));
        bbList.get(0).setRoot(true);
        ArrayList<Link> linkRoot = createSPTLink(0, bbList, A,true);
        for (i = 0; i < linkRoot.size(); i++)
            mother.addView(new Line(getApplicationContext(),
                    listNode.get(linkRoot.get(i).getStart()),
                    listNode.get(linkRoot.get(i).getEnd()),
                    true));
    }

    public static ArrayList<Link> createLinks(ArrayList<Node> nodes) {
        ArrayList<Link> reLink = new ArrayList<Link>();

        for (int i = 0; i < nodes.size(); i++)
            for (int j = 0; j < nodes.size(); j++) {
                int ni = nodes.get(i).getName();
                int nj = nodes.get(j).getName();
                if (i != j)
                    reLink.add(new Link(i, maxtrixLink[ni][nj], j));
            }
        return reLink;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mst:
                mother.removeAllViews();
                for (Node n : listNode) {
                    n.setBackBone(false);
                    n.setRoot(false);
                    mother.addView(n);
                }
                createMSTLink();
                break;
            case R.id.spt:
                mother.removeAllViews();
                for (Node n : listNode) {
                    n.setBackBone(false);
                    n.setRoot(false);
                }
                listNode.get(0).setRoot(true);
                for (Node n : listNode)
                    mother.addView(n);
                createSPTLink(0, listNode, 1,false);
                break;
            case R.id.mentor:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Chọn tham số");
                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.alert, null);
                builder.setView(row);
                SeekBar skbW = (SeekBar) row.findViewById(R.id.seekBarW);
                final EditText edtW = (EditText) row.findViewById(R.id.editTextW);
                SeekBar skbRn = (SeekBar) row.findViewById(R.id.seekBarRn);
                final EditText edtRn = (EditText) row.findViewById(R.id.editTextRn);
                edtW.setText("" + Wmin);
                skbW.setMax(Wmax - Wmin);
                skbW.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        edtW.setText("" + (progress + Wmin));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                edtRn.setText("" + Rmin);
                skbRn.setMax(D - Rmin);
                skbRn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        edtRn.setText("" + (progress + Rmin));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Node n : listNode) {
                            n.setBackBone(false);
                            n.setRoot(false);
                        }
                        We = (int) OptionActivity.getNumber(edtW, "W", getApplication());
                        Rr = (int) OptionActivity.getNumber(edtRn, "R", getApplicationContext());
                        mother.removeAllViews();
                        for (Node n : listNode)
                            mother.addView(n);
                        createMENTORLink();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
