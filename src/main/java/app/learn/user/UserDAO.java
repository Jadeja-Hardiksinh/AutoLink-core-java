package app.learn.user;

import app.learn.common.config.DatabaseConfig;
import app.learn.common.util.HashUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class UserDAO {
    public boolean add(User user) {
        user.setPassword(HashUtil.createHash(user.getPassword()));
        String query = "Select t from User t where t.email = :email";
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            List<User> users = em.createQuery(query, User.class).setParameter("email", user.getEmail()).getResultList();
            if (!users.isEmpty()) {
                transaction.commit();
                em.close();
                return false;
            }
            em.persist(user);
            transaction.commit();
            em.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
    }

    public User findByEmail(String email) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            return em.createNamedQuery("User.fetchUserByEmail", User.class).setParameter("email", email).getSingleResultOrNull();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
  
    }


}
