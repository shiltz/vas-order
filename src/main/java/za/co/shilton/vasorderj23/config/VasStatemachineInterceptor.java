package za.co.shilton.vasorderj23.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.persist.AbstractPersistingStateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;

@Slf4j
public class VasStatemachineInterceptor<S, E, T extends String> extends AbstractPersistingStateMachineInterceptor<S, E, T> implements StateMachineRuntimePersister<S, E, T> {


    @Override
    public void preStateChange(org.springframework.statemachine.state.State<S, E> state, Message<E> message, Transition<S, E> transition, StateMachine<S, E> stateMachine, StateMachine<S, E> rootStateMachine) {

    }

    @Override
    public void postStateChange(org.springframework.statemachine.state.State<S, E> state,
                                Message<E> message,
                                Transition<S, E> transition,
                                StateMachine<S, E> stateMachine,
                                StateMachine<S, E> stateMachine1) {
        if (state != null) {
            try {
                this.write(this.buildStateMachineContext(stateMachine, stateMachine1, state, message), (T) stateMachine.getId());
            } catch (Exception e) {
                throw new StateMachineException("Unable to persist stateMachineContext", e);
            }
        }
    }

    @Override
    public Exception stateMachineError(StateMachine<S, E> stateMachine, Exception e) {
        return null;
    }

    @Override
    public void write(StateMachineContext<S, E> stateMachineContext, T t) throws Exception {
       //todo: add logic to save
        log.info("saving");
    }

    @Override
    public StateMachineContext<S, E> read(T t) throws Exception {
        log.info("reading");
        return null;
    }

    @Override
    public StateMachineInterceptor<S, E> getInterceptor() {
        return this;
    }
}
