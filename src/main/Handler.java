package main;

public class Handler {

	public boolean validate(IssueReport report) {
		if (report == null) {
			return false;
		}

		String[] vals = { report.getName(), report.getBuilding(), report.getRoomCode(), report.getIssueType() };

		for (int i = 0; i < vals.length; i++) {
			if (vals[i] == null || vals[i].trim().isEmpty()) {
				return false;
			}
		}

		if (report.getSev() < 1 || report.getSev() > 10) {
			return false;
		}

		return true;
	}

	public String update(IssueReport report) {
		String res = priorityValue(report.getIssueType(), report.getSev(), report.isTeachFlag(), report.isSpecialist(),
				report.isRequiresFollowUp());
		return res;
	}

	public int calculateDetails(IssueReport report) {
		int x = 2;

		// severe problems usually take longer
		if (report.getSev() >= 7) {
			x = x + 2;
		}

		// issues affecting teaching spaces need more coordination
		if (report.isTeachFlag()) {
			x = x + 1;
		}

		if (report.isRequiresFollowUp()) {
			x = x + 1;
		}

		x = x + typeHours(report.getIssueType());

		return x;
	}

	public WorkTicket record(int id, IssueReport report) {
		if (report == null) {
			throw new IllegalArgumentException("Report cannot be null");
		}

		String[] vals = { report.getName(), report.getBuilding(), report.getRoomCode() };

		int idx = 0;
		while (idx < vals.length) {
			if (vals[idx] == null || vals[idx].trim().isEmpty()) {
				throw new IllegalArgumentException("Missing details");
			}
			idx++;
		}

		if (report.getIssueType() == null || report.getIssueType().trim().isEmpty()) {
			throw new IllegalArgumentException("Issue type is required");
		}

		boolean specialist = false;

		if ("Electrical".equals(report.getIssueType()) || "Plumbing".equals(report.getIssueType())) {
			if ("Electrical".equals(report.getIssueType()) && report.getSev() > 6) {
				specialist = true;
			} else if (report.isTeachFlag()) {
				if ("Plumbing".equals(report.getIssueType())) {
					specialist = true;
				}
			}
		}

		int hrs = calculateDetails(report);
		String priority = update(report);
		boolean closeRoom = shouldCloseRoom(report.getIssueType(), report.getSev(), report.isTeachFlag(), specialist);

		WorkTicket t = new WorkTicket(id, report, report.getBuilding(), report.getRoomCode(), report.getIssueType(),
				hrs, priority, specialist, closeRoom);

		return t;
	}

	private String priorityValue(String issueType, int sev, boolean teachFlag, boolean specialist,
			boolean requiresFollowUp) {

		String res = "LOW";

		// severe issues affecting teaching should be handled immediately
		if (sev >= 9 && teachFlag) {
			res = "URGENT";
		}

		// severe electrical faults should also be treated urgently
		if ("Electrical".equals(issueType) && sev >= 8) {
			res = "URGENT";
		}

		// important teaching issues should not wait
		if (res.equals("LOW") && sev >= 7 && teachFlag) {
			res = "HIGH";
		}

		// specialist work at moderate or high severity should be prioritised
		if (res.equals("LOW") && specialist && sev >= 6) {
			res = "HIGH";
		}

		// follow-up work and medium severity cases should not be left as low
		if (res.equals("LOW") && (requiresFollowUp || sev >= 5)) {
			res = "MEDIUM";
		}

		return res;
	}

	private boolean shouldCloseRoom(String issueType, int sev, boolean teachFlag, boolean specialist) {

		// severe issues in teaching rooms should close the room
		if (sev > 7 && teachFlag) {
			return true;
		}

		// specialist electrical issues should close the room
		if (specialist && "Electrical".equals(issueType)) {
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
}