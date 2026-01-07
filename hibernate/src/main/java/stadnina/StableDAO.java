package stadnina;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class StableDAO {

    public void saveStable(Stable stable) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(stable);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- NOWA METODA DO AKTUALIZACJI KONIA ---
    public void updateHorse(Horse horse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(horse); // To wysyła zmienione dane do bazy
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    // -----------------------------------------

    public void deleteStable(Stable stable) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(stable);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Stable> getAllStables() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Stable> cr = cb.createQuery(Stable.class);
            Root<Stable> root = cr.from(Stable.class);
            cr.select(root);
            return session.createQuery(cr).getResultList();
        }
    }

    public Stable findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Stable> query = session.createQuery("FROM Stable WHERE stableName = :name", Stable.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        }
    }

    public List<Object[]> getExportData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.stableName, h.name, h.breed, h.price FROM Horse h JOIN h.stable s";
            return session.createQuery(hql).list();
        }
    }
    // --- DODAJ TO DO StableDAO.java ---
    public void deleteHorse(Horse horse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(horse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}