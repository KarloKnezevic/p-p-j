package hr.fer.zemris.ppj.manipulators.terminals;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;

/**
 * <code>CharManipulator</code> is a manipulator for characters.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class CharManipulator implements Manipulator {

    // svi ascii + '\t', '\n', '\0', '\'', '\"', '\\'

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<Char>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "ZNAK";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 42.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        String value = (String) node.getAttribute(Attribute.VALUE);
        value = value.substring(1, value.length() - 1);

        char charValue;
        if (value.length() > 2) {
            return false;
        }
        else if (value.length() == 2) {
            switch (value) {
                case "\\t":
                    charValue = '\t';
                    break;
                case "\\n":
                    charValue = '\n';
                    break;
                case "\\0":
                    charValue = '\0';
                    break;
                case "\\\'":
                    charValue = '\'';
                    break;
                case "\\\"":
                case "\"":
                    charValue = '"';
                    break;
                case "\\\\":
                    charValue = '\\';
                    break;
                default:
                    return false;
            }
        }
        else {
            charValue = value.charAt(0);
            if (charValue == '\\') {
                return false;
            }
        }

        node.addAttribute(Attribute.VALUE, charValue);
        return true;
    }

    @Override
    public void generate(Node node) {
        return;
    }
}
