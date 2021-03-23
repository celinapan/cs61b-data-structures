package enigma;

import static enigma.EnigmaException.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Trang Van
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.trim().replaceAll("\\)(\\s+)\\(", ")(");
        if (!validateFormat(cycles, _alphabet)) {
            throw EnigmaException.error("Invalid Cycles Format.");
        } else {
            _cycles = new HashMap<>();
            addCycle(cycles);
        }
    }

   /** Checks CYCLES input to see if it's well-formed.
    * @return true iff format is valid
    * @param s string
    * @param alpha alphabet*/
    private static boolean validateFormat(String s, Alphabet alpha) {
        String saveStr = s;
        char[] charArr = s.toCharArray();
        if (charArr.length == 0) {
            return true;
        }
        if (charArr[0] != '(') {
            return false;
        }
        for (int i = 0; i < charArr.length; i += 1) {
            char getChar = charArr[i];
            if (i != charArr.length - 1) {
                if ((charArr[i] == '(' && charArr[i + 1] == '(')) {
                    return false;
                }
                if (charArr[i] == ')') {
                    if (charArr[i + 1] == ')' || charArr[i + 1] != '(') {
                        return false;
                    }
                }
            } else {
                if (charArr[i] != ')') {
                    return false;
                }
            }
            if (!validateContents(saveStr, getChar, alpha)) {
                return false;
            }
        }
        return true;
    }

    /** Checks to see if CH is in Alphabet and is not a * or whitespace
     * Source: baeldung  to count chars in string.
     * @return iff contents are valid
     * @param s string
     * @param c char
     * @param alpha alphabet*/
    private static boolean validateContents(String s, char c, Alphabet alpha) {
        if (c == '(' || c == ')') {
            return true;
        } else if (!alpha.contains(c) || c == '*'
                || Character.isWhitespace(c)) {
            return false;
        } else if (alpha.contains(c)) {
            long count = s.chars().filter(ch -> ch == c).count();
            if (count > 1) {
                return false;
            }
        }
        return true;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm.
     *  Source: W3School(HashMap examples)*/
    private void addCycle(String cycle) {
        if (cycle.equals("")) {
            for (int i = 0; i < _alphabet.size(); i += 1) {
                char getChar = _alphabet.toChar(i);
                _cycles.put(getChar, getChar);
            }
        }
        ArrayList<String> cycleArr = makeCycleArr(cycle);
        for (String s: cycleArr) {
            for (int i = 0; i < s.length(); i += 1) {
                char first = s.charAt(0);
                if (i == s.length() - 1) {
                    _cycles.put(s.charAt(i), first);
                } else {
                    _cycles.put(s.charAt(i), s.charAt(i + 1));
                }
            }
        }
    }

    /** ADDCYCLES helper to extract Strings from () and add to ArrayList.
     *  Source: TutorialsPoint (Regex, Pattern & Matcher Class)
     *  @return ArrayList of Strings
     *  @param c string*/
    private ArrayList<String> makeCycleArr(String c) {
        ArrayList<String> arr = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher m = pattern.matcher(c);
        while (m.find()) {
            arr.add(m.group(1));
        }
        return arr;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw EnigmaException.error("Character not found.");
        } else if (!_cycles.containsKey(p)) {
            return p;
        } else {
            return _cycles.get(p);
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw EnigmaException.error("Character not found.");
        } else if (!_cycles.containsValue(c)) {
            return c;
        } else {
            char key = ' ';
            for (Character k: _cycles.keySet()) {
                if (c == _cycles.get(k)) {
                    key = k;
                }
            }
            return key;
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (Character k: _cycles.keySet()) {
            if (k == _cycles.get(k)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** HashMap to permute characters. */
    private HashMap<Character, Character> _cycles;
}
