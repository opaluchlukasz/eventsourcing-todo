package com.github.opaluchlukasz.eventsourcingtodo.command;

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemDoneEvent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.axonframework.commandhandling.model.EntityId;
import org.axonframework.eventsourcing.EventSourcingHandler;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TodoItem {
    @EntityId(routingKey = "item") public final String item;
    public boolean done;

    @EventSourcingHandler
    public void on(TodoItemDoneEvent event) {
        done = true;
    }
}
