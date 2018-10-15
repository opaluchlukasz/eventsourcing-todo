package com.github.opaluchlukasz.eventsourcingtodo.command

import spock.lang.Specification

class TodoItemTest extends Specification {

    def 'should return done todo item'() {
        given:
        def todoItem = new TodoItem('foo', false)

        when:
        def doneItem = todoItem.done()

        then:
        doneItem.done
        doneItem.item == todoItem.item
    }
}
