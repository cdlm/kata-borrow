package borrow;

public class Thing {
    protected String description;
    protected Person owner;

    public Thing(String description, Person initialOwner) {
        this.description = description;
        this.owner = initialOwner;
    }

    public Person getOwner() { return owner; }

    public String toString() { return description; }
}
