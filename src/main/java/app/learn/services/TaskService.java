package app.learn.services;

import app.learn.dao.TaskDAO;
import app.learn.models.Task;

import java.util.List;

public class TaskService {
    TaskDAO taskDAO = new TaskDAO();

    public void createTask(Task task) {
        taskDAO.add(task);
    }

    public boolean updateTask(Task task) {
        return taskDAO.update(task);
    }

    public Task findTaskById(Long id) {
        return taskDAO.find(id);
    }

    public List<Task> fetchAllTasks() {
        return taskDAO.findAll();
    }

    public boolean deleteTask(Long id) {

        return taskDAO.delete(id);
    }


}
