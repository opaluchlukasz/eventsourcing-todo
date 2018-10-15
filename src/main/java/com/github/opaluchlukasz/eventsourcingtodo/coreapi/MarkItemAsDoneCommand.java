package com.github.opaluchlukasz.eventsourcingtodo.coreapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@RequiredArgsConstructor
@Getter
public class MarkItemAsDoneCommand {
    @TargetAggregateIdentifier
    private final String listName;
    private final String item;
}
