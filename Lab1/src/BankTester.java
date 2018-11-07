import javafx.scene.AmbientLight;

import java.util.*;
import java.util.stream.Collectors;

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly1.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println(b1.equals(b5));
            System.out.println("Your bank equals method do not work properly2.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly3.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly4.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}

class Account{
    private String name;
    private long id;
    private String balance;

    Account(String name, String balance){
        this.name = name;
        this.balance = balance;
        Random random = new Random();
        this.id = random.nextLong();
    }
    String getBalance(){
        return this.balance;
    }
    String getName(){
        return this.name;
    }
    long getId(){
        return this.id;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %s\n", getName(), getBalance());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Account acc = ((Account)obj);
        return getId() == acc.getId();
    }
}
abstract class Transaction{
    private long fromId;
    private long toId;
    protected String description;
    private String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getDescription(){
        return this.description;
    }
    public String getAmount() {
        return amount;
    }
}
class FlatAmountProvisionTransaction extends Transaction{
    private String flatAmount;
    FlatAmountProvisionTransaction(long fromId, long toId,String amount, String flatProvision){
        super(fromId, toId, "FlatAmount", amount);
        this.flatAmount = flatProvision;
    }

    String getFlatAmount(){
        return flatAmount;
    }
}
class FlatPercentProvisionTransaction extends Transaction{
    private int centsPerDollar;
    FlatPercentProvisionTransaction (long fromId, long toId, String amount, int centsPerDolar){
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDolar;
    }

    int getPercent(){
        return this.centsPerDollar;
    }
}
class Bank{
    private Account[] accounts;
    private String name;
    private String sumAll;
    private String provision;


    Bank(String name, Account accounts[]){
        this.accounts = accounts.clone();
        this.name = name;
        this.sumAll = "0.00$";
        this.provision = "0.00$";
    }
    static double stringToDouble(String s){
        return Double.parseDouble(s.substring(0, s.length()-1));
    }
    static String doubleToString(double broj){
        double test = (double)broj;

        return String.format("%.2f$", test);
    }
    public Account getAccount(long id){
        return (Account) Arrays.stream(accounts).filter(a-> a.getId() == id).toArray()[0];
    }
    public String getName(){
        return this.name;
    }
    public boolean makeTransaction(Transaction t){
        Account from;
        Account to;
        try {
            from = getAccount(t.getFromId());
            to = getAccount(t.getToId());
            if (stringToDouble(from.getBalance()) < stringToDouble(t.getAmount())) return false;
        }catch (Exception e){
            return false;
        }
        double kolku = stringToDouble(t.getAmount());
        this.sumAll = doubleToString(kolku+stringToDouble(this.sumAll));

        double kamata=0;
        if(t.getDescription().equals("FlatAmount")){
            kamata = stringToDouble(((FlatAmountProvisionTransaction)t).getFlatAmount());
        }else if(t.getDescription().equals("FlatPercent")){
            kamata = (((FlatPercentProvisionTransaction)t).getPercent()/100f) * ((int)kolku);
        }
        this.provision = doubleToString(kamata+stringToDouble(this.provision));

        double fromBalance = stringToDouble(from.getBalance()) - (kolku+kamata);
        from.setBalance(doubleToString(fromBalance));
        double toBalance = stringToDouble(to.getBalance()) + kolku;
        to.setBalance(doubleToString(toBalance));

        return true;
    }

    String totalTransfers(){
        return this.sumAll;
    }
    String totalProvision(){
        return this.provision;
    }
    public Account[] getAccounts(){
        return this.accounts;
    }
    public String getSum(){
        return  this.sumAll;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n\n", this.name));
        for(Account ac : this.accounts){
            sb.append(ac);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean r=true;
        if(obj == null) return false;
        if(!getName().equals(((Bank)obj).getName()))r = false;
        Account acc[] = ((Bank)obj).getAccounts();
        if(!getSum().equals(((Bank)obj).getSum()))r=false;
        if(accounts.length==acc.length) {
            for (int i = 0; i < accounts.length; i++) {
                if (!accounts[i].equals(acc[i])) r = false;
            }
        }else r = false;

        return r;
    }
}