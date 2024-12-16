package za.co.shilton.vasorderj23.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

@Service("orderValidationAction")
@Slf4j
public class OrderValidationAction extends BaseAction {

    @Override
    public void execute(StateContext<State, Event> stateContext) {
       var response = stateContext.getStateMachine()
                .sendEvent(Mono.defer(() -> Mono.just(MessageBuilder.withPayload(getErrorEvent()).build())))
                .blockFirst();
       log.info("Order validation response: {}", response);
    }

    @Override
    public Event getSuccessEvent() {
        return Event.ORDER_VALIDATION_PASSED;
    }

    @Override
    public Event getErrorEvent() {
        return Event.ORDER_VALIDATION_FAILED;
    }
}
