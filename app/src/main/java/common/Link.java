package common;

import java.util.ArrayList;

/**
 * Created by Thiem on 07-Apr-16.
 */
public class Link {
    private int start;
    private int end;
    private double d;

    public Link(int start, double d, int end) {
        this.start = start;
        this.d = d;
        this.end = end;
    }

    public static Link[] bubbleSort(ArrayList<Link> l) {
        int s = l.size();
        Link[] ls = new Link[s];
        for (int i = 0; i < s; i++) {
            ls[i] = l.get(i);
        }

        for (int i = 0; i < s - 1; i++) {
            for (int j = s - 1; j > i; j--)
                if (ls[j].getD() < ls[j - 1].getD() && ls[j].getD() != 0) {
                    //swap
                    Link ll = ls[j];
                    ls[j] = ls[j - 1];
                    ls[j - 1] = ll;
                }
        }
        return ls;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public double getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
