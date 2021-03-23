/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 = "([\\d]|[0-1][0-2]|[0][\\d])\\/([\\d]|[0-2][\\d]|[0][\\d]|[3][0-1])\\/[1-2][\\d][\\d]+"; //FIXME: Add your regex here

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 = "\\(((\\d+)|(\\s*\\d+\\,\\s+\\d*))+[^\\,\\s]\\)"; //FIXME: Add your regex here

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 = "\\w+\\.(\\w+[-\\.]?)+"; //FIXME: Add your regex here

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "^[\\_+|\\$+]([\\_|\\w+|\\$+|\\d])*"; //FIXME: Add your regex here

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 = "[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}"; //FIXME: Add your regex here

}
