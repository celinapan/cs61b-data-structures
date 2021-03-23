package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Trang Van
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = "" + chars.charAt(0);
        for (int i = 0; i < chars.length(); i += 1) {
            char getChar = chars.charAt(i);
            if (_chars.indexOf(getChar) < 0) {
                _chars += getChar;
            } else if (Character.isWhitespace(getChar)) {
                throw EnigmaException.error("Invalid Alphabet.");
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        try {
            return _chars.charAt(index);
        } catch (StringIndexOutOfBoundsException e) {
            throw EnigmaException.error("Index out of bounds.");
        }
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (_chars.indexOf(ch) == -1) {
            throw EnigmaException.error("Character Not Found.");
        } else  {
            return _chars.indexOf(ch);
        }
    }

    /** Fields: _CHARS. */
    private String _chars;
}
