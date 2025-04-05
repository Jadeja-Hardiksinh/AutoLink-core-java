package app.learn.task;

import java.util.List;

public class TaskService {


    public void createTask(Task task) {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.add(task);
    }

    public boolean updateTask(Task task) {
        TaskDAO taskDAO = new TaskDAO();
        return taskDAO.update(task);
    }

    public Task findTaskById(Long id) {
        TaskDAO taskDAO = new TaskDAO();
        return taskDAO.find(id);
    }

    public List<Task> fetchAllTasks() {
        TaskDAO taskDAO = new TaskDAO();
        return taskDAO.findAll();
    }

    public boolean deleteTask(Long id) {
        TaskDAO taskDAO = new TaskDAO();
        return taskDAO.delete(id);
    }


}
