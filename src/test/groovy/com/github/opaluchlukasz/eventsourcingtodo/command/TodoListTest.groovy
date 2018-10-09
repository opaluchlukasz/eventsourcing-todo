package com.github.opaluchlukasz.eventsourcingtodo.command

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.AddTodoItemCommand
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.CreateTodoListCommand
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemAddedEvent
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoListCreatedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import spock.lang.Specification

class TodoListTest extends Specification {
    private static final String LIST_NAME = 'to do'
    private AggregateTestFixture<TodoList> fixture

    def setup() {
        fixture = new AggregateTestFixture(TodoList)
    }

    def 'should create todo list'() {
        expect:
        fixture.givenNoPriorActivity()
                .when(new CreateTodoListCommand(LIST_NAME))
                .expectEvents(new TodoListCreatedEvent(LIST_NAME))
    }

    def 'should add a todo item'() {
        given:
        def item = 'foo'

        expect:
        fixture.given(new TodoListCreatedEvent(LIST_NAME))
                .when(new AddTodoItemCommand(LIST_NAME, item))
                .expectEvents(new TodoItemAddedEvent(LIST_NAME, item))
    }

    def 'should add another todo item'() {
        given:
        def item = 'bar'

        expect:
        fixture.given(new TodoListCreatedEvent(LIST_NAME), new TodoItemAddedEvent(LIST_NAME, 'foo'))
                .when(new AddTodoItemCommand(LIST_NAME, item))
                .expectEvents(new TodoItemAddedEvent(LIST_NAME, item))
    }
}
