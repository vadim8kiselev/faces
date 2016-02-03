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
                    .setParameter("username", user.getUserName())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
        } catch (NoResultException error) {
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
        } catch (NoResultException error) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static Long addUser(ProfileEntity user) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(user);
            manager.getTransaction().commit();
            return count = getCount();
        } catch (PersistenceException error) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static ProfileEntity getProfile(Long id) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (ProfileEntity) manager
                    .createQuery("" +
                            "SELECT user " +
                            "FROM ProfileEntity user " +
                            "WHERE id = :id").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException error) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static ProfileEntity getProfile(String urlName) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (ProfileEntity) manager
                    .createQuery("" +
                            "SELECT user " +
                            "FROM ProfileEntity user " +
                            "WHERE urlname = :urlName")
                    .setParameter("urlName", urlName)
                    .getSingleResult();

        } catch (NoResultException error) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static void updateProfile(ProfileEntity profile) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.merge(profile);
            manager.getTransaction().commit();

        } catch (NoResultException error) {
            error.printStackTrace();
        } finally {
            manager.close();
        }
    }

    public static void deleteProfile(Long id) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.createQuery("" +
                    "DELETE FROM ProfileEntity user" +
                    " WHERE user.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        } finally {
            manager.close();
        }
    }

    public static boolean isValidId(Long id) {
        return id >= 1 && id <= count;
    }

    public static boolean isValidUrlName(String urlName) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.createQuery("" +
                    "SELECT user.urlname " +
                    "FROM ProfileEntity user " +
                    "WHERE urlname = :urlName")
                    .setParameter("urlName", urlName)
                    .getSingleResult();
            return true;
        } catch (NoResultException error) {
            return false;
        } finally {
            manager.close();
        }
    }
}
