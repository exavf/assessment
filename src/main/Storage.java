package main;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private List<WorkTicket> workTicketList = new ArrayList<>();

    public void add(WorkTicket tkt) {
        workTicketList.add(tkt);
    }

    public WorkTicket item(int idx) {
        WorkTicket ticket = workTicketList.get(idx);
        return ticket;
    }

    public int count() {
        return workTicketList.size();
    }
}