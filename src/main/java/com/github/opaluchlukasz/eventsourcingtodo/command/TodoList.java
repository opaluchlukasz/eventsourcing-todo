package com.github.opaluchlukasz.eventsourcingtodo.command;

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.*;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class TodoList {
    @AggregateIdentifier
    private String listName;
    private List<TodoItem> todos;

    @CommandHandler
    public TodoList(CreateTodoListCommand command) {
        apply(new TodoListCreatedEvent(command.getListName()));
    }

    @CommandHandler
    public void handle(AddTodoItemCommand command) {
        apply(new TodoItemAddedEvent(command.getListName(), command.getItem()));
    }

    @CommandHandler
    public void handle(MarkItemAsDoneCommand command) {
        apply(new TodoItemDoneEvent(command.getListName(), command.getItem()));
    }

    @EventSourcingHandler
    public void on(TodoListCreatedEvent todoListCreatedEvent) {
        listName = todoListCreatedEvent.getListName();
        todos = new LinkedList<>();
    }

    @EventSourcingHandler
    public void on(TodoItemAddedEvent event) {
        todos.stream()
                .filter(item -> item.getItem().equals(event.getItem())).findFirst()
                .ifPresent(item -> {
                    throw new IllegalArgumentException("Item already exists");
                });
        todos.add(new TodoItem(event.getItem(), false));
    }

    @EventSourcingHandler
    public void on(TodoItemDoneEvent event) {
        todos = todos.stream()
                .map(item -> item.getItem().equals(event.getItem()) ? item.done() : item)
                .collect(toList());
    }
}