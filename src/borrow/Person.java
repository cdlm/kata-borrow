package borrow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Person {
    protected String name;
    protected List<Thing> currentBelongings;
    protected Map<Thing, Person> borrowers, lenders;

    public Person(String name) {
        this.name = name;
        currentBelongings = new LinkedList<Thing>();
        borrowers = new HashMap<Thing, Person>();
        lenders = new HashMap<Thing, Person>();
    }

    public Thing acquireSomething(String description) {
        Thing something = new Thing(description, this);
        currentBelongings.add(something);
        return something;
    }

    public boolean isOwner(Thing something) { return something.getOwner() == this; }

    public boolean hasCustody(Thing something) { return currentBelongings.contains(something); }

    public boolean isBorrowing(Thing something) { return lenders.containsKey(something); }

    public boolean lend(Person borrower, Thing something) {
        if (!this.hasCustody(something)) return false;
        if (borrower == this) return true;

        borrowers.put(something, borrower);
        currentBelongings.remove(something);
        borrower.receiveCustody(something, this);
        return true;
    }

    public void giveBack(Thing something) {
        if (!this.hasCustody(something)) return;

        Person lender = lenders.get(something);
        currentBelongings.remove(something);
        lender.receiveCustody(something, this);
    }

    protected void receiveCustody(Thing something, Person previousCustodian) {
        if (previousCustodian == this) return;

        currentBelongings.add(something);

        if (this.isOwner(something) || this.isBorrowing(something)) {
            this.notifyBorrowers(something);
        } else {
            lenders.put(something, previousCustodian);
        }
    }

    protected void notifyBorrowers(Thing something) {
        if (!this.hasCustody(something)) lenders.remove(something);

        Person borrower = borrowers.remove(something);
        if (borrower != null) borrower.notifyBorrowers(something);
    }

    public String toString() { return name; }

    public String inventory() {
        StringBuilder sb = new StringBuilder();
        for (Thing each : currentBelongings) {
            sb.append("- ");
            sb.append(each);
            sb.append('\n');
        }
        return sb.toString();
    }
}
