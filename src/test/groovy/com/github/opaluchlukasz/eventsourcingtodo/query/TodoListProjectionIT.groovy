package com.github.opaluchlukasz.eventsourcingtodo.query

import com.github.opaluchlukasz.eventsourcingtodo.EventSourcingTodoApplication
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.CreateTodoListCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.axonframework.queryhandling.responsetypes.ResponseTypes.multipleInstancesOf
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EventSourcingTodoApplication)
@ContextConfiguration
class TodoListProjectionIT extends Specification {
    private static final String LIST_NAME = 'abc'
    @Autowired private CommandGateway commandGateway
    @Autowired private QueryGateway queryGateway

    def 'should answer fetch list names query when there no list defined'() {
        when:
        def query = queryGateway.query(new Query.FetchListNamesQuery(), multipleInstancesOf(String))

        then:
        query.get() == []
    }

    def 'should answer fetch list names query when there are list defined'() {
        given:
        commandGateway.send(new CreateTodoListCommand(LIST_NAME))

        when:
        def query = queryGateway.query(new Query.FetchListNamesQuery(), multipleInstancesOf(String))

        then:
        query.get() == [LIST_NAME]
    }

    def 'should return single todo list'() {
        given:
        commandGateway.send(new CreateTodoListCommand(LIST_NAME))

        when:
        def query = queryGateway.query(new Query.FetchTodoListQuery(LIST_NAME), ReadTodoList)

        then:
        query.get() == new ReadTodoList(LIST_NAME)
    }
}
