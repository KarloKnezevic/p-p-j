package hr.fer.zemris.ppj.finite.automaton.transfer;

import static hr.fer.zemris.ppj.finite.automaton.Automatons.extractStates;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.ppj.finite.automaton.interfaces.Input;
import hr.fer.zemris.ppj.finite.automaton.interfaces.State;
import hr.fer.zemris.ppj.finite.automaton.interfaces.TransferFunction;
import hr.fer.zemris.ppj.finite.automaton.interfaces.Transition;

/**
 * <code>FAutomatonTransferFunction</code> implements behavior common to all finite automaton transfer functions.
 *
 * @author Domagoj Polancec
 *
 * @version 1.0
 */
public abstract class FAutomatonTransferFunction implements TransferFunction {

    private final Set<FAutomatonTransition> transitions = new HashSet<>();
    private final Set<FAutomatonTransition> epsilonTransitions = new HashSet<>();
    private final Set<FAutomatonTransition> normalTransitions = new HashSet<>();

    /**
     * Class constructor, specifies the transitions of the transfer function.
     *
     * @param transitions
     *            the transitions.
     * @since 1.0
     */
    public FAutomatonTransferFunction(final Set<FAutomatonTransition> transitions) {
        this.transitions.addAll(transitions);

        for (final FAutomatonTransition transition : this.transitions) {
            if (transition.isEpsilonTransition()) {
                epsilonTransitions.add(transition);
            }
            else {
                normalTransitions.add(transition);
            }
        }
    }

    @Override
    public boolean hasTransition(final Transition transition) {
        return !getTransitions().contains(transition);
    }

    @Override
    public boolean hasTransition(final State oldState, final State newState, final Input input) {
        return !getTransitions(oldState, newState, input).isEmpty();
    }

    @Override
    public Set<Transition> getTransitions() {
        return new HashSet<>(getTransitions(null, null, null));
    }

    @Override
    public Set<Transition> getTransitions(final State oldState, final State newState, final Input input) {
        return new HashSet<>(findMatching(oldState, newState, input, transitions));
    }

    @Override
    public Set<State> getNewStates(final Set<State> currentStates, final Input input) {
        Set<State> newStates = new HashSet<>(currentStates);
        if (input != null) {
            newStates = applyTransition(newStates, input);
        }

        newStates = applyEpsilonTransition(newStates);

        return newStates;
    }

    /*
     * Finds transitions that match the specified criteria.
     */
    private Set<Transition> findMatching(final State oldState, final State newState, final Input input,
            final Set<FAutomatonTransition> transitions) {
        final Set<Transition> found = new HashSet<>();

        for (final Transition transition : transitions) {
            final boolean inputBool = (input != null) && !input.equals(transition.getInput());
            final boolean oldBool = (oldState != null) && !oldState.equals(transition.getOldState());
            final boolean newBool = (newState != null) && !newState.equals(transition.getNewState());

            if (inputBool || oldBool || newBool) {
                continue;
            }
            found.add(transition);
        }
        return found;
    }

    /*
     * Calculates the e-closure of the specified states.
     */
    private Set<State> applyEpsilonTransition(final Set<State> currentStates) {

        if (currentStates.isEmpty()) {
            return new HashSet<>();
        }

        if (epsilonTransitions.isEmpty()) {
            return currentStates;
        }

        final Set<State> returnStates = new HashSet<>(currentStates);

        while (true) {
            final Set<State> newStates = new HashSet<>();

            final int oldCount = returnStates.size();
            for (final State currentState : returnStates) {
                newStates
                        .addAll(extractStates(findMatching(currentState, null, null, epsilonTransitions), false, true));
            }

            returnStates.addAll(newStates);
            final int newCount = returnStates.size();
            if (newCount == oldCount) {
                break;
            }
        }

        return returnStates;
    }

    /*
     * Calculates the result of the transfer function from the specified state for the given input.
     */
    private Set<State> applyTransition(final Set<State> currentStates, final Input input) {
        if (currentStates.isEmpty()) {
            return new HashSet<>();
        }

        final Set<State> newStates = new HashSet<>();
        for (final State currentState : currentStates) {
            newStates.addAll(extractStates(findMatching(currentState, null, input, normalTransitions), false, true));
        }

        return newStates;
    }

}
