package com.kiselev.faces.dao;

import com.kiselev.faces.dao.entities.ProfileEntity;

import javax.persistence.*;

public class DAO {

    private static EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("faces");

    private static Long count = getCount();

    public static Long getId(ProfileEntity user) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (Long) manager.createQuery("" +
                    "SELECT user.id " +
                    "FROM ProfileEntity user " +
                    "WHERE username = :username AND password = :password")
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            manager.close();
        }
    }

    private static Long getCount() {
        EntityManager manager = factory.createEntityManager();
        try {
            return (Long) manager.createQuery("" +
                    "SELECT COUNT(*) " +
                    "FROM ProfileEntity user")
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static Long addUser(ProfileEntity user) throws PersistenceException {
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.merge(user);
        manager.getTransaction().commit();
        manager.close();
        return count = getCount();
    }

    public static ProfileEntity getProfile(Long id) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (ProfileEntity) manager
                    .createQuery("FROM ProfileEntity user " +
                            "WHERE id = :id").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static boolean isValidId(Long id) {
        return id >= 1 && id <= count;
    }
}
