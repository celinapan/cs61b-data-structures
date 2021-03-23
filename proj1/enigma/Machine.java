package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Trang Van
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        HashMap<String, Rotor> zipRotors = new HashMap<>();
        _rotors = new Rotor[numRotors()];
        for (Rotor r : _allRotors) {
            if (zipRotors.containsKey(r.name())) {
                throw EnigmaException.error("Duplicate rotor name");
            } else {
                zipRotors.put(r.name(), r);
            }
        }
        for (int i = 0; i < rotors.length; i += 1) {
            if (zipRotors.containsKey(rotors[i])) {
                Rotor currRotor = zipRotors.get(rotors[i]);
                if (!currRotor.reflecting() && i == 0) {
                    throw EnigmaException.error("Invalid Rotor Position");
                } else {
                    _rotors[i] = currRotor;
                }
            } else {
                throw EnigmaException.error("Rotor Not Found");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int k = 0;
        if (setting.length() == numRotors() - 1) {
            for (int i = 1; i < _rotors.length; i += 1) {
                if (_alphabet.contains(setting.charAt(k))) {
                    _rotors[i].set(setting.charAt(k));
                    k += 1;
                } else {
                    throw EnigmaException.error("Invalid Char in Setting");
                }
            }
        } else {
            throw EnigmaException.error("Invalid Number of Characters");
        }

    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Set Alphabet Ring.
     * @param setting string*/
    void setRing(String setting) {
        for (int i = 1; i < numRotors(); i += 1) {
            _rotors[i].ringSetting(setting.charAt(i - 1));
        }
    }
    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int lastRotor = _rotors.length - 1;
        boolean[] advanceMask = new boolean[_rotors.length];
        advanceMask[lastRotor] = true;
        for (int i = lastRotor; i > 0; i -= 1) {
            Rotor currRotor = _rotors[i];
            Rotor prevRotor = _rotors[i - 1];
            if (currRotor.atNotch() && prevRotor.rotates()) {
                advanceMask[i] = true;
                advanceMask[i - 1] = true;
            }
        }
        for (int i = 0; i < advanceMask.length; i += 1) {
            if (advanceMask[i]) {
                _rotors[i].advance();
            }
        }
        int translate = _plugboard.permute(c);
        for (int i = lastRotor; i >= 0; i -= 1) {
            translate = _rotors[i].convertForward(translate);
        }
        for (int i = 1; i <= lastRotor; i += 1) {
            translate = _rotors[i].convertBackward(translate);
        }
        return _plugboard.invert(translate);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll("\\s", "");
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            char getChar = msg.charAt(i);
            if (_alphabet.contains(getChar)) {
                result += _alphabet.toChar(convert(_alphabet.toInt(getChar)));
            }
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of Rotors for Machine. */
    private int _numRotors;
    /** Number of Pawls for Machine. */
    private int _pawls;
    /** Collection of all available rotors. */
    private Collection<Rotor> _allRotors;
    /** Array of rotors ot use in set up. */
    private Rotor[] _rotors;
    /** Permutation for plugboard setting. */
    private Permutation _plugboard;
}
