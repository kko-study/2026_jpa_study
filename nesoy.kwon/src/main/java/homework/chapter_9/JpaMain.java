package homework.chapter_9;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;


public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			AddressEntity addressEntity = new AddressEntity();
			List<Address> addressHistory = addressEntity.getAddressHistory();
			addressHistory.add(new Address(" 서울", "강남", "123-123"));
			addressHistory.add(new Address(" 경기", "성남", "123-456"));
			em.persist(addressEntity);

			em.flush();
			em.clear();

			AddressEntity foundAddressEntity = em.find(AddressEntity.class, addressEntity.getId());
			List<Address> foundAddressEntityAddressHistory = foundAddressEntity.getAddressHistory();
			foundAddressEntityAddressHistory.add(new Address(" 새로운 주소야", "새로운", "123-123"));
			em.persist(foundAddressEntity);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}
}
