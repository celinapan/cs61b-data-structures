package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Trang Van
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output.
     *  Source: StackOverflow, splicing array with range*/
    private void process() {
        Machine m = readConfig();
        String s = "";
        if (_input.hasNextLine()) {
            s = _input.nextLine();
            if (s.length() > 0 && s.charAt(0) == '*') {
                setUp(m, s);
            } else {
                throw error("Need input configuration");
            }
        }
        while (_input.hasNextLine()) {
            s = _input.nextLine();
            if (s.length() == 0) {
                printMessageLine("");
            } else if (s.charAt(0) == '*') {
                setUp(m, s);
            } else {
                printMessageLine(m.convert(s));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config.
     *  Source: GeeksForGeeks (Convert Char to Int)*/
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine().trim());
            Integer numRotors = _config.nextInt();
            Integer numPawls = _config.nextInt();
            _config.nextLine();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNextLine() && _config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String cycles = "";
            while (_config.hasNext(Pattern.compile("\\(.+"))) {
                cycles += _config.nextLine();
            }
            Permutation p = new Permutation(cycles, _alphabet);

            if (type.length() == 1) {
                if (type.charAt(0) == 'N') {
                    return new FixedRotor(name, p);

                } else if (type.charAt(0) == 'R') {
                    return new Reflector(name, p);
                }
            }
            return new MovingRotor(name, p, type.substring(1));
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] s = settings.substring(2).split("\\s+");
        String[] rotorNames = Arrays.copyOfRange(s, 0, M.numRotors());
        String[] mSetting = Arrays.copyOfRange(s, M.numRotors(), s.length);
        String[] plugboard;
        String ringSetting = "";
        String cycles = "";
        if (rotorNames.length != M.numRotors()) {
            throw EnigmaException.error("Wrong number of rotors defined");
        }
        if (mSetting.length != 0) {
            String setting = mSetting[0].trim();
            if (mSetting.length > 1 && mSetting[1].trim().charAt(0) != '(') {
                ringSetting = mSetting[1];
                plugboard = Arrays.copyOfRange(mSetting, 2, mSetting.length);
            } else {
                plugboard = Arrays.copyOfRange(mSetting, 1, mSetting.length);
            }
            for (String plug: plugboard) {
                cycles += plug.trim();
            }
            M.setPlugboard(new Permutation(cycles, _alphabet));
            M.insertRotors(rotorNames);
            M.setRotors(setting);
            if (ringSetting.length() > 0) {
                M.setRing(ringSetting);
            }
        } else {
            throw EnigmaException.error("No initial settings");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() > 5) {
            _output.print(msg.substring(0, 5) + " ");
            msg = msg.substring(5);
        }
        _output.println(msg.trim());
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
