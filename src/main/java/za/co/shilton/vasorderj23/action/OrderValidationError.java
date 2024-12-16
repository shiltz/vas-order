package za.co.shilton.vasorderj23.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

@Service("orderValidationError")
@Slf4j
public class OrderValidationError extends BaseAction {

    @Override
    public Event getSuccessEvent() {
        return Event.ORDER_FAILED;
    }

    @Override
    public Event getErrorEvent() {
        return Event.ORDER_FAILED;
    }

    @Override
    public void execute(StateContext<State, Event> stateContext) {
        log.info("Order validation error");
        var response = stateContext.getStateMachine()
                .sendEvent(Mono.defer(() -> Mono.just(MessageBuilder.withPayload(getErrorEvent()).build())))
                .blockFirst();
        log.info("Order validation response: {}", response);


    }
}
