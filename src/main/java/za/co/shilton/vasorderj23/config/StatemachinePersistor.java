package za.co.shilton.vasorderj23.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

@Slf4j
public class StatemachinePersistor implements StateMachinePersist<State, Event, Object> {

    @Override
    public void write(StateMachineContext<State, Event> stateMachineContext, Object s) throws Exception {
        log.info("Persisting state {}", stateMachineContext.getExtendedState());

    }

    @Override
    public StateMachineContext<State, Event> read(Object s) throws Exception {
        log.info("Reading state {}", s.toString());
        return null;
    }
}
