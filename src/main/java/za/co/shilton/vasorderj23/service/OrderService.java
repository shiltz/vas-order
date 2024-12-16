package za.co.shilton.vasorderj23.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
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
        stateMachine.getStateMachineAccessor().withRegion()
                        .addStateMachineInterceptor(new StateMachineInterceptorAdapter<State, Event>() {

                            @Override
                            public void postStateChange(org.springframework.statemachine.state.State<State, Event> state, Message<Event> message, Transition<State, Event> transition, StateMachine<State, Event> stateMachine, StateMachine<State, Event> stateMachine1) {

                            }

                            @Override
                            public Exception stateMachineError(StateMachine<State, Event> stateMachine, Exception e) {
                                return null;
                            }
                        });
        stateMachine.startReactively().blockOptional();
        var response = stateMachine.sendEvent(Mono
                        .just(MessageBuilder
                                .withPayload(Event.START)
                                .build()))
                .blockFirst();

        log.info(response.getResultType().toString());

    }
}
