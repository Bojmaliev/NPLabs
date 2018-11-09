import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();

                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
class Date{
    private int year;
    private int mounth;
    private int day;
    Date(String date){
        String d[] = date.split("-");
        this.year = Integer.parseInt(d[0]);
        this.mounth = Integer.parseInt(d[1]);
        this.day = Integer.parseInt(d[2]);
    }

    boolean isNewerOf(Date d){
        if(year > d.year)return true;
        else if (year < d.year) return false;
        else{
            if(mounth > d.mounth)return true;
            else if (mounth < d.mounth) return false;
            else{
                if(day > d.day)return true;
                else if (day < d.day) return false;
                else{
                    return false;
                }
            }

        }
    }

}
abstract class Contact{
    protected String date;
    Contact(String date){
        this.date = date;
    }
    public boolean isNewerThan(Contact c){
        Date d1 = new Date(date);
        Date d2 = new Date(c.date);
        return  d1.isNewerOf(d2);
    }

    @Override
    public abstract String toString();

    public abstract String getType();
}

class EmailContact extends Contact{
    private String email;
    EmailContact(String date, String email){
        super(date);
        this.email = email;
    }
    public String getType(){
        return "Email";
    }
    public String getEmail(){
        return email;
    }
    @Override
    public String toString() {
        return String.format("\"%s\"", email);
    }
}
enum Operator { VIP, ONE, TMOBILE }
class PhoneContact extends Contact{
    private String phone;
    PhoneContact(String date, String phone){
        super(date);
        this.phone = phone;
    }
    public String getType(){
        return "Phone";
    }
    public String getPhone(){
        return phone;
    }
    public Operator getOperator(){
        char op = phone.charAt(2);
        if(op == '0' || op == '1' || op == '2') return Operator.TMOBILE;
        if(op == '5' || op == '6')return Operator.ONE;
        return Operator.VIP;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", phone);
    }
}
class Student{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private ArrayList<Contact> contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = new ArrayList<>();
    }

    public void addEmailContact(String date, String email){
        contacts.add(new EmailContact(date, email));
    }
    public void addPhoneContact(String date, String phone){
        contacts.add(new PhoneContact(date, phone));
    }
    public Contact[] getEmailContacts(){
        return contacts.stream().filter(a-> a.getType().equals("Email")).toArray(EmailContact[]::new);
    }
    public Contact[] getPhoneContacts(){
        return contacts.stream().filter(a-> a.getType().equals("Phone")).toArray(PhoneContact[]::new);
    }
    public String getCity(){
        return this.city;
    }
    public int getNumContacts(){
        return contacts.size();
    }
    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }
    public long getIndex(){
        return index;
    }
    public Contact getLatestContact(){
        return contacts.stream().reduce((contact, contact2) -> (contact.isNewerThan(contact2) ? contact : contact2)).get();
    }

    @Override
    public String toString() {
        String phoneContacts = Arrays.stream(getPhoneContacts()).map(a->((PhoneContact)a).toString()).collect(Collectors.joining(", "));
        String emailContacts = Arrays.stream(getEmailContacts()).map(a->((EmailContact)a).toString()).collect(Collectors.joining(", "));

        return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%s, \"grad\":\"%s\", \"indeks\":%s, \"telefonskiKontakti\":[%s], \"emailKontakti\":[%s]}", firstName, lastName, age, city, index, phoneContacts, emailContacts);
    }
}

class Faculty{
    private String name;
    private ArrayList<Student> students;

    Faculty(String name, Student [] students){
        this.students = new ArrayList<>();
        this.name = name;
        this.students.addAll(Arrays.stream(students).collect(Collectors.toList()));
    }
    public int countStudentsFromCity(String cityName){
        return (int) students.stream().filter(a-> a.getCity().equals(cityName)).count();
    }
    public Student getStudent(long index){
        return students.stream().filter(a-> a.getIndex() == index).findFirst().get();
    }
    public double getAverageNumberOfContacts(){
        return students.stream().mapToInt(a-> a.getNumContacts()).sum()/(double)students.size();
    }
    public Student getStudentWithMostContacts(){
        return students.stream().reduce((a,b) -> (a.getNumContacts() > b.getNumContacts() ? a : (a.getNumContacts()==b.getNumContacts() ? (a.getIndex() > b.getIndex() ? a : b) : b))).get();
    }

    @Override
    public String toString() {
        String studentsString = students.toString();
        return String.format("{\"fakultet\":\"%s\", \"studenti\":%s}", name, studentsString);
    }
}