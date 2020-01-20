
package de.byoc.axon;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateRoot;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@AggregateRoot
public class MyAggregate {

    @AggregateIdentifier
    String id;

    public MyAggregate() {
    }

    @CommandHandler
    public MyAggregate(CreateAggregate command) {
        apply(new Created(command.id));
    }

    @CommandHandler
    void justApplyEvent(DoAction cmd) {
        apply(cmd.event);
    }

    @EventSourcingHandler
    void on(Created event) {
        this.id = event.id.toString();
    }

}
