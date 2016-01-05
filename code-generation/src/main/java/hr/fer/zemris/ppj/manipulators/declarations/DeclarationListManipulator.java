package hr.fer.zemris.ppj.manipulators.declarations;

import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.Utils;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;

/**
 * <code>DeclarationListManipulator</code> is a manipulator for declaration list.
 *
 * @author Domagoj Polancec
 *
 * @version 1.1
 */
public class DeclarationListManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<DeclarationList>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<lista_deklaracija>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 68.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {

        for (final Node current : node.getChildren()) {
            if (!current.check()) {
                return Utils.badNode(node);
            }
        }

        return true;
    }

    @Override
    public void generate(Node node) {
        switch (Production.fromNode(node)) {
            case DECLARATION_LIST_1: {
                break;
            }

            case DECLARATION_LIST_2: {
                break;
            }

            default:
                System.err.println("Generation reached undefined production!");
                break;
        }
    }

}
