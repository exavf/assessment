package main;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private List<WorkTicket> vals = new ArrayList<>();

    public void add(WorkTicket tkt) {
        vals.add(tkt);
    }

    public WorkTicket item(int idx) {
        WorkTicket x = vals.get(idx);
        return x;
    }

    public int count() {
        return vals.size();
    }
}