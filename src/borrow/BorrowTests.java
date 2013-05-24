package borrow;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BorrowTests {

    protected Person finn, jake, marcelline, iceKing;
    protected Thing sword, viola, guitar;

    @Before
    public void setUp() {
        finn = new Person("Finn the Human");
        jake = new Person("Jake the Dog");
        marcelline = new Person("Marcelline the Vampire Queen");
        iceKing = new Person("Simon Petrikov");

        sword = finn.acquireSomething("épée rouge");
        viola = jake.acquireSomething("violon alto");
        guitar = marcelline.acquireSomething("hache à 4 cordes");
    }

    @Test
    public void testOwners() {
        assertEquals(finn, sword.getOwner());
        assertEquals(jake, viola.getOwner());
        assertEquals(marcelline, guitar.getOwner());

    }

    @Test
    public void testInitialOwnerships() {
        assertTrue(finn.isOwner(sword));
        assertTrue(jake.isOwner(viola));
        assertTrue(marcelline.isOwner(guitar));

        assertFalse(finn.isOwner(viola));
        assertFalse(finn.isOwner(guitar));

        assertFalse(jake.isOwner(sword));
        assertFalse(jake.isOwner(guitar));

        assertFalse(marcelline.isOwner(sword));
        assertFalse(marcelline.isOwner(viola));
    }

    @Test
    public void testInitialCustodies() {
        assertTrue(finn.hasCustody(sword));
        assertTrue(jake.hasCustody(viola));
        assertTrue(marcelline.hasCustody(guitar));

        assertFalse(finn.hasCustody(viola));
        assertFalse(finn.hasCustody(guitar));

        assertFalse(jake.hasCustody(sword));
        assertFalse(jake.hasCustody(guitar));

        assertFalse(marcelline.hasCustody(sword));
        assertFalse(marcelline.hasCustody(viola));
    }

    @Test
    public void testCannotLendTwice() {
        assertTrue(jake.lend(finn, viola));
        assertFalse(jake.lend(marcelline, viola));
    }

    @Test
    public void testBorrowAndGiveBack() {
        jake.lend(marcelline, viola);

        assertTrue(marcelline.hasCustody(viola));
        assertFalse(jake.hasCustody(viola));

        assertFalse(marcelline.isOwner(viola));
        assertTrue(jake.isOwner(viola));

        marcelline.giveBack(viola);

        assertFalse(marcelline.hasCustody(viola));
        assertTrue(jake.hasCustody(viola));

        assertFalse(marcelline.isOwner(viola));
        assertTrue(jake.isOwner(viola));
    }

    @Test
    public void testSubBorrow() {
        marcelline.lend(finn, guitar);
        finn.lend(jake, guitar);

        assertFalse(marcelline.hasCustody(guitar));
        assertFalse(finn.hasCustody(guitar));
        assertTrue(jake.hasCustody(guitar));

        jake.giveBack(guitar); // to Finn

        assertFalse(marcelline.hasCustody(guitar));
        assertTrue(finn.hasCustody(guitar));
        assertFalse(jake.hasCustody(guitar));
    }

    @Test
    public void testReturnShortcut() {
        jake.lend(finn, viola);
        finn.lend(marcelline, viola);
        marcelline.lend(iceKing, viola);
        iceKing.lend(finn, viola);

        assertTrue(finn.hasCustody(viola));
        assertFalse(marcelline.isBorrowing(viola));
        assertFalse(iceKing.isBorrowing(viola));
    }

    @Test
    public void testInventories() {
        assertEquals(iceKing.inventory(), "");
        assertEquals(finn.inventory(), "- épée rouge\n");

        jake.lend(finn, viola);
        marcelline.lend(finn, guitar);
        assertEquals(finn.inventory(), "" +
                "- épée rouge\n" +
                "- violon alto\n" +
                "- hache à 4 cordes\n");
    }
}
