package hr.fer.zemris.ppj.manipulators.expressions;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.SemanticErrorReporter;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;
import hr.fer.zemris.ppj.types.IntType;
import hr.fer.zemris.ppj.types.Type;

/**
 * <code>LogicalAndExpressionManipulator</code> is a manipulator for logical and expression.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class LogicalAndExpressionManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<LogicalAndExpression>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<log_i_izraz>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 60.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        final Node firstChild = node.getChild(0);
        final String firstSymbol = firstChild.name();

        // <log_i_izraz> ::= <bin_ili_izraz>
        if ("<bin_ili_izraz>".equals(firstSymbol)) {

            // 1. provjeri(<bin_ili_izraz>)
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, firstChild.getAttribute(Attribute.TYPE));
            node.addAttribute(Attribute.L_EXPRESSION, firstChild.getAttribute(Attribute.L_EXPRESSION));
            return true;
        }

        final Node thirdChild = node.getChild(2);
        // <log_i_izraz> ::= <log_i_izraz> OP_I <bin_ili_izraz>
        if ("<log_i_izraz>".equals(firstSymbol)) {

            // 1. provjeri(<log_i_izraz>)
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            // 2. <log_i_izraz>.tip ~ int
            final Type type1 = (Type) firstChild.getAttribute(Attribute.TYPE);
            if (!type1.implicitConversion(new IntType())) {
                SemanticErrorReporter.report(node);
                return false;
            }

            // 3. provjeri(<bin_ili_izraz>)
            if (!thirdChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            // 4. <bin_ili_izraz>.tip ~ int
            final Type type2 = (Type) thirdChild.getAttribute(Attribute.TYPE);
            if (!type2.implicitConversion(new IntType())) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, new IntType());
            node.addAttribute(Attribute.L_EXPRESSION, false);
            return true;
        }

        System.err.println("Shold never happen");
        SemanticErrorReporter.report(node);
        return false;
    }

    @Override
    public void generate(Node node) {
        switch (Production.fromNode(node)) {
        case LOGICAL_AND_EXPRESSION_1: {
            // LOGICAL_AND_EXPRESSION_1("<log_i_izraz> ::= <bin_ili_izraz>"),
            node.getChild(0).generate();
            break;
        }

        case LOGICAL_AND_EXPRESSION_2: {
            // LOGICAL_AND_EXPRESSION_2("<log_i_izraz> ::= <log_i_izraz> OP_I <bin_ili_izraz>"),
            node.getChild(0).generate();
            node.getChild(2).generate();
            break;
        }

        default:
            System.err.println("Generation reached undefined production!");
            break;
        }
    }
}
