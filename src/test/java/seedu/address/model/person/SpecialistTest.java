package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

public class SpecialistTest {
    @Test
    public void equals() {
        assertTrue(true);
        // TODO: Add tests for specialists equals method based on fields
    }
    @Test
    public void toStringMethod() {
        String expected = Specialist.class.getCanonicalName() + "{name=" + BOB.getName() + ", phone=" + BOB.getPhone()
                + ", email=" + BOB.getEmail() + ", address=" + BOB.getAddress() + ", tags=" + BOB.getTags() + "}";
        assertEquals(expected, BOB.toString());
    }
}
