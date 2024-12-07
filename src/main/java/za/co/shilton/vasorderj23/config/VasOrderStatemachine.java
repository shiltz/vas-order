package za.co.shilton.vasorderj23.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ExternalTransitionConfigurer;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class VasOrderStatemachine extends EnumStateMachineConfigurerAdapter<State, Event> {

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions)
            throws Exception {
        var statemachineConfigurer = configure(transitions.withExternal(), State.ORDER_VALIDATION, State.QUOTE_VALIDATION, Event.ORDER_VALIDATION_PASSED, State.ORDER_VALIDATION_FAILURE, Event.ORDER_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.QUOTE_VALIDATION, State.LIMITS_VALIDATION, Event.QUOTE_VALIDATION_PASSED, State.QUOTE_VALIDATION_FAILURE, Event.QUOTE_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.LIMITS_VALIDATION, State.PAYMENT, Event.LIMIT_VALIDATION_PASSED, State.LIMIT_VALIDATION_FAILURE, Event.LIMIT_VALIDATION_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.PAYMENT, State.PARTNER_ORDER, Event.PAYMENT_PASSED, State.PAYMENT_FAILURE, Event.PAYMENT_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.PARTNER_ORDER, State.HISTORY_UPDATE, Event.PARTNER_ORDER_PASSED, State.PARTNER_ORDER_FAILURE, Event.HISTORY_FAILED);
        statemachineConfigurer = configure(statemachineConfigurer.withExternal(), State.HISTORY_UPDATE, State.NOTIFICATION, Event.HISTORY_UPDATED, State.HISTORY_UPDATE_FAILURE, Event.HISTORY_FAILED);
        configure(statemachineConfigurer.withExternal(), State.NOTIFICATION, State.FINISH_ORDER_SUCCESS, Event.NOTIFICATION_PASSED, State.NOTIFICATION_FAILURE, Event.NOTIFICATION_FAILED);
//
//        transitions
//                .withExternal()
//                .source(State.ORDER_VALIDATION).target(State.QUOTE_VALIDATION).event(Event.ORDER_VALIDATION_PASSED)
//                .and()
//                    .withExternal()
//                    .source(State.ORDER_VALIDATION).target(State.ORDER_VALIDATION_FAILURE).event(Event.ORDER_VALIDATION_FAILED)
//                    .and()
//                    .withExternal()
//                    .source(State.ORDER_VALIDATION_FAILURE).target(State.FINISH_ORDER_FAILURE).event(Event.ORDER_FAILED)
//                .and()
//                .withExternal()
//                .source(State.QUOTE_VALIDATION).target(State.LIMITS_VALIDATION).event(Event.QUOTE_VALIDATION_PASSED)
//                .and()
//                    .withExternal()
//                    .source(State.QUOTE_VALIDATION).target(State.QUOTE_VALIDATION_FAILURE).event(Event.QUOTE_VALIDATION_FAILED)
//                    .and()
//                    .withExternal()
//                    .source(State.QUOTE_VALIDATION_FAILURE).target(State.FINISH_ORDER_FAILURE).event(Event.ORDER_FAILED);
    }

    StateMachineTransitionConfigurer<State, Event> configure (ExternalTransitionConfigurer<State, Event> externalTransitionConfigurer, State fromState, State toState, Event eventSuccess, State unsuccessfulState, Event unsuccessfulEvent) throws Exception {
       return externalTransitionConfigurer.source(fromState).target(toState).event(eventSuccess)
                .and()
                .withExternal()
                .source(fromState).target(unsuccessfulState).event(unsuccessfulEvent)
                .and()
                .withExternal()
                .source(unsuccessfulState).target(State.FINISH_ORDER_FAILURE).event(Event.ORDER_FAILED)
                .and();
    }

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states)
            throws Exception {
        states
                .withStates()
                .initial(State.ORDER_VALIDATION)
                .states(EnumSet.allOf(State.class));
    }

}
