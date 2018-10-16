package com.github.opaluchlukasz.eventsourcingtodo.command

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemDoneEvent
import spock.lang.Specification

class TodoItemTest extends Specification {

    def 'should mark item as done'() {
        given:
        def todoItem = new TodoItem('foo', false)

        when:
        todoItem.on(new TodoItemDoneEvent('a', 'foo'))

        then:
        todoItem.done
    }
}
