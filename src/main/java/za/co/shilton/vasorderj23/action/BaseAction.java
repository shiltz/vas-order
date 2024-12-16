package za.co.shilton.vasorderj23.action;

import org.springframework.statemachine.action.Action;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

public abstract class BaseAction implements Action<State, Event> {


    public abstract Event getSuccessEvent();

    public abstract Event getErrorEvent();

}
