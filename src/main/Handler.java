package main;

public class Handler {

    // Validates that the report is valid
	public boolean validate(IssueReport report) {
        // Default checking
		if (report == null) {
			return false;
		}

        // Extracted the code here into multiple methods to separate out the logic - easily understandable I think

        // Renamed vals to a more suitable variable name
		String[] reportInformation = { report.getName(), report.getBuilding(), report.getRoomCode(), report.getIssueType() };

        // Checks that every value within reportInformation is either not empty or null
        if (!validateReportInformation(reportInformation)){return false;}

        // Validates that severity is ONLY between 1-10, otherwise return false
        if (!validateSeverity(report)){return false;}

        return true;
	}

	public String update(IssueReport report) {
        // I think need to just pass report in, instead of passing everything within report
        // Also updated variable name
		String updatedPriorityValue = priorityValue(report);
		return updatedPriorityValue;
	}

	public int calculateDetails(IssueReport report) {

        // Replaced magic number with final constant and replaced variable name
        final int DEFAULT_ESTIMATED_EFFORT_HOURS = 2;
        int estimatedEffortHours = DEFAULT_ESTIMATED_EFFORT_HOURS;

		// severe problems usually take longer
		if (report.getSev() >= 7) {
            estimatedEffortHours = estimatedEffortHours + 2;
		}

		// issues affecting teaching spaces need more coordination
		if (report.isTeachFlag()) {
            estimatedEffortHours = estimatedEffortHours + 1;
		}

		if (report.isRequiresFollowUp()) {
            estimatedEffortHours = estimatedEffortHours + 1;
		}

        estimatedEffortHours = estimatedEffortHours + typeHours(report.getIssueType());

		return estimatedEffortHours;
	}

    public WorkTicket record(int id, IssueReport report) {
		if (report == null) {
			throw new IllegalArgumentException("Report cannot be null");
		}

        // Improved variable names here as well

		String[] reportRecords = { report.getName(), report.getBuilding(), report.getRoomCode() };

        // Changed while loop into a for-each loop
        for (String reportRecord : reportRecords) {
            if (reportRecord == null || reportRecord.trim().isEmpty()) {
                throw new IllegalArgumentException("Missing details");
            }
        }

		if (report.getIssueType() == null || report.getIssueType().trim().isEmpty()) {
			throw new IllegalArgumentException("Issue type is required");
		}

        // Assigning value of specialist to a new method to separate the logic
        boolean specialist = isSpecialistNeeded(report.getIssueType(), report.getSev(), report.isTeachFlag());

		int expectedHours = calculateDetails(report);
		String priority = update(report);
		boolean closeRoom = shouldCloseRoom(report, specialist); // Passed on report instaed of individual attributes

        // Report is passed again and again - removed redundant parameters
		WorkTicket ticket = new WorkTicket(id, report, expectedHours, priority, specialist, closeRoom);

		return ticket;
	}

	private String priorityValue(IssueReport report) {

		String priority = "LOW";

        // Passed on IssueReport instance and utilising get methods instead

		// severe issues affecting teaching should be handled immediately
		if (report.getSev() >= 9 && report.isTeachFlag()) {
			priority = "URGENT";
		}

		// severe electrical faults should also be treated urgently
        if (report.getIssueType().equals("Electrical") && report.getSev() >= 8) {
			priority = "URGENT";
		}

		// important teaching issues should not wait
		if (priority.equals("LOW") && report.getSev() >= 7 && report.isTeachFlag()) {
			priority = "HIGH";
		}

		// specialist work at moderate or high severity should be prioritised
		if (priority.equals("LOW") && report.isSpecialist() && report.getSev() >= 6) {
			priority = "HIGH";
		}

		// follow-up work and medium severity cases should not be left as low
		if (priority.equals("LOW") && (report.isRequiresFollowUp() || report.getSev() >= 5)) {
			priority = "MEDIUM";
		}

		return priority;
	}

	private boolean shouldCloseRoom(IssueReport record, boolean specialist) {

		// severe issues in teaching rooms should close the room
		if (record.getSev() > 7 && record.isTeachFlag()) {
			return true;
		}

		// specialist electrical issues should close the room
		if (specialist && "Electrical".equals(record.getIssueType())) {
			return true;
		}

		return false;
	}

	private boolean needsSpecialist(IssueReport report) {
		return ("Electrical".equals(report.getIssueType()) && report.getSev() >= 7)
				|| ("Plumbing".equals(report.getIssueType()) && report.isTeachFlag());
	}

	private int typeHours(String issueType) {
		if ("Electrical".equals(issueType)) {
			return 2;
		}

		if ("Plumbing".equals(issueType)) {
			return 1;
		}

		return 0;
	}

	private boolean requiresEscalation(IssueReport report) {
		if (report != null)
			return isCriticalCombination(report);
		else
			return false;
	}

	private boolean isCriticalCombination(IssueReport report) {
		return report.getSev() > 8 && report.isTeachFlag() && report.isRequiresFollowUp();
	}

    private boolean validateReportInformation(String[] reportInformation) {
        for (int i = 0; i < reportInformation.length; i++) {
            if (reportInformation[i] == null || reportInformation[i].trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean validateSeverity(IssueReport report) {
        if (report.getSev() < 1 || report.getSev() > 10) {
            return false;
        }
        return true;
    }

    private boolean isSpecialistNeeded (String issueType, int severity, boolean teachFlag) {
        if (issueType.equals("Electrical") || issueType.equals("Plumbing")) {
            if ("Electrical".equals(issueType) && severity > 6) { return true; }
            else if (teachFlag) {
                if (issueType.equals("Plumbing")) { return true; }
            }
        }

        return false;
    }
}