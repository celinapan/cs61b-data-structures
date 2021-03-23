package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Trang Van
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _rotates = true;

    }

    @Override
    boolean rotates() {
        return _rotates;
    }

    @Override
    void advance() {
        int s = setting() + 1;
        set(permutation().wrap(s));
    }

    @Override
    boolean atNotch() {
        char[] notches = _notches.toCharArray();
        for (char c: notches) {
            if (c == permutation().alphabet().toChar(setting())) {
                return true;
            }
        }
        return false;
    }

    /** Notches on the rotor. */
    private String _notches;
    /** Boolean if it rotates, should be true. */
    private boolean _rotates;
}
