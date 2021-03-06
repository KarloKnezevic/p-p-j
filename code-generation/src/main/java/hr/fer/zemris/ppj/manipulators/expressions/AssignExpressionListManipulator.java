package hr.fer.zemris.ppj.manipulators.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.SemanticErrorReporter;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.interfaces.Manipulator;
import hr.fer.zemris.ppj.types.Type;

/**
 * <code>AssignExpressionListManipulator</code> is a manipulator for assign expression list.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class AssignExpressionListManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<AssignExpressionList>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<lista_izraza_pridruzivanja>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 72.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        final Node firstChild = node.getChild(0);
        final String firstSymbol = firstChild.name();

        // <lista_izraza_pridruzivanja> ::= <izraz_pridruzivanja>
        if ("<izraz_pridruzivanja>".equals(firstSymbol)) {

            // 1. provjeri(<izraz_pridruzivanja>)
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            final List<Type> types = new ArrayList<>(Arrays.asList((Type) firstChild.getAttribute(Attribute.TYPE)));
            node.addAttribute(Attribute.TYPES, types);
            node.addAttribute(Attribute.ELEMENT_COUNT, 1);
            return true;
        }

        final Node thirdChild = node.getChild(2);
        // <lista_izraza_pridruzivanja> ::= <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>
        if ("<lista_izraza_pridruzivanja>".equals(firstSymbol)) {

            // 1. provjeri(<lista_izraza_pridruzivanja>)
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            // 2. provjeri(<izraz_pridruzivanja>)
            if (!thirdChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            @SuppressWarnings("unchecked")
            final List<Type> types = new ArrayList<>((List<Type>) firstChild.getAttribute(Attribute.TYPES));
            types.add((Type) thirdChild.getAttribute(Attribute.TYPE));
            final int elementCount = (Integer) firstChild.getAttribute(Attribute.ELEMENT_COUNT) + 1;
            node.addAttribute(Attribute.TYPES, types);
            node.addAttribute(Attribute.ELEMENT_COUNT, elementCount);
            return true;
        }

        System.err.println("Shold never happen");
        SemanticErrorReporter.report(node);
        return false;
    }

    @Override
    public void generate(Node node) {
        switch (Production.fromNode(node)) {
        case ASSIGN_EXPRESSION_LIST_1: {
            // ASSIGN_EXPRESSION_LIST_1("<lista_izraza_pridruzivanja> ::= <izraz_pridruzivanja>"),
            node.getChild(0).generate();
            break;
        }

        case ASSIGN_EXPRESSION_LIST_2: {
            // <lista_izraza_pridruzivanja> ::= <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>");
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
