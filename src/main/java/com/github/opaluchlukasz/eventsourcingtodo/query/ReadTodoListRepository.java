package com.github.opaluchlukasz.eventsourcingtodo.query;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class ReadTodoListRepository {
    private final ValueOperations<String, ReadTodoList> singleListOperations;
    private final ValueOperations<String, List<String>> listNamesOperations;

    public ReadTodoListRepository(RedisTemplate redisTemplate) {
        singleListOperations = redisTemplate.opsForValue();
        listNamesOperations = redisTemplate.opsForValue();
    }

    public List<String> findListNames() {
        listNamesOperations.setIfAbsent("list", new LinkedList<>());
        return listNamesOperations.get("list");
    }

    public void saveListNames(List<String> list) {
        listNamesOperations.set("list", list);
    }

    public void saveTodoList(ReadTodoList list) {
        singleListOperations.set(list.getListName(), list);
    }

    public ReadTodoList findTodoList(String listName) {
        return singleListOperations.get(listName);
    }
}
