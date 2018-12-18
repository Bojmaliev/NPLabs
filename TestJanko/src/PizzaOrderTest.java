import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

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
class InvalidExtraTypeException extends RuntimeException{}
class ItemOutOfStockException extends  RuntimeException{}
class InvalidPizzaTypeException extends RuntimeException{}
class ArrayIndexOutOfBоundsException extends RuntimeException{}
class OrderLockedException extends RuntimeException{}
class EmptyOrder extends RuntimeException{}
interface Item{
    int getPrice();
    String getType();
}
class ExtraItem implements Item{
    String type;
    ExtraItem(String type){
        if(!type.equals("Coke") && !type.equals("Ketchup"))throw new InvalidExtraTypeException();
        this.type=type;
    }
    @Override
    public int getPrice() {
        if(getType().equals("Ketchup")) return 3;
        if(getType().equals("Coke")) return 5;
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }
}
class PizzaItem implements Item{
    String type;
    PizzaItem(String type){
        if(!type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian"))throw new InvalidPizzaTypeException();
        this.type=type;
    }

    @Override
    public int getPrice() {
        if(getType().equals("Standard")) return 10;
        if(getType().equals("Pepperoni")) return 12;
        if(getType().equals("Vegetarian")) return 8;
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }
}
class OrderItem{
    Item item;
    int count;
    OrderItem(Item item, int count){
        this.item = item;
        this.count = count;
    }
    int getPrice(){
        return item.getPrice()*count;
    }

    @Override
    public String toString() {
        return String.format("%-15sx%2d%5d$", item.getType(), count, getPrice());
    }
}
class Order{
    private ArrayList<OrderItem> items;
    private boolean isLocked;
    Order(){
        isLocked=false;
        items = new ArrayList<>();
    }
    void addItem(Item item, int count){
        if(isLocked)throw  new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException();
        for(OrderItem i:  items){
            if(i.item.getType().equals(item.getType())){
                i.count = count;
                return;
            }
        }
        items.add(new OrderItem(item, count));
    }
    int getPrice(){
        int sum = 0;
        for(OrderItem oi : items){
            sum+=oi.getPrice();
        }
        return sum;
    }
    void displayOrder(){
        int i=1;
        for(OrderItem oi : items){
            System.out.println(String.format("%3d.%s", i, oi));
            i++;
        }
        System.out.println(String.format("%-22s%5d$", "Total:", getPrice()));

    }
    void removeItem(int idx){
        if(isLocked)throw  new OrderLockedException();
        if(idx> items.size()-1 || idx < 0)throw new ArrayIndexOutOfBоundsException();
        items.remove(idx);
    }
    void lock(){
        if(items.size() == 0)throw new EmptyOrder();
        isLocked= true;
    }

}
