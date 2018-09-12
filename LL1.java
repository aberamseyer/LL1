import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements an LL1 parser that checks a 'program' given as a command line argument for syntax errors based on its defined grammar.
 * It uses the recursive-descent (top-down) approach.
 * Prints "Yes" if syntax follows the given ruleset, otherwise prints "No"
 *
 * @author Abe Ramseyer
 */
public class LL1 {
    /* holds the input string of code */
    static Queue<Character> code;

    /* used for validating characters as numeric */
    static final String numbers = "0123456789";

    /**
     * Program entry point. Performs basic input validation and calls methods to parse the input.
     * @param the 'code' string to parse for validity
     */
    public static void main(String[] args) {
        // basic input validation
        if (args.length != 1) {
            System.err.println("Usage: java LL1 \"CODE\"");
            System.exit(1);
        }

        // add everything from the input to the queue for reading from while parsing
        code = args[0].chars().mapToObj(c -> (char) c)
                .collect(Collectors.toCollection(LinkedList::new));

        // manually append the EOF character
        code.offer('$');

        // call the first method to begin parsing
        e();

        // if we made it here, that means the code didn't have any errors, so we can safely print "Yes"
        System.out.println("Yes");
    }

    /**
     * rule E
     */
    static void e() {
        switch (code.peek()) {
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
            case '(':
                t();
                ePrime();
                break;
            default: no();
        }
    }

    /**
     * rule E'
     */
    static void ePrime() {
        switch (code.peek()) {
            case '+':
                get('+');
                t();
                ePrime();
                break;
            case '-':
                get('-');
                t();
                ePrime();
                break;
            case ')':
            case '$':
                break;
            default: no();
        }
    }

    /**
     * rule T
     */
    static void t() {
        switch (code.peek()) {
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
            case '(':
                f();
                tPrime();
                break;
            default: no();
        }
    }

    /**
     * rule T'
     */
    static void tPrime() {
        switch (code.peek()) {
            case '+':
            case '-':
            case ')':
            case '$':
                break;
            case '*':
                get('*');
                f();
                tPrime();
                break;
            case '/':
                get('/');
                f();
                tPrime();
                break;
            default: no();
        }
    }

    /**
     * rule F
     */
    static void f() {
        switch (code.peek()) {
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                get('n');
                break;
            case '(':
                get('(');
                e();
                get(')');
                break;
            default: no();
        }
    }

    /**
     * removes a token from the input string. A token is either a symbol or a number, as seen in the two conditions
     * @param expected the character we expect to see if syntax is valid
     */
    static void get(char expected) {
        char next = code.poll();
        if (expected == 'n' && numbers.substring(1).indexOf(next) != -1) { // check for a number without a leading '0'
            while (numbers.indexOf(code.peek()) != -1) // remove any remaining digits of the same number
                code.poll();
            return;
        }
        else if (next == expected) // check for an exact symbol match
            return;

        // at this point, next is either null (queue is empty) or holds the wrong character, so we know there's a syntax error
        no();
    }

    /*
     *  If at any time a mismatch is detected, this method is called to print "No" and exit the program
     */
    static void no() {
        System.out.println("No");
        System.exit(0);
    }
}