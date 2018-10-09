package com.github.opaluchlukasz.eventsourcingtodo;

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.AddTodoItemCommand;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.CreateTodoListCommand;
import com.github.opaluchlukasz.eventsourcingtodo.query.Query.FetchListNamesQuery;
import com.github.opaluchlukasz.eventsourcingtodo.query.Query.FetchTodoListQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@SpringBootApplication
public class EventSourcingTodoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(EventSourcingTodoApplication.class, args);

        // Just to verify
        CommandGateway commandBus = run.getBean(CommandGateway.class);
        QueryGateway queryBus = run.getBean(QueryGateway.class);

        commandBus.send(asCommandMessage(new CreateTodoListCommand("foo")));
        commandBus.send(asCommandMessage(new AddTodoItemCommand("foo", "aaa")));

        queryBus.query(new FetchListNamesQuery(), ResponseTypes.multipleInstancesOf(String.class))
                .thenAccept(System.out::println);
        queryBus.query(new FetchTodoListQuery("foo"), ResponseTypes.multipleInstancesOf(String.class))
                .thenAccept(System.out::println);
    }
}
