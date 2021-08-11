package pl.jozwik.quillgeneric.quillmacro.monix

import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId, WithTransaction }

trait MonixRepository[K, T <: WithId[K], UP] extends Repository[Task, K, T, UP]

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K], UP] extends RepositoryWithGeneratedId[Task, K, T, UP]

trait MonixRepositoryLong[K, T <: WithId[K]] extends MonixRepository[K, T, Long]

trait MonixRepositoryWithGeneratedIdLong[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedId[K, T, Long]

trait MonixRepositoryUnit[K, T <: WithId[K]] extends MonixRepository[K, T, Unit]

trait MonixRepositoryWithGeneratedIdUnit[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedId[K, T, Unit]

trait MonixRepositoryWithTransaction[K, T <: WithId[K], UP] extends MonixRepository[K, T, UP] with WithTransaction[Task]

trait MonixRepositoryWithTransactionWithGeneratedId[K, T <: WithId[K], UP] extends MonixRepositoryWithGeneratedId[K, T, UP] with WithTransaction[Task]

trait MonixRepositoryLongWithTransaction[K, T <: WithId[K]] extends MonixRepositoryLong[K, T] with WithTransaction[Task]

trait MonixRepositoryWithGeneratedIdLongWithTransaction[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedIdLong[K, T] with WithTransaction[Task]
