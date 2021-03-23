import java.io.Reader;
import java.io.IOException;
import java.util.Objects;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Trang Van
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        // TODO: YOUR CODE HERE
        _from = from;
        _to = to;
        _str = str;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

    /** Overriding the abstract method from Reader */
    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        int charCount =  _str.read(cbuf, off, len);
        int index;
        for (int i = off; i < off + charCount; i++) {
            index = _from.indexOf(cbuf[i]);
            if(index == -1) {
                cbuf[i] = cbuf[i];
            } else {
                cbuf[i] = _to.charAt(index);
            }
        }
        return charCount;
    }
    @Override
    public void close() throws IOException {
        try {
            _str.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /** Instance variable */
    private String _from;
    private String _to;
    private Reader _str;
}
