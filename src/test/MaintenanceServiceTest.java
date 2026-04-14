package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.IssueReport;
import main.MaintenanceService;
import main.WorkTicket;

public class MaintenanceServiceTest {

    @Test
    void openTicketShouldStoreTicketAndIncreaseCount() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Harry",
                "Engineering Building",
                "ENG-201",
                "Heating",
                4,
                false,
                false,
                false
        );

        WorkTicket ticket = service.openTicket(report);

        assertNotNull(ticket);
        assertEquals(1, ticket.getId());
        assertEquals(1, service.getOpenTicketCount());
        assertSame(ticket, service.getTicket(0));
    }

    @Test
    void secondTicketShouldGetNextId() {
        MaintenanceService service = new MaintenanceService();

        IssueReport report1 = new IssueReport(
                "John",
                "Engineering Building",
                "ENG-201",
                "Heating",
                4,
                false,
                false,
                false
        );

        IssueReport report2 = new IssueReport(
                "Ben",
                "Science Building",
                "SCI-110",
                "Plumbing",
                5,
                true,
                false,
                false
        );

        WorkTicket first = service.openTicket(report1);
        WorkTicket second = service.openTicket(report2);

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
        assertEquals(2, service.getOpenTicketCount());
    }

    @Test
    void estimateEffortShouldCalculateExpectedHoursForElectricalTeachingFollowUpCase() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Chris",
                "Queens Building",
                "QB-301",
                "Electrical",
                8,
                true,
                false,
                true
        );

        int hours = service.estimateEffort(report);

        assertEquals(8, hours);
    }

    @Test
    void estimateEffortShouldCalculateExpectedHoursForSimplePlumbingCase() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Elizabeth",
                "Bancroft",
                "B-120",
                "Plumbing",
                3,
                false,
                false,
                false
        );

        int hours = service.estimateEffort(report);

        assertEquals(3, hours);
    }

    @Test
    void assignPriorityShouldReturnUrgentForSevereTeachingIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Asad",
                "Library",
                "L-20",
                "Heating",
                9,
                true,
                false,
                false
        );

        String priority = service.assignPriority(report);

        assertEquals("URGENT", priority);
    }

    @Test
    void assignPriorityShouldReturnUrgentForSevereElectricalIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Farah",
                "Physics",
                "PHY-09",
                "Electrical",
                8,
                false,
                false,
                false
        );

        String priority = service.assignPriority(report);

        assertEquals("URGENT", priority);
    }

    @Test
    void assignPriorityShouldReturnHighForTeachingIssueAtSeveritySeven() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Bob",
                "Arts One",
                "A1-14",
                "Heating",
                7,
                true,
                false,
                false
        );

        String priority = service.assignPriority(report);

        assertEquals("HIGH", priority);
    }

    @Test
    void assignPriorityShouldReturnHighForSpecialistModerateSeverityIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Jamie",
                "Graduate Centre",
                "GC-10",
                "Air",
                6,
                false,
                true,
                false
        );

        String priority = service.assignPriority(report);

        assertEquals("HIGH", priority);
    }

    @Test
    void assignPriorityShouldReturnMediumWhenFollowUpIsRequired() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Jade",
                "Informatics",
                "INF-22",
                "Heating",
                3,
                false,
                false,
                true
        );

        String priority = service.assignPriority(report);

        assertEquals("MEDIUM", priority);
    }

    @Test
    void assignPriorityShouldReturnLowForMinorRoutineIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Emma",
                "Mathematics",
                "MTH-10",
                "Heating",
                2,
                false,
                false,
                false
        );

        String priority = service.assignPriority(report);

        assertEquals("LOW", priority);
    }

    @Test
    void openTicketShouldPopulateExpectedHoursAndPriority() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Lina",
                "Queens Building",
                "QB-301",
                "Electrical",
                8,
                true,
                false,
                true
        );

        WorkTicket ticket = service.openTicket(report);

        assertEquals(8, ticket.getExpectedHours());
        assertEquals("URGENT", ticket.getPriority());
    }

    @Test
    void openTicketShouldReturnInfoInExpectedFormat() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Angela",
                "Library",
                "L-20",
                "Heating",
                9,
                true,
                false,
                false
        );

        WorkTicket ticket = service.openTicket(report);

        assertEquals("1 Library L-20 URGENT", ticket.info());
    }

    @Test
    void openTicketShouldThrowExceptionForNullReport() {
        MaintenanceService service = new MaintenanceService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(null)
        );

        assertEquals("Report cannot be null", exception.getMessage());
    }

    @Test
    void openTicketShouldThrowNameIsRequiredForBlankName() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "   ",
                "Engineering Building",
                "ENG-001",
                "Electrical",
                5,
                false,
                false,
                false
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(report)
        );

        assertEquals("Name is required", exception.getMessage());
    }

    @Test
    void openTicketShouldThrowBuildingIsRequiredForBlankBuilding() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Alice",
                " ",
                "ENG-001",
                "Electrical",
                5,
                false,
                false,
                false
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(report)
        );

        assertEquals("Building is required", exception.getMessage());
    }

    @Test
    void openTicketShouldThrowInvalidReportForMissingRoomCode() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Ron",
                "Engineering Building",
                "",
                "Electrical",
                5,
                false,
                false,
                false
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(report)
        );

        assertEquals("Invalid report", exception.getMessage());
    }

    @Test
    void openTicketShouldThrowInvalidReportForMissingIssueType() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Kevin",
                "Engineering Building",
                "ENG-001",
                "  ",
                5,
                false,
                false,
                false
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(report)
        );

        assertEquals("Invalid report", exception.getMessage());
    }

    @Test
    void openTicketShouldThrowInvalidReportForInvalidSeverity() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Bernard",
                "Engineering Building",
                "ENG-001",
                "Electrical",
                11,
                false,
                false,
                false
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.openTicket(report)
        );

        assertEquals("Invalid report", exception.getMessage());
    }

    @Test
    void openTicketShouldSetSpecialistNeededForElectricalSeveritySeven() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Julia",
                "Engineering Building",
                "ENG-001",
                "Electrical",
                7,
                false,
                false,
                false
        );

        WorkTicket ticket = service.openTicket(report);

        assertTrue(ticket.isSpecialistNeeded());
    }

    @Test
    void openTicketShouldCloseRoomForSevereTeachingIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Antony",
                "Arts One",
                "A1-14",
                "Heating",
                8,
                true,
                false,
                false
        );

        WorkTicket ticket = service.openTicket(report);

        assertTrue(ticket.isCloseRoom());
    }

    @Test
    void openTicketShouldNotCloseRoomForRoutineIssue() {
        MaintenanceService service = new MaintenanceService();
        IssueReport report = new IssueReport(
                "Fred",
                "Graduate Centre",
                "GC-22",
                "Heating",
                3,
                false,
                false,
                false
        );

        WorkTicket ticket = service.openTicket(report);

        assertFalse(ticket.isCloseRoom());
    }
}