import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


abstract class  TriviaQuestion {

    public String question;		// Actual question
    public String answer;		// Answer to question
    public int value;			// Point value of question

    public TriviaQuestion() {
        question = "";
        answer = "";
        value = 0;
    }

    public TriviaQuestion(String q, String a, int v) {
        question = q;
        answer = a;
        value = v;
    }
    int getValue(){
        return value;
    }

    @Override
    public String toString() {
        return question;
    }

    public abstract boolean checkAnswer(String answer);
}
class TrueFalseTriviaQuestion extends TriviaQuestion{
    TrueFalseTriviaQuestion(String q, String a, int v){
        super(q,a,v);
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nEnter 'T' for true or 'F' for false.";
    }

    @Override
    public boolean checkAnswer(String answer) {
        if (answer.charAt(0) == this.answer.charAt(0)) return true;
        return false;
    }
}
class FreeFormTriviaQuestion extends TriviaQuestion{
    FreeFormTriviaQuestion(String q, String a, int v){
        super(q,a,v);
    }

    @Override
    public boolean checkAnswer(String answer) {
        if (answer.toLowerCase().equals(this.answer.toLowerCase()))  return true;
            return false;


    }

}

class TriviaData {

    private ArrayList<TriviaQuestion> data;

    public TriviaData() {
        data = new ArrayList<>();
    }

    public void addQuestion(TriviaQuestion question) {
        data.add(question);
    }

    public void showQuestion(int index) {
        TriviaQuestion q = data.get(index);
        System.out.println("Question " + (index + 1) + ".  " + q.getValue() + " points.");
        System.out.println(q);

    }

    public int numQuestions() {
        return data.size();
    }

    public TriviaQuestion getQuestion(int index) {
        return data.get(index);
    }
}

public class TriviaGame {

    public TriviaData questions;	// Questions

    public TriviaGame() {
        // Load questions
        questions = new TriviaData();
        questions.addQuestion(new FreeFormTriviaQuestion("The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3));
        questions.addQuestion(new TrueFalseTriviaQuestion("Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1));
        questions.addQuestion(new FreeFormTriviaQuestion("1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2));
        questions.addQuestion(new FreeFormTriviaQuestion("I am the geometric figure most like a lost parrot",
                "polygon", 2));
        questions.addQuestion(new TrueFalseTriviaQuestion("Generics were introducted to Java starting at version 5.0.",
                "T", 1));
    }
    // Main game loop

    public static void main(String[] args) {
        int score = 0;			// Overall score
        int questionNum = 0;	// Which question we're asking
        TriviaGame game = new TriviaGame();
        Scanner keyboard = new Scanner(System.in);
        // Ask a question as long as we haven't asked them all
        while (questionNum < game.questions.numQuestions()) {
            // Show question
            game.questions.showQuestion(questionNum);
            // Get answer
            String answer = keyboard.nextLine();
            // Validate answer
            TriviaQuestion q = game.questions.getQuestion(questionNum);

            if(q.checkAnswer(answer)){
                System.out.println("That is correct!  You get " + q.value + " points.");
                score += q.value;
            }else{
                System.out.println("Wrong, the correct answer is " + q.answer);
            }

            System.out.println("Your score is " + score);
            questionNum++;
        }
        System.out.println("Game over!  Thanks for playing!");
    }
}
