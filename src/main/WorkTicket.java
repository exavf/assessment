package main;

public class WorkTicket {

    private int id;
    private IssueReport report;
    private int expectedHours;
    private String priority;
    private boolean specialistNeeded;
    private boolean closeRoom;

    public WorkTicket(int id, IssueReport report, String building, String roomCode,
                      String type, int hrs, String priority,
                      boolean specialistNeeded, boolean closeRoom) {

        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null");
        }

        if (building == null || roomCode == null || type == null) {
            throw new IllegalArgumentException("Ticket data is incomplete");
        }

        this.id = id;
        this.report = report;
        this.expectedHours = hrs;
        this.priority = priority;
        this.specialistNeeded = specialistNeeded;
        this.closeRoom = closeRoom;
    }

    public int getId() {
        return id;
    }

    public IssueReport getReport() {
        return report;
    }

    public int getExpectedHours() {
        return expectedHours;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isSpecialistNeeded() {
        return specialistNeeded;
    }

    public boolean isCloseRoom() {
        return closeRoom;
    }

    public String info() {
        String p = "";
        p = p + id;
        p = p + " ";
        p = p + report.getBuilding();
        p = p + " ";
        p = p + report.getRoomCode();
        p = p + " ";
        p = p + priority;
        return p;
    }
}