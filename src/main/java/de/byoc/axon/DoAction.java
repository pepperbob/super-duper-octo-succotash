package de.byoc.axon;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class DoAction {

    @TargetAggregateIdentifier
    final String id;

    final Object event;

    public DoAction(String id, Object what) {
        this.id = id;
        this.event = what;
    }

}
