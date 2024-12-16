package za.co.shilton.vasorderj23.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import za.co.shilton.vasorderj23.common.statemachine.state.Event;
import za.co.shilton.vasorderj23.common.statemachine.state.State;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final StateMachineFactory<State, Event> factory;

    public void createOrder() {
        StateMachine<State,Event> stateMachine = factory.getStateMachine(UUID.randomUUID().toString());
        stateMachine.startReactively().blockOptional();
        var response = stateMachine.sendEvent(Mono
                        .just(MessageBuilder
                                .withPayload(Event.START)
                                .build()))
                .blockFirst();

        log.info(response.getResultType().toString());

    }
}
