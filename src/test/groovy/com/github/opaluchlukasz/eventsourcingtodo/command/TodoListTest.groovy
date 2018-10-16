package com.github.opaluchlukasz.eventsourcingtodo.command

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.*
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork
import org.axonframework.test.aggregate.AggregateTestFixture
import spock.lang.Specification

import java.util.function.Function
import java.util.function.Predicate

import static org.axonframework.test.matchers.Matchers.matches

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

        and:
        TodoList t = aggregateState()
        t.todos.size() == 2
    }

    def 'should not add todo item when aggregate does not exist'() {
        given:
        fixture.given()
                .when(new AddTodoItemCommand(LIST_NAME, 'foo'))
                .expectException(AggregateNotFoundException.class)
    }

    def 'should not add another todo item with same name'() {
        given:
        def item = 'foo'

        expect:
        fixture.given(new TodoListCreatedEvent(LIST_NAME), new TodoItemAddedEvent(LIST_NAME, item))
                .when(new AddTodoItemCommand(LIST_NAME, item))
                .expectException(matches([ test: { it.cause.class == IllegalArgumentException &&
                        it.cause.message == 'Item already exists' }] as Predicate))
    }

    def 'should not mark item as done when aggregate does not exist'() {
        given:
        fixture.given()
                .when(new MarkItemAsDoneCommand(LIST_NAME, 'non-existing'))
                .expectException(AggregateNotFoundException.class)
    }

    def 'should mark todo item as done'() {
        given:
        def item = 'bar'

        expect:
        fixture.given(
                new TodoListCreatedEvent(LIST_NAME),
                new TodoItemAddedEvent(LIST_NAME, 'foo'),
                new TodoItemAddedEvent(LIST_NAME, 'bar'))
                .when(new MarkItemAsDoneCommand(LIST_NAME, item))
                .expectEvents(new TodoItemDoneEvent(LIST_NAME, item))

        and:
        TodoList t = aggregateState()
        !t.todos.find { it.item == 'foo' }.done
        t.todos.find { it.item == item }.done
    }

    private TodoList aggregateState() {
        CurrentUnitOfWork.set(new DefaultUnitOfWork())
        fixture.getRepository().load(LIST_NAME).invoke([apply: { it }] as Function)
    }
}
