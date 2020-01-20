package de.byoc.axon.commands;

import de.byoc.axon.*;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class MyAggregateTest {

  private static Logger log = LoggerFactory.getLogger(MyAggregateTest.class);

  Configuration axon;
  private EntityTransaction trans;
  private EntityManager em;

  @Before
  public void setup() {
    em = Persistence.createEntityManagerFactory("eventstore").createEntityManager();
    trans = em.getTransaction();

    axon = DefaultConfigurer.defaultConfiguration()
            .configureEmbeddedEventStore(c ->
                    JpaEventStorageEngine.builder()
                            .upcasterChain(c.upcasterChain())
                            .entityManagerProvider(() -> em)
                            .transactionManager(NoTransactionManager.instance())
                            .build())
            .configureAggregate(MyAggregate.class)
            .registerEventUpcaster(c -> new FilterEventUpcaster(EventB.class))
            .start();
  }

  @After
  public void after() {
    axon.shutdown();
  }

  @Test
  public void dupplicatedSequences() {
    trans.begin();

    CreateAggregate command = new CreateAggregate("Hello!");
    axon.commandGateway().sendAndWait(command);
    axon.commandGateway().sendAndWait(new DoAction(command.id, new EventA()));
    axon.commandGateway().sendAndWait(new DoAction(command.id, new EventB()));
    axon.commandGateway().sendAndWait(new DoAction(command.id, new EventC()));

    em.flush();
    List<DomainEventEntry> list = em.createQuery("from DomainEventEntry d", DomainEventEntry.class).getResultList();

    list.stream().forEach(x -> log.info("{}, {}", x.getType(), x.getSequenceNumber()));

    trans.commit();

  }

}
