package za.co.shilton.vasorderj23.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

@SpringBootTest
class VasOrderStatemachineTest {

    @Autowired
    private StateMachine<State, Event> stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine.startReactively().blockOptional();
    }

    @AfterEach
    void tearDown() {
        stateMachine.stopReactively().blockOptional();
    }

    @Test
    public void testSuccessfulOrderFlowOrderValidationFailure() throws Exception {
        StateMachineTestPlan<State, Event> plan =
                StateMachineTestPlanBuilder.<State, Event>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(stateMachine)
                        .step()
                        .expectStates(State.ORDER_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_VALIDATION_FAILED)
                        .expectStateChanged(1)
                        .expectStates(State.ORDER_VALIDATION_FAILURE)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.FINISH_ORDER_FAILURE)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void testSuccessfulOrderFlow() throws Exception {
        StateMachineTestPlan<State, Event> plan =
                StateMachineTestPlanBuilder.<State, Event>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(stateMachine)
                        .step()
                        .expectState(State.START)
                        .and()
                        .step()
                        .sendEvent(Event.START)
                        .expectStateChanged(1)
                        .expectStates(State.ORDER_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectStates(State.QUOTE_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.QUOTE_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.LIMITS_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.LIMIT_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.PAYMENT)
                        .and()
                        .step()
                        .sendEvent(Event.PAYMENT_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.PARTNER_ORDER)
                        .and()
                        .step()
                        .sendEvent(Event.PARTNER_ORDER_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.HISTORY_UPDATE)
                        .and()
                        .step()
                        .sendEvent(Event.HISTORY_UPDATED)
                        .expectStateChanged(1)
                        .expectState(State.NOTIFICATION)
                        .and()
                        .step()
                        .sendEvent(Event.NOTIFICATION_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.FINISH_ORDER_SUCCESS)
                        .and()
                        .build();
        plan.test();
    }



    @Test
    public void testSuccessfulOrderFlowQuoteValidationFailure() throws Exception {
        StateMachineTestPlan<State, Event> plan =
                StateMachineTestPlanBuilder.<State, Event>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(stateMachine)
                        .step()
                        .expectStates(State.ORDER_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectStates(State.QUOTE_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.QUOTE_VALIDATION_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.QUOTE_VALIDATION_FAILURE)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.FINISH_ORDER_FAILURE)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void testSuccessfulOrderFlowLimitValidationFailure() throws Exception {
        StateMachineTestPlan<State, Event> plan =
                StateMachineTestPlanBuilder.<State, Event>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(stateMachine)
                        .step()
                        .expectStates(State.ORDER_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectStates(State.QUOTE_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.QUOTE_VALIDATION_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.QUOTE_VALIDATION_FAILURE)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.FINISH_ORDER_FAILURE)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void testSuccessfulOrderFlowPaymentValidationFailure() throws Exception {
        StateMachineTestPlan<State, Event> plan =
                StateMachineTestPlanBuilder.<State, Event>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(stateMachine)
                        .step()
                        .expectStates(State.ORDER_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectStates(State.QUOTE_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.QUOTE_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.LIMITS_VALIDATION)
                        .and()
                        .step()
                        .sendEvent(Event.LIMIT_VALIDATION_PASSED)
                        .expectStateChanged(1)
                        .expectState(State.PAYMENT)
                        .and()
                        .step()
                        .sendEvent(Event.PAYMENT_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.PAYMENT_FAILURE)
                        .and()
                        .step()
                        .sendEvent(Event.ORDER_FAILED)
                        .expectStateChanged(1)
                        .expectState(State.FINISH_ORDER_FAILURE)
                        .and()
                        .build();
        plan.test();
    }

}