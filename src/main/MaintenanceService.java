package main;

public class MaintenanceService {

	private Handler handler;
	private Storage storage;
	private int nextId;

	public MaintenanceService() {
		this.handler = new Handler();
		this.storage = new Storage();
		this.nextId = 1;
	}

	public WorkTicket openTicket(IssueReport report) {
		if (report == null) {
			throw new IllegalArgumentException("Report cannot be null");
		}

		if (report.getName() == null || report.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Name is required");
		}

		if (report.getBuilding() == null || report.getBuilding().trim().isEmpty()) {
			throw new IllegalArgumentException("Building is required");
		}

		if (!handler.validate(report)) {
			throw new IllegalArgumentException("Invalid report");
		}

		int x = handler.calculateDetails(report);
		String res = handler.update(report);
		WorkTicket t = handler.record(nextId, report);

		if (x > 0 && res != null && !res.isEmpty()) {
			storage.add(t);
			nextId++;
		}

		return t;
	}

	public int estimateEffort(IssueReport report) {
		return handler.calculateDetails(report);
	}

	public String assignPriority(IssueReport report) {
		return handler.update(report);
	}

	public int getOpenTicketCount() {
		return storage.count();
	}

	public WorkTicket getTicket(int index) {
		return storage.item(index);
	}
}