package hr.fer.zemris.ppj.lexical.analysis.automaton.transfer;

import hr.fer.zemris.ppj.lexical.analysis.automaton.interfaces.Input;
import hr.fer.zemris.ppj.lexical.analysis.automaton.interfaces.State;

public class DeterministicTransition extends NormalTransition {

    public DeterministicTransition(State oldState, State newState, Input input) {
        super(oldState, newState, input);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        FAutomatonTransition other = (FAutomatonTransition) obj;
        if (getInput() == null) {
            if (other.getInput() != null) {
                return false;
            }
        }
        else if (!getInput().equals(other.getInput())) {
            return false;
        }
        if (getOldState() == null) {
            if (other.getOldState() != null) {
                return false;
            }
        }
        else if (!getOldState().equals(other.getOldState())) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInput() == null) ? 0 : getInput().hashCode());
        result = prime * result + ((getOldState() == null) ? 0 : getOldState().hashCode());
        return result;
    }

}
