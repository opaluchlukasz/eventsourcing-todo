package com.github.opaluchlukasz.eventsourcingtodo.query

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemAddedEvent
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoListCreatedEvent
import spock.lang.Specification
import spock.lang.Subject

class TodoListProjectionTest extends Specification {
    private static final String LIST_NAME_1 = 'foo'
    private static final String LIST_NAME_2 = 'bar'

    private ReadTodoListRepository readTodoListRepository = Mock(ReadTodoListRepository)
    @Subject private TodoListProjection todoListProjection

    def setup() {
        todoListProjection = new TodoListProjection(readTodoListRepository)
    }

    def 'should handle TodoListCreated event'() {
        given:
        readTodoListRepository.findListNames() >> [LIST_NAME_1]

        when:
        todoListProjection.on(new TodoListCreatedEvent(LIST_NAME_2))

        then:
        1 * readTodoListRepository.saveListNames([LIST_NAME_1, LIST_NAME_2])
    }

    def 'should handle TodoItemAdded event'() {
        given:
        def todoItem = new ReadTodoList.Item(LIST_NAME_2)
        readTodoListRepository.findTodoList(LIST_NAME_1) >> new ReadTodoList(LIST_NAME_1, [todoItem].toList())

        when:
        todoListProjection.on(new TodoItemAddedEvent(LIST_NAME_1, 'bas'))

        then:
        1 * readTodoListRepository.saveTodoList(new ReadTodoList(LIST_NAME_1, [todoItem, new ReadTodoList.Item('bas')]))
    }

    def 'should answer FetchListNames query'() {
        given:
        def expectedList = [LIST_NAME_1, LIST_NAME_2]

        when:
        def returnedList = todoListProjection.handle(new Query.FetchListNamesQuery())

        then:
        returnedList == expectedList
        1 * readTodoListRepository.findListNames() >> expectedList
    }

    def 'should answer FetchTodoList query'() {
        given:
        def expectedTodoList = new ReadTodoList(LIST_NAME_1)

        when:
        def returnedTodoList = todoListProjection.handle(new Query.FetchTodoListQuery(LIST_NAME_1))

        then:
        returnedTodoList == expectedTodoList
        1 * readTodoListRepository.findTodoList(LIST_NAME_1) >> expectedTodoList
    }
}
