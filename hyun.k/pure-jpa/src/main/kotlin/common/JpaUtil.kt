package common

import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence

object JpaUtil {

    private const val PERSISTENCE_UNIT_NAME = "hyun-persistence"

    fun executeInTransaction(logic: (em: EntityManager) -> Unit) {
        val emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
        val em = emf.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            logic(em)
            tx.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            tx.rollback()
        } finally {
            em.close()
            emf.close()
        }
    }
}