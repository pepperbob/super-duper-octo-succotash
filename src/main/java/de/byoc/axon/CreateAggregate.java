
package de.byoc.axon;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class CreateAggregate {

    @TargetAggregateIdentifier
    public final String id = UUID.randomUUID().toString();

    final String message;

    public CreateAggregate(String message) {
        this.message = message;
    }

}
