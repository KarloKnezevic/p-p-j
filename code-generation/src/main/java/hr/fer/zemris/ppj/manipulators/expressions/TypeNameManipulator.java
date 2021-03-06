package hr.fer.zemris.ppj.manipulators.expressions;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.SemanticErrorReporter;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;
import hr.fer.zemris.ppj.types.Type;
import hr.fer.zemris.ppj.types.VoidType;

/**
 * <code>TypeNameManipulator</code> is a manipulator for type name.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class TypeNameManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<TypeName>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<ime_tipa>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 56.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        final Node firstChild = node.getChild(0);
        final String firstSymbol = firstChild.name();
        // <ime_tipa> ::= <specifikator_tipa>
        if ("<specifikator_tipa>".equals(firstSymbol)) {

            // 1. provjeri(<specifikator_tipa>
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, firstChild.getAttribute(Attribute.TYPE));
            return true;
        }

        final Node secondChild = node.getChild(1);
        // <ime_tipa> ::= KR_CONST <specifikator_tipa>
        if ("KR_CONST".equals(firstSymbol)) {

            // 1. provjeri(<specifikator_tipa>)
            if (!secondChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            // 2. <specifikator_tipa>.tip != void
            final Type type = (Type) secondChild.getAttribute(Attribute.TYPE);
            if (type.equals(new VoidType())) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, type.toConst());
            return true;
        }
        System.err.println("Shold never happen");
        SemanticErrorReporter.report(node);
        return false;
    }

    @Override
    public void generate(Node node) {
        // TYPE_NAME_1("<ime_tipa> ::= <specifikator_tipa>"),
        // TYPE_NAME_2("<ime_tipa> ::= KR_CONST <specifikator_tipa>"),
        switch (Production.fromNode(node)) {
        case TYPE_NAME_1: {
            node.getChild(0).generate();
            break;
        }

        case TYPE_NAME_2: {
            node.getChild(1).generate();
            break;
        }

        default:
            System.err.println("Generation reached undefined production!");
            break;
        }
    }
}
