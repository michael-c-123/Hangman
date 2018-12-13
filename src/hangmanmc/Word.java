
package hangmanmc;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * @author Michael
 */
public class Word {
    private static final String[] VOWELS = {"A", "E", "I", "O", "U"};
    private static final String[] COMMONS = {"R", "S", "T", "L", "N", "E"};

    private final static Image[] IMAGES = new Image[9];

    static {
        BufferedImage img;
        try {
            int counter = 0;
            for (int i = IMAGES.length-1; i >= 0; i--) {
                img = ImageIO.read(new File("images/" + i + ".jpg"));
                IMAGES[counter] = img;
                counter++;
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error loading images.\n" + ex.getMessage(), "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String word;
    private String blanks;
    private String guessed;

    private int attempts;

    public Word(String word) {
        this.word = word.toUpperCase();
        char[] blankArray = new char[word.length()];
        Arrays.fill(blankArray, '_');
        blanks = new String(blankArray);
        guessed = "";
    }

    public boolean isEasy() {
        return (word.length() <= 4)
                && ((numVowels() >= 2) || numCommons() >= 1);
    }

    public boolean isMed() {
        return (word.length() > 4 && word.length() < 8)
                && ((numVowels() >= 2) || (numCommons() >= 2));
    }

    public boolean isHard() {
        return !isEasy() && !isMed();
    }

    private int numVowels() {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            String letter = word.substring(i, i + 1);   //charAt(i)
            for (String vowel : VOWELS)
                if (letter.equals(vowel)) {
                    count++;
                    break;
                }
//            if (Arrays.asList(VOWELS).contains(letter))
//                count++;
        }
        return count;
    }

    private int numCommons() {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            String letter = word.substring(i, i + 1);   //charAt(i)
            for (String common : COMMONS)
                if (letter.equals(common)) {
                    count++;
                    break;
                }
//            if (Arrays.asList(COMMONS).contains(letter))
//                count++;
        }
        return count;
    }

    public boolean guess(String letter) {
        final String orig = blanks;
        blanks = "";
        String chunk = word;
        int start = 0;

        for (int i = 0; i < word.length() && chunk.indexOf(letter) != -1; i++) {
            chunk = word.substring(i);  //from current letter to end
            String currentLetter = word.substring(i, i + 1);    //charAt
            if (currentLetter.equals(letter)) {
                for (int j = start; j < i; j++) //add segment from original to new blanks
                    blanks += orig.substring(j, j + 1);
                blanks += letter;   //add the successfully guessed letter
                start = i + 1;  //move starting point for segment
            }
        }

        blanks += orig.substring(start);    //add any remaining segment

        boolean found = !orig.equals(blanks);
        if (!found) { //wrong
            guessed += letter; // add to wrong letters
            attempts++;
        }
        return found;
    }

    public boolean won() {
        return word.equals(blanks);
    }

    public boolean lost() {
        return attempts >= IMAGES.length - 1;
    }

    public Image getImage() {
        return IMAGES[attempts];
    }

    public static Image getStartImage() {
        return IMAGES[0];
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < word.length(); i++) {
            string += word.charAt(i);
            string += " ";
        }
        return string.trim();
    }

    public String toBlanks() {
        String string = "";
        for (int i = 0; i < blanks.length(); i++) {
            string += blanks.substring(i, i + 1).toUpperCase();
            string += " ";
        }
        return string.trim();
    }

    public String toGuessed() {
        String string = "";
        for (int i = 0; i < guessed.length(); i++) {
            string += guessed.substring(i, i + 1).toUpperCase();
            string += " ";
        }
        string = string.trim();
        if(string.isEmpty())
            return " ";
        else
            return string;
    }

    public String getWord() {
        return word;
    }

}
