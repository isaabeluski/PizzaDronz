package uk.ac.ed.inf;

public class Menu {
    private String name;
    private int priceInPence;

    public Menu(String name, int priceInPence) {
        this.name = name;
        this.priceInPence = priceInPence;
    }

    public Menu() {

    }

    public String getName(){
        return this.name;
    }

    public int getPriceInPence() {
        return this.priceInPence;
    }
}
