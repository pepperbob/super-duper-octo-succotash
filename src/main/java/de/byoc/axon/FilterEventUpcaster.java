package de.byoc.axon;

import org.axonframework.serialization.upcasting.Upcaster;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;

import java.util.stream.Stream;

public class FilterEventUpcaster implements EventUpcaster {

    private final Class<EventB> clazz;

    public FilterEventUpcaster(Class<EventB> eventBClass) {
        this.clazz = eventBClass;
    }

    @Override
    public Stream<IntermediateEventRepresentation> upcast(Stream<IntermediateEventRepresentation> in) {
        return in
                .filter(x -> !x.getType().getName().equals(clazz.getName()));
    }
}
