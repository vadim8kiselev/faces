package com.kiselev.faces.dao;

import com.kiselev.faces.dao.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class UserDAO {
    private static EntityManager manager =
            Persistence.createEntityManagerFactory("faces")
                    .createEntityManager();

    private static Long count = getCount();

    public static boolean checkUsername(UserEntity user) {
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
        }
    }

    public static Long getId(UserEntity user) {
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
        }
    }

    public static Long getCount() {
        return (Long) manager.createQuery("" +
                "SELECT COUNT(*) " +
                "FROM UserEntity user")
                .getSingleResult();
    }

    public static Long addUser(UserEntity user) {
        manager.getTransaction().begin();
        manager.merge(user);
        manager.getTransaction().commit();
        return count = getCount();
    }

    public static String getUsername(Long id) {

        if (id < 1 || id > count)
            return null;

        return (String) manager.createQuery("" +
                "SELECT user.username " +
                "FROM UserEntity user " +
                "WHERE id = :id")
                .setParameter("id", id)
                .getSingleResult();

    }
}
