package hr.fer.zemris.ppj.grammar;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.ppj.grammar.interfaces.Symbol;

/**
 * <code>Production</code> represents a production of a context free grammar.
 *
 * @author Jan Kelemen
 *
 * @version alpha
 */
public class Production implements Comparable<Production> {

    private final int order;
    private final Symbol leftSide;
    private final List<Symbol> rightSide = new ArrayList<>();

    /**
     * Class constructor, specifies the left and the right side of the production.
     *
     * @param leftSide
     *            the left side.
     * @param rightSide
     *            the right side.
     * @param order
     *            order of the production, used to resolve grammar ambiguities.
     * @since alpha
     */
    public Production(final Symbol leftSide, final List<Symbol> rightSide, final int order) {
        this.leftSide = leftSide;
        this.rightSide.addAll(rightSide);
        this.order = order;
    }

    /**
     * @return returns the left side of the production.
     * @since alpha
     */
    public Symbol leftSide() {
        return leftSide;
    }

    /**
     * @return returns the right side of the production.
     * @since alpha
     */
    public List<Symbol> rightSide() {
        return new ArrayList<>(rightSide);
    }

    /**
     * Checks if the production is a epsilon production.
     *
     * @return <code>true</code> if the production is a epsilon production, <code>false</code> otherwise.
     * @since alpha
     */
    public boolean isEpsilonProduction() {
        return rightSide.isEmpty();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o instanceof Production) {
            final Production production = (Production) o;

            // return leftSide.equals(production.leftSide()) && rightSide.equals(production.rightSide());
            return order == production.order;
        }

        return false;
    }

    @Override
    public int hashCode() {
        // final int prime = 31;
        // int result = 1;
        //
        // result = (prime * result) + leftSide.hashCode();
        // result = (prime * result) + rightSide.hashCode();
        //
        // return result;
        return order;
    }

    @Override
    public String toString() {
        String result = leftSide.toString() + ((rightSide.isEmpty()) ? " null" : "");

        for (final Symbol symbol : rightSide) {
            result += " " + symbol.toString();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @since
     */
    @Override
    public int compareTo(final Production o) {
        return Integer.compare(order, o.order);
    }
}
