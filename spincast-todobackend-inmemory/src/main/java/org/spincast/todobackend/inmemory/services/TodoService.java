package org.spincast.todobackend.inmemory.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spincast.core.json.IJsonObject;
import org.spincast.todobackend.inmemory.models.ITodo;
import org.spincast.todobackend.inmemory.repositories.ITodoRepository;

import com.google.inject.Inject;

/**
 * Todo's service implementation.
 */
public class TodoService implements ITodoService {

    private final ITodoRepository todoRepository;

    /**
     * Constructor
     */
    @Inject
    public TodoService(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    protected ITodoRepository getTodoRepository() {
        return this.todoRepository;
    }

    @Override
    public List<ITodo> getAllTodos() {

        Collection<ITodo> todos = getTodoRepository().getAllTodos();

        //==========================================
        // Sort the Todos by their order.
        //==========================================
        List<ITodo> todosOrdered = sortTodosByOrder(todos);

        return todosOrdered;
    }

    /**
     * Sorts the Todos by their order.
     */
    protected List<ITodo> sortTodosByOrder(Collection<ITodo> todos) {
        if(todos == null) {
            return null;
        }

        List<ITodo> todosList = new ArrayList<ITodo>(todos);
        Collections.sort(todosList, new Comparator<ITodo>() {

            @Override
            public int compare(ITodo todo1, ITodo todo2) {
                return Integer.compare(todo1.getOrder(), todo2.getOrder());
            }
        });

        return todosList;
    }

    @Override
    public ITodo addTodo(ITodo newTodo) {
        ITodo todo = getTodoRepository().addTodo(newTodo);

        //==========================================
        // Return the Todo with its generated id.
        //==========================================
        return todo;
    }

    @Override
    public void deleteAllTodos() {
        getTodoRepository().deleteAllTodos();
    }

    @Override
    public ITodo getTodo(int todoId) {
        ITodo todo = getTodoRepository().getTodo(todoId);
        return todo;
    }

    @Override
    public ITodo patchTodo(ITodo todo, IJsonObject jsonPatch) {

        if(todo == null || jsonPatch == null) {
            return todo;
        }

        if(jsonPatch.isKeyExists("title")) {
            todo.setTitle(jsonPatch.getString("title"));
        }

        if(jsonPatch.isKeyExists("completed")) {
            todo.setCompleted(jsonPatch.getBoolean("completed"));
        }

        if(jsonPatch.isKeyExists("order")) {
            todo.setOrder(jsonPatch.getInteger("order"));
        }

        return todo;
    }

    @Override
    public void deleteTodo(int todoId) {
        getTodoRepository().deleteTodo(todoId);
    }

}