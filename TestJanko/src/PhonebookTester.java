import java.io.*;
import java.util.*;

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = (PhoneBook) PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}

class Contact{
    String name;
    String [] phonenumberr;

    Contact(String name, String... phonenumber)
    {
        phonenumberr=new String[0];
        if(!name.matches("[A-Za-z0-9]{4,10}"))  throw new InvalidNameException();
        for(String s:phonenumber) addNumber(s);

        this.name=name;
    }

    public String getName(){
        return name;
    }

    String[] getNumbers()
    {
        String [] pn=phonenumberr.clone();

        Arrays.sort(pn);
        return pn;
    }

    void addNumber(String phoneNumber)throws RuntimeException{
        if(phonenumberr.length==5)throw new MaximumSizeExceddedException(); //martin phonenumberr.length==5
        if(!phoneNumber.matches("07[0125678][0-9]{6}")) throw new InvalidNumberException();
        String[]nova=Arrays.copyOf(phonenumberr,phonenumberr.length+1);
        nova[nova.length-1]=phoneNumber;
        phonenumberr=nova;


    }
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(name).append("\n").append(phonenumberr.length).append("\n");
        sb.append("[");
        String [] podredena=getNumbers();
        sb.append(String.join(", ", podredena));
        sb.append("]");

        return sb.toString();
    }


    static Contact valueOf(String s) throws InvalidFormatException{
        try {
            Scanner sc = new Scanner(s);
            String name = sc.next();
            String[] telbroevi = new String[0];
            int broj = sc.nextInt();
            for (int i = 0; i < broj; i++) {
                telbroevi[i] = sc.next();
            }
            return new Contact(name,telbroevi);
        }

        catch(Exception e)
        {
            throw new InvalidFormatException();
        }




    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.getName())&&Arrays.equals(phonenumberr, contact.phonenumberr);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(phonenumberr);
        return result;
    }
}


class PhoneBook{

    ArrayList<Contact> contacts;

    PhoneBook(){
        contacts=new ArrayList<>();
    }

    void addContact(Contact contact){
        if(contacts.size()>=250) throw new MaximumSizeExceddedException();
        for( Contact c: contacts)
        {
            if(c.getName().equals(contact.name)) throw new InvalidNameException();
        }

        contacts.add(contact);

    }

    Contact getContactForName(String name)
    {
        for(Contact c:contacts)
        {
            if(c.getName().equals(name)) return c;
        }

        return null;
    }


    int numberOfContacts()
    {
        return contacts.size();
    }

    Contact [] getContacts(){
        return contacts.stream().sorted(Comparator.comparing(Contact::getName)).toArray(Contact[]::new);
    }

    boolean removeContact(String name)
    {
        for(Contact c:contacts)
        {
            if(c.getName().equals(name))
            {
                contacts.remove(c);
                return true;
            }

        }

        return false;
    }

    public String toString(){
        contacts.stream().sorted(Comparator.comparing(Contact::getName));

        StringBuilder sb=new StringBuilder();
        for(Contact c:contacts)
        {
            sb.append(c.toString()).append("\n");
        }


        return sb.toString();

    }


    Contact [] getContactsForNumber(String number_prefix){

        ArrayList<Contact> e=new ArrayList<>();

        for(Contact c:contacts)
        {
            String [] a=c.getNumbers();
            for(int i=0;i<a.length;i++)
            {
                if(a[i].startsWith(number_prefix,0))
                {
                    e.add(c);
                    break;
                }
            }
        }

        return e.stream().toArray(Contact[]::new);
    }

    static boolean saveAsTextFile(PhoneBook phonebook, String path)
    {
        try{

            File file=new File(path);
            file.createNewFile();
            PrintWriter pw=new PrintWriter(file);
            pw.print(phonebook.toString());
            pw.flush();
            pw.close();
            return true;

        } catch (IOException e) {
            throw new InvalidFormatException();

        }

    }

    static PhoneBook loadFromTextFile(String path)
    {

        try(BufferedReader br=new BufferedReader(new FileReader(new File(path))))
        {
            String current;
            PhoneBook phoneBook = new PhoneBook();
            while ((current = br.readLine()) != null) {
                String name = current;
                int len = Integer.parseInt(br.readLine());
                String phoneNumbers[] = new String[len];
                for (int i = 0; i < len; i++) {
                    phoneNumbers[i] = br.readLine();
                }
                Contact contact = new Contact(name, phoneNumbers);
                phoneBook.addContact(contact);
                br.readLine();
            }
            return phoneBook;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneBook phonebook = (PhoneBook) o;
        return Objects.equals(contacts, phonebook.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts);
    }
}

class InvalidFormatException extends RuntimeException{
    InvalidFormatException(){};
}


class InvalidNameException extends RuntimeException{

    public boolean name;

    InvalidNameException(){}
}

class MaximumSizeExceddedException extends  RuntimeException{
    MaximumSizeExceddedException(){}
}

class InvalidNumberException  extends  RuntimeException{
    InvalidNumberException(){}
}
