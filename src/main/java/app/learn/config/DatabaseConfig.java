package app.learn.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfig {
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("AutoLink-Persistence-Unit");

    private static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void shutdown() {
        emf.close();
    }
}
