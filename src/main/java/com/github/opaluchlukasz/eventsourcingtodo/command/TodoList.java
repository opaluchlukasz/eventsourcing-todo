package com.github.opaluchlukasz.eventsourcingtodo.command;

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.AddTodoItemCommand;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.CreateTodoListCommand;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemAddedEvent;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoListCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.commandhandling.model.ForwardMatchingInstances;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.LinkedList;
import java.util.List;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class TodoList {
    @AggregateIdentifier private String listName;
    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private List<TodoItem> todos;

    @CommandHandler
    public TodoList(CreateTodoListCommand command) {
        apply(new TodoListCreatedEvent(command.getListName()));
    }

    @CommandHandler
    public void handle(AddTodoItemCommand command) {
        todos.stream()
                .filter(item -> item.getItem().equals(command.getItem())).findFirst()
                .ifPresent(item -> {
                    throw new IllegalArgumentException("Item already exists");
                });
        apply(new TodoItemAddedEvent(command.getListName(), command.getItem()));
    }

    @EventSourcingHandler
    public void on(TodoListCreatedEvent todoListCreatedEvent) {
        listName = todoListCreatedEvent.getListName();
        todos = new LinkedList<>();
    }

    @EventSourcingHandler
    public void on(TodoItemAddedEvent event) {
        todos.add(new TodoItem(event.getItem(), false));
    }
}