package hr.fer.zemris.ppj.manipulators.terminals;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;

/**
 * <code>IntManipulator</code> is a manipulator for integers.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class IntManipulator implements Manipulator {

    // -2,147,483,648 to 2,147,483,647

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<Int>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "BROJ";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 42.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        String value = (String) node.getAttribute((Attribute.VALUE));

        int intVal = 0;
        try {
            if (value.length() == 1) {
                intVal = Integer.parseInt(value);
            }
            else {
                final boolean negative = value.startsWith("-");
                if (negative) {
                    value = value.substring(1);
                }

                if (value.startsWith("0x") || value.startsWith("0X")) {
                    value = value.substring(2);
                    intVal = Integer.parseInt(value, 16);
                }
                else if (value.startsWith("0")) {
                    value = value.substring(1);
                    intVal = Integer.parseInt(value, 8);
                }
                else {
                    intVal = Integer.parseInt(value);
                }

                if (negative) {
                    intVal *= -1;
                }
            }
        }
        catch (final NumberFormatException e) {
            return false;
        }

        node.addAttribute(Attribute.VALUE, intVal);
        return true;
    }

    @Override
    public void generate(Node node) {
        return;
    }
}
