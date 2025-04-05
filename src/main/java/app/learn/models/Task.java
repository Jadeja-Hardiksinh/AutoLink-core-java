package app.learn.models;

import app.learn.enums.TaskPriority;
import app.learn.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    private String title;
    private String description;
    @Column(name = "category")
    private String taskCategory;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Column(name = "priority")
    @Enumerated(value = EnumType.STRING)
    private TaskPriority taskPriority = TaskPriority.HIGH;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private TaskStatus taskStatus = TaskStatus.COMPLETED;

    public Task() {

    }

    public Task(String title, String description, String taskCategory, TaskPriority taskPriority, LocalDate dueDate, TaskStatus taskStatus) {

        this.title = title;
        this.description = description;
        this.taskCategory = taskCategory;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
