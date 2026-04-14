package main;

public class IssueReport {

    private String name;
    private String building;
    private String roomCode;
    private String issueType;
    private int sev;
    private boolean teachFlag;
    private boolean specialistFlag;
    private boolean requiresFollowUp;

    public IssueReport(String name, String building, String roomCode, String issueType,
                       int sev, boolean teachFlag, boolean specialist, boolean requiresFollowUp) {
        this.name = name;
        this.building = building;
        this.roomCode = roomCode;
        this.issueType = issueType;
        this.sev = sev;
        this.teachFlag = teachFlag;
        this.specialistFlag = specialist;
        this.requiresFollowUp = requiresFollowUp;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getIssueType() {
        return issueType;
    }

    public int getSev() {
        return sev;
    }

    public boolean isTeachFlag() {
        return teachFlag;
    }

    public boolean isSpecialist() {
        return specialistFlag;
    }

    public boolean isRequiresFollowUp() {
        return requiresFollowUp;
    }
}