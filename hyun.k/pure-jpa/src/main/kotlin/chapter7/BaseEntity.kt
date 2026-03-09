package chapter7

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    protected var createdAt: LocalDateTime? = null
    protected var updatedAt: LocalDateTime? = null
}