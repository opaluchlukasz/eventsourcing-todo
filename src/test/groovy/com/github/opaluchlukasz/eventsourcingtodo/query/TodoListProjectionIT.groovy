package com.github.opaluchlukasz.eventsourcingtodo.query

import com.github.opaluchlukasz.eventsourcingtodo.EventSourcingTodoApplication
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.CreateTodoListCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static java.util.concurrent.TimeUnit.SECONDS
import static org.axonframework.queryhandling.responsetypes.ResponseTypes.multipleInstancesOf
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EventSourcingTodoApplication)
@ContextConfiguration
class TodoListProjectionIT extends Specification {
    @Autowired private CommandGateway commandGateway
    @Autowired private QueryGateway queryGateway

    def 'should answer fetch list names query when there are list defined'() {
        given:
        def listName = 'foo'
        commandGateway.sendAndWait(new CreateTodoListCommand(listName), 5, SECONDS)

        when:
        def query = queryGateway.query(new Query.FetchListNamesQuery(), multipleInstancesOf(String))

        then:
        query.get() == [listName]
    }

    def 'should return single todo list'() {
        given:
        def listName = 'bar'
        commandGateway.sendAndWait(new CreateTodoListCommand(listName), 5, SECONDS)

        when:
        def query = queryGateway.query(new Query.FetchTodoListQuery(listName), ReadTodoList)

        then:
        query.get() == new ReadTodoList(listName)
    }
}
