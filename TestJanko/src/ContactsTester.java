/*import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

abstract class Contact {
    private String date;

    Contact(String date){
        this.date=date;

    }

    public boolean isNewerThan(Contact c){
        String[] date1 = date.split("-");
        String[] date2 = c.date.split("-");

        if(Integer.parseInt(date1[0] )> Integer.parseInt(date2[0]))
            return true;
        if(Integer.parseInt(date1[0] )== Integer.parseInt(date2[0])) {
            if (Integer.parseInt(date1[1]) > Integer.parseInt(date2[1]))
                return true;
            if (Integer.parseInt(date1[1]) == Integer.parseInt(date2[0])){
                if (Integer.parseInt(date1[2]) > Integer.parseInt(date2[2]))
                    return true;
                else return false;
            }
            return false;

        }
          return false;

    }
    public abstract String getType();
}
class EmailContact extends  Contact{
    private String email;

    EmailContact(String date, String email){
        super(date);
        this.email=email;


    }
    public String getEmail(){

        return email;
    }
    @Override
    public String getType(){
        return "Email";
    }

}
enum Operator { VIP, ONE, TMOBILE }

class PhoneContact extends Contact{
    private String phone;

    PhoneContact(String date, String phone){
        super(date);
        this.phone=phone;
    }

    @Override
    public String getType(){
        return "Phone";
    }

    public String getPhone(){

        return phone;
    }
    public Operator getOperator(){
        char b = phone.charAt(2);
        if(b=='0' || b=='1' || b=='2')
            return Operator.TMOBILE;
        if(b=='5' || b=='6')
            return Operator.ONE;
        return Operator.VIP;
    }



}
class Student{

    private Contact[] kontakti;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;

    Student(String firstName, String lastName, String city, int age, long index){

        this.firstName=firstName;
        this.lastName=lastName;
        this.city=city;
        this.age=age;
        this.index=index;
        kontakti=new Contact[0];
    }

    public void addEmailContact(String date, String email){
        kontakti = Arrays.copyOf(kontakti, kontakti.length+1);
        kontakti[kontakti.length-1]=new EmailContact(date,email);

    }
    public void addPhoneContact(String date, String phone){
        kontakti = Arrays.copyOf(kontakti, kontakti.length+1);
        kontakti[kontakti.length-1]=new PhoneContact(date,phone);

    }
    public Contact[] getEmailContacts(){
        return Arrays.stream(kontakti).filter(a -> a.getType().equals("Email")).toArray(Contact[]::new);
    }
    public Contact[] getPhoneContacts(){
        return Arrays.stream(kontakti).filter(a -> a.getType().equals("Phone")).toArray(Contact[]::new);
    }
    public String getCity(){
        return city;
    }
    public long getIndex(){
        return index;
    }
    public Contact getLatestContact(){
        return Arrays.stream(kontakti).reduce((acc,b) -> (acc.isNewerThan(b) ? acc : b)).get();
    }
    public int getNumberContacts(){
        return kontakti.length;
    }

    @Override
    public String toString() {
        String phoneContacts = Arrays.stream(getPhoneContacts()).map(a-> "\""+((PhoneContact)a).getPhone()+"\"").collect(Collectors.joining(", "));
        String emailContacts = Arrays.stream(getEmailContacts()).map(a-> "\""+((EmailContact)a).getEmail()+"\"").collect(Collectors.joining(", "));

        return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[%s], \"emailKontakti\":[%s]}", firstName, lastName, age, city, index, phoneContacts, emailContacts);
    }
}

class Faculty{
    private String name;
    private Student[]students;

    Faculty(String name, Student [] students){

        this.name=name;
        this.students=students;
    }
    public int countStudentsFromCity(String cityName){
        return (int)Arrays.stream(students).filter(a->a.getCity().equals(cityName)).count();
    }
    public Student getStudent(long index){
        for(Student s : students)
            if(s.getIndex() == index) return s;
            return null;
    }
    public double getAverageNumberOfContacts(){
        int vkupnoKontakti=0;
        for(Student s:students){
            vkupnoKontakti+=s.getNumberContacts();

        }
        return vkupnoKontakti/(double)students.length;
    }
    public Student getStudentWithMostContacts(){
        Student max = students[0];
        for(Student s:students){
            if(max.getNumberContacts() < s.getNumberContacts()){
                max = s;
            }else if(max.getNumberContacts() == s.getNumberContacts()){
                if(max.getIndex() < s.getIndex()){
                    max = s;
                }
            }

        }
        return max;
    }

    @Override
    public String toString() {
        String studenti = Arrays.stream(students).map(a-> a.toString()).collect(Collectors.joining(", "));
        return String.format("{\"fakultet\":\"%s\", \"studenti\":[%s]}", name, studenti);
    }
}*/