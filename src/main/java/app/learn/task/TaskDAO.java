package app.learn.task;

import app.learn.common.config.DatabaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class TaskDAO {
    public void add(Task task) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(task);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();

        } finally {
            em.close();
        }


    }

    public boolean update(Task task) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Task existingTask = find(task.getId());
            if (existingTask != null) {
                if (task.getDescription() != null) {
                    existingTask.setDescription(task.getDescription());
                }
                if (task.getTitle() != null) {
                    existingTask.setTitle(task.getTitle());
                }
                if (task.getDueDate() != null) {
                    existingTask.setDueDate(task.getDueDate());
                }
                if (task.getTaskPriority() != null) {
                    existingTask.setTaskPriority(task.getTaskPriority());
                }
                if (task.getTaskStatus() != null) {
                    existingTask.setTaskStatus(task.getTaskStatus());
                }
                if (task.getTaskCategory() != null) {
                    existingTask.setTaskCategory(task.getTaskCategory());
                }
                em.merge(existingTask);
                transaction.commit();
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public Task find(Long id) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Task task = em.find(Task.class, id);
            transaction.commit();
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return null;
        } finally {
            em.close();
        }

    }

    public List<Task> findAll() {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            String query = "Select t from Task t";
            transaction.begin();
            List<Task> tasks = em.createQuery(query, Task.class).getResultList();
            transaction.commit();
            em.close();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            em.close();
        }
        return null;
    }

    public boolean delete(Long id) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        Task task = em.find(Task.class, id);
        if (task != null) {
            try {
                transaction.begin();
                em.remove(task);
                transaction.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                return false;

            }
        }
        return false;

    }
}
