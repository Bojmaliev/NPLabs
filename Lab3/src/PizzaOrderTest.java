import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}

interface Item{
    int getPrice();
    String getType();
}
class ExtraItem implements Item{
    String type;
    ExtraItem(String type) throws Exception{
        if(!type.equals("Coke") && !type.equals("Ketchup")){
            throw new InvalidExtraTypeException("Not valid input");
        }
        this.type = type;
    }
    @Override
    public int getPrice(){
        switch (type){
            case "Coke" : return 5;
            case "Ketchup" : return 3;
            default: return 0;
        }
    }
    @Override
    public String getType(){
        return this.type;
    }
}
class PizzaItem implements Item{
    String type;
    PizzaItem(String type) throws Exception{
        if(!type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian")){
            throw new InvalidPizzaTypeException("Not valid input");
        }
        this.type = type;
    }
    @Override
    public int getPrice(){
        switch (type){
            case "Standard" : return 10;
            case "Pepperoni" : return 12;
            case "Vegetarian" : return 8;
            default: return 0;
        }
    }
    @Override
    public String getType(){
        return this.type;
    }
}
class InvalidExtraTypeException extends Exception{
    InvalidExtraTypeException(String e){
        super(e);
    }
}
class InvalidPizzaTypeException extends Exception{
    InvalidPizzaTypeException(String e){
        super(e);
    }
}
class ItemOutOfStockException extends Exception{
    ItemOutOfStockException(Item item){

    }
}
class ArrayIndexOutOfBоundsException extends Exception{
    ArrayIndexOutOfBоundsException(int i){
        super(String.valueOf(i));
    }
}
class OrderLockedException extends Exception{
    OrderLockedException(){

    }
}
class Pair<E,T>{
    E item1;
    T item2;
    Pair(E item1, T item2){
        this.item1=item1;
        this.item2=item2;
    }
}
class EmptyOrder extends Exception{
    EmptyOrder(){
    }
}
class Order{
    ArrayList<Pair<Item, Integer>> listItems;
    boolean locked = false;
    Order(){
        this.listItems = new ArrayList<>();
    }
    void addItem(Item item, int count) throws Exception{
        if(locked)throw new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException(item);
        if(itemExists(item)){
            getItemByType(item.getType()).item2 = count;
        }else listItems.add(new Pair<>(item, count));
    }
    Pair<Item, Integer> getItemByType(String type){
        return listItems.stream().filter(a-> a.item1.getType().equals(type)).findFirst().get();
    }
    boolean itemExists(Item item){

        return listItems.stream().anyMatch(a -> a.item1.getType().equals(item.getType()));
    }
    int getPrice(){
        return listItems.stream().map(a-> a.item1.getPrice() * a.item2).reduce((a,b)->a+b).get();
    }
    void removeItem(int idx) throws Exception{
        if(locked)throw new OrderLockedException();
        if(idx >= listItems.size())throw new ArrayIndexOutOfBоundsException(idx);
        listItems.remove(idx);
    }
    void displayOrder(){
        IntStream.range(0, listItems.size()).forEach(i-> System.out.println(String.format("  %d.%-15sx%2d%5d$", i+1, listItems.get(i).item1.getType(), listItems.get(i).item2, listItems.get(i).item2*listItems.get(i).item1.getPrice())));
        System.out.println(String.format("Total:                %5d$", getPrice()));
    }
    void lock() throws Exception{
        if(listItems.size() == 0){
            throw new EmptyOrder();
        }
        this.locked = true;
    }
}