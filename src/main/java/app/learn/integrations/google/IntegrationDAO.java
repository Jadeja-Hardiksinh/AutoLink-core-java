package app.learn.integrations.google;

import app.learn.common.config.DatabaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class IntegrationDAO {
    public void add(Integration integration) {
        EntityManager em = DatabaseConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ;
            em.persist(integration);
            transaction.commit();
            em.close();
        } catch (Exception e) {
            transaction.rollback();
            em.close();
            e.printStackTrace();
        }
    }

}
