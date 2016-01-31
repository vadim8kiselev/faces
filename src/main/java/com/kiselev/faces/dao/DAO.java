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
        } catch (NoResultException error) {
            error.printStackTrace();
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
            error.printStackTrace();
            return null;
        } finally {
            manager.close();
        }
    }

    public static Long addUser(ProfileEntity user) throws PersistenceException {
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(user);
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

        } catch (NoResultException error) {
            error.printStackTrace();
            return null;
        } finally {
            manager.close();
        }
    }

    public static boolean isRegistered(Long id) {
        EntityManager manager = factory.createEntityManager();
        try {
            return manager.createQuery("" +
                    "SELECT user.firstName " +
                    "FROM ProfileEntity user " +
                    "WHERE id = :id").setParameter("id", id)
                    .getSingleResult() != null;
        } catch (NoResultException error) {
            error.printStackTrace();
            return false;
        } finally {
            manager.close();
        }
    }

    public static void register(Long id, String firstName, String lastName,
                                String photo) {
        EntityManager manager = factory.createEntityManager();
        try {
            ProfileEntity profile = getProfile(id);
            if (profile == null) {
                return;
            }

            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            if (!photo.equals("")) {
                profile.setPhoto(photo);
            }

            manager.getTransaction().begin();
            manager.merge(profile);
            manager.getTransaction().commit();

        } catch (NoResultException error) {
            error.printStackTrace();
        } finally {
            manager.close();
        }
    }

    public static boolean isValidId(Long id) {
        return id >= 1 && id <= count;
    }
}
