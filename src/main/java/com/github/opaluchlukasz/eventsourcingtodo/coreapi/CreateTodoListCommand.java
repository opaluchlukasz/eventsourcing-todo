package com.github.opaluchlukasz.eventsourcingtodo.coreapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@RequiredArgsConstructor
@Getter
public class CreateTodoListCommand {
    @TargetAggregateIdentifier
    private final String listName;
}
