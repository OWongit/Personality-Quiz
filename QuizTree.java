// Owen Wilhere
// 02/28/2023
// CSE 123
// TA: Jay Dharmadhikari
// This class is QuizTree. It functions as an interactive quiz similar to the
// famous personality quizzes seen on the buzzfeed website.
import java.io.*;
import java.util.*;

public class QuizTree {
    private QuizTreeNode quiz;

    // Constructs a QuizTree object by reading quiz data from a provided Scanner
    // inputFile. It initializes the quiz tree using the data from the inputFile. 
    // Parameters: Scanner inputFile - the scanner object representing the input 
    //                                 file containing quiz data in pre-order 
    //                                 traversal format. 
    // No Returns.
    public QuizTree(Scanner inputFile) {
        this.quiz = writeQuiz(inputFile);
    }

    // This method writes a quiz using an inputFile as a parameter containing
    // nodes in pre-order traversal format. 
    // Returns a QuizTreeNode that is the first node of the quiz.
    private QuizTreeNode writeQuiz(Scanner inputFile) {
        String line = inputFile.nextLine();
        int score = Character.getNumericValue(line.charAt(line.length() - 1));
        line = line.split("-")[0];
        if (line.startsWith("END:")) {
            line = line.split(":")[1];
            return new QuizTreeNode(score, line);
        }
        return new QuizTreeNode(
            score, line, writeQuiz(inputFile), writeQuiz(inputFile));
    }

    // This method takes the quiz by prompting the user with various questions
    // and prints the score. 
    // Paramaters: Scanner console - the scanner used to interact with the user. 
    // No Returns.
    public void takeQuiz(Scanner console) {
        int totalScore = takeQuiz(console, quiz);
        System.out.println("Your score is: " + totalScore);
    }

    // This method takes the quiz by prompting the user with various questions
    // and prints the score. 
    // Paramaters: Scanner console - the scanner used to interact with the user.
    //             QuizTreeNode node - the node the quiz is currently on in the
    //                                 traversal.
    // Returns an integer representing the score of a specific node or the total
    // of a branch of nodes.
    private int takeQuiz(Scanner console, QuizTreeNode node) {
        if (node != null && node.left == null && node.right == null) {
            System.out.println("Your result is: " + node.result);
            return node.score;
        } else {
            String choiceLeft = node.result.split("/")[0];
            String choiceRight = node.result.split("/")[1];
            System.out.print(
                "Do you prefer " + choiceLeft + " or " + choiceRight + "? ");
            String userChoice = console.nextLine();
            if (userChoice.equalsIgnoreCase(choiceLeft)) {
                return node.score + takeQuiz(console, node.left);
            } else if (userChoice.equalsIgnoreCase(choiceRight)) {
                return node.score + takeQuiz(console, node.right);
            }
            System.out.println(" Invalid response, enter a valid response.");
            return 0 + takeQuiz(console, node);
        }
    }

    // This method exports a quiz onto a file in pre-order traveral format.
    // Parameters: PrintStream - outputFile: the file location being printed on.
    // No Returns
    public void export(PrintStream outputFile) {
        export(quiz, outputFile);
    }

    // This method exports a quiz onto a file in pre-order traveral format.
    // Parameters: PrintStream - outputFile: the file location being printed on.
    //             QuizTreeNode node - the node that's data is being printed.
    // No Returns
    private void export(QuizTreeNode node, PrintStream outputFile) {
        if (node != null && node.left == null && node.right == null) {
            outputFile.println("END:" + node.result + "-" + node.score);
        } else {
            outputFile.println(node.result + "-" + node.score);
            export(node.left, outputFile);
            export(node.right, outputFile);
        }
    }

    // This method replaces an end result of a quiz with another question with
    // two other results each node having a new score. 
    // Parameters: String toReplace - the end result being replaced.
    //             String choices - the choices of the new question.
    //             String leftResult - the left result of the choices.
    //             String rightResult - the right result of the choices.
    // No Returns.
    public void addQuestion(String toReplace, String choices, String leftResult,
        String rightResult) {
        quiz = addQuestion(toReplace, choices, leftResult, rightResult, quiz);
    }

    // This method replaces an end result of a quiz with another question with
    // two other results each node having a new score. 
    // Parameters: String toReplace - the end result being replaced.
    //             String choices - the choices of the new question.
    //             String leftResult - the left result of the choices.
    //             String rightResult - the right result of the choices.
    //             QuizTreeNode node - the node of the new question.
    // Returns a QuizTreeNode containting the new question.
    private QuizTreeNode addQuestion(String toReplace, String choices,
        String leftResult, String rightResult, QuizTreeNode node) {
        if (node != null) {
            if (node.result.equalsIgnoreCase(toReplace)) {
                String[] left = leftResult.split("-");
                String[] right = rightResult.split("-");
                String[] choicesSplit = choices.split("-");
                QuizTreeNode leftChoice =
                    new QuizTreeNode(Integer.parseInt(left[1]), left[0]);
                QuizTreeNode rightChoice =
                    new QuizTreeNode(Integer.parseInt(right[1]), right[0]);
                node = new QuizTreeNode(Integer.parseInt(choicesSplit[1]),
                    choicesSplit[0], leftChoice, rightChoice);
            } else {
                node.left = addQuestion(
                    toReplace, choices, leftResult, rightResult, node.left);
                node.right = addQuestion(
                    toReplace, choices, leftResult, rightResult, node.right);
            }
        }
        return node;
    }

    // This method is cutoff. It takes the quiz, but before it traverses the
    // quiz it prompts the user for a cutoff score. Once the quiz reaches this
    // score it automaticaly takes the quiz, selecting choices at random.
    // Paramaters: Scanner console - the scanner used to interact with
    //                               the user.
    // No Returns
    public void creativeExtension(Scanner console) {
        int cutoff = -1;
        System.out.print("What is your cutoff? ");
        cutoff = Integer.parseInt(console.nextLine());
        while (cutoff < 0) {
            System.out.println(
                "--Invalid reponse, cutoff must be equal to or greater than zero.--");
            System.out.print("What is your cutoff? ");
            cutoff = Integer.parseInt(console.nextLine());
        }
        int totalScore = creativeExtentension(console, quiz, cutoff, 0);
        System.out.println("Your score is: " + totalScore);
    }

    // This method is cutoff. It takes the quiz, but before it traverses the
    // quiz it prompts the user for a cutoff score. Once the quiz reaches this
    // score it automaticaly takes the quiz, selecting choices at random.
    // Paramaters: Scanner console - the scanner used to interact with the user.
    //             QuizTreeNode node - the node the quiz is currently on in the
    //                                 traversal. 
    //             int cutoff - the score to start the automated
    //                          random quiz at. 
    //             int scoreSoFar - the score so far in the
    //                              quiz.
    // Returns an integer representing the score of a specific node or the total
    // of a branch of nodes.
    private int creativeExtentension(
        Scanner console, QuizTreeNode node, int cutoff, int scoreSoFar) {
        scoreSoFar = scoreSoFar + node.score;
        if (scoreSoFar >= cutoff) {
            System.out.println("You have reached the cutoff.");
            Random ran = new Random();
            return autoTakeQuiz(node, ran);
        }
        if (node != null && node.left == null && node.right == null) {
            System.out.println("Your result is: " + node.result);
            return node.score;
        } else {
            String choiceLeft = node.result.split("/")[0];
            String choiceRight = node.result.split("/")[1];
            System.out.print(
                "Do you prefer " + choiceLeft + " or " + choiceRight + "? ");
            String userChoice = console.nextLine();
            if (userChoice.equalsIgnoreCase(choiceLeft)) {
                return node.score
                    + creativeExtentension(
                        console, node.left, cutoff, scoreSoFar);
            } else if (userChoice.equalsIgnoreCase(choiceRight)) {
                return node.score
                    + creativeExtentension(
                        console, node.right, cutoff, scoreSoFar);
            }
            System.out.println(" Invalid response, enter a valid response.");
            return 0 + takeQuiz(console, node);
        }
    }

    // This method automatically takes the quiz, choosing choices at random.
    // Parameters: QuizTreeNode node - the node the quiz is currently on in the
    //                                 traversal.
    //             Random ran - a Random object.
    // Returns an integer representing the score of a specific node or the total
    // of a branch of nodes.
    private int autoTakeQuiz(QuizTreeNode node, Random ran) {
        if (node != null && node.left == null && node.right == null) {
            System.out.println("Your randomized result is: " + node.result);
            return node.score;
        } else {
            int num = ran.nextInt(2);
            if (num == 0) {
                return node.score + autoTakeQuiz(node.left, ran);
            }
            return node.score + autoTakeQuiz(node.right, ran);
        }
    }

    // PROVIDED
    // Returns the given percent rounded to two decimal places.
    private double roundTwoPlaces(double percent) {
        return (double) Math.round(percent * 100) / 100;
    }

    // This class is a QuizTreeNode. It represents a single node in a QuizTree.
    private static class QuizTreeNode {
        public int score;
        public String result;
        public QuizTreeNode left;
        public QuizTreeNode right;

        // Constructs a Quiz leaf node with the given data.
        public QuizTreeNode(int score, String result) {
            this(score, result, null, null);
        }

        // Constructs a Quiz leaf or branch node with the given data and links.
        public QuizTreeNode(
            int score, String result, QuizTreeNode left, QuizTreeNode right) {
            this.score = score;
            this.result = result;
            this.left = left;
            this.right = right;
        }
    }
}
