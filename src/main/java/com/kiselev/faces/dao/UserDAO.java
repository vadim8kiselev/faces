package com.kiselev.faces.dao;

import com.kiselev.faces.dao.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class UserDAO {
    private static EntityManager manager =
            Persistence.createEntityManagerFactory("faces")
                    .createEntityManager();

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

    public static boolean checkAccount(UserEntity user) {
        try {
            manager.createQuery("" +
                    "SELECT user.username, user.password " +
                    "FROM UserEntity user " +
                    "WHERE username = :username AND password = :password")
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static void addUser(UserEntity user) {
        manager.getTransaction().begin();
        manager.merge(user);
        manager.getTransaction().commit();
    }
}
