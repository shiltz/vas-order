package za.co.shilton.vasorderj23.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ExternalTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import reactor.core.publisher.Mono;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class VasOrderStatemachine extends EnumStateMachineConfigurerAdapter<State, Event> {

    @Autowired
    @Qualifier("orderValidationAction")
    private Action<State, Event> orderValidationAction;

    @Autowired
    @Qualifier("orderValidationError")
    private Action<State, Event> orderValidationError;

    @Override
    public void configure(StateMachineConfigurationConfigurer<State, Event> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions)
            throws Exception {
        var statemachineConfigurer = transitions.withExternal().source(State.START).target(State.ORDER_VALIDATION).event(Event.START).and();
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.ORDER_VALIDATION, State.QUOTE_VALIDATION, Event.ORDER_VALIDATION_PASSED, State.ORDER_VALIDATION_FAILURE, Event.ORDER_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.QUOTE_VALIDATION, State.LIMITS_VALIDATION, Event.QUOTE_VALIDATION_PASSED, State.QUOTE_VALIDATION_FAILURE, Event.QUOTE_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.LIMITS_VALIDATION, State.PAYMENT, Event.LIMIT_VALIDATION_PASSED, State.LIMIT_VALIDATION_FAILURE, Event.LIMIT_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.PAYMENT, State.PARTNER_ORDER, Event.PAYMENT_PASSED, State.PAYMENT_FAILURE, Event.PAYMENT_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.PARTNER_ORDER, State.HISTORY_UPDATE, Event.PARTNER_ORDER_PASSED, State.PARTNER_ORDER_FAILURE, Event.HISTORY_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.HISTORY_UPDATE, State.NOTIFICATION, Event.HISTORY_UPDATED, State.HISTORY_UPDATE_FAILURE, Event.HISTORY_FAILED);
        configure(statemachineConfigurer.withExternal(), State.NOTIFICATION, State.FINISH_ORDER_SUCCESS, Event.NOTIFICATION_PASSED, State.NOTIFICATION_FAILURE, Event.NOTIFICATION_FAILED);

    }

    private StateMachineTransitionConfigurer<State, Event> configure (ExternalTransitionConfigurer<State, Event> externalTransitionConfigurer, State fromState, State toState, Event eventSuccess, State unsuccessfulState, Event unsuccessfulEvent) throws Exception {
       return externalTransitionConfigurer.source(fromState).target(toState).event(eventSuccess)
                .and()
                .withExternal()
                .source(fromState).target(unsuccessfulState).event(unsuccessfulEvent)
                .and()
                .withExternal()
                .source(unsuccessfulState).target(State.FINISH_ORDER_FAILURE).event(Event.ORDER_FAILED)
                .and()
                .withExternal()
                .source(State.FINISH_ORDER_FAILURE).target(State.END).event(Event.ORDER_FAILED)
                .and()
                .withExternal()
                .source(State.FINISH_ORDER_SUCCESS).target(State.END).event(Event.ORDER_PASSED)
                .and();
    }

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states)
            throws Exception {
        states
                .withStates()
                .initial(State.START, null)
                .state(State.ORDER_VALIDATION, orderValidationAction)
                .state(State.ORDER_VALIDATION_FAILURE, orderValidationError)
                .state(State.FINISH_ORDER_FAILURE, executeFailureAction())
                .state(State.FINISH_ORDER_SUCCESS, executeSuccessAction())
                .states(EnumSet.allOf(State.class));
    }

    @Bean
    public Action<State, Event> executeSuccessAction() {
        return (StateContext<State, Event> stateContext) -> {
            stateContext.getStateMachine()
                    .sendEvent(Mono.defer(() -> Mono.just(MessageBuilder.withPayload(Event.ORDER_PASSED).build())))
                    .blockFirst();
        };
    }

    @Bean
    public Action<State, Event> executeFailureAction() {
        return (StateContext<State, Event> stateContext) -> {
            stateContext.getStateMachine()
                    .sendEvent(Mono.defer(() -> Mono.just(MessageBuilder.withPayload(Event.ORDER_FAILED).build())))
                    .blockFirst();
        };
    }

    @Bean
    public StateMachineListener<State, Event> listener() {
        return new StateMachineListenerAdapter<State, Event>() {

            @Override
            public void transitionStarted(Transition<State, Event> transition) {
                System.out.println("transitionStarted");
            }

            @Override
            public void eventNotAccepted(Message<Event> event) {
                System.out.println("Event not accepted: " + event);
            }

            @Override
            public void stateChanged(org.springframework.statemachine.state.State<State, Event> from, org.springframework.statemachine.state.State<State, Event> to) {
                System.out.println("State change to " + to.getId());
            }

            @Override
            public void stateMachineError(StateMachine<State, Event> stateMachine, Exception exception) {
                System.out.println("State machine error: " + exception.getMessage());
            }

        };
    }

}
