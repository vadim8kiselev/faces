package com.kiselev.faces.dao;

import com.kiselev.faces.dao.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class DAO {

    private static EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("faces");

    private static Long count = getCount();

    public static boolean checkUsername(UserEntity user) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.createQuery("" +
                    "SELECT user.username " +
                    "FROM UserEntity user " +
                    "WHERE username = :name")
                    .setParameter("name", user.getUsername())
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } finally {
            manager.close();
        }
    }

    public static Long getId(UserEntity user) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (Long) manager.createQuery("" +
                    "SELECT user.id " +
                    "FROM UserEntity user " +
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
                    "FROM UserEntity user")
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            manager.close();
        }
    }

    public static Long addUser(UserEntity user) {
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.merge(user);
        manager.getTransaction().commit();
        manager.close();
        return count = getCount();
    }

    public static String getUsername(Long id) {
        EntityManager manager = factory.createEntityManager();
        try {
            return (String) manager.createQuery("" +
                    "SELECT user.username " +
                    "FROM UserEntity user " +
                    "WHERE id = :id")
                    .setParameter("id", id)
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
