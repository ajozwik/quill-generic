package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId, WithTransaction, WithUpdate }

trait MonixRepository[K, T <: WithId[K]] extends Repository[K, T] with WithMonix

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix

trait MonixRepositoryLong[K, T <: WithId[K]] extends MonixRepository[K, T] with WithUpdate[Long]

trait MonixRepositoryWithGeneratedIdLong[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedId[K, T] with WithUpdate[Long]

trait MonixRepositoryUnit[K, T <: WithId[K]] extends MonixRepository[K, T] with WithUpdate[Unit]

trait MonixRepositoryWithGeneratedIdUnit[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedId[K, T] with WithUpdate[Unit]

trait MonixRepositoryWithTransaction[K, T <: WithId[K]] extends MonixRepository[K, T] with WithTransaction

trait MonixRepositoryWithTransactionWithGeneratedId[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedId[K, T] with WithTransaction

trait MonixRepositoryLongWithTransaction[K, T <: WithId[K]] extends MonixRepositoryLong[K, T] with WithTransaction

trait MonixRepositoryWithGeneratedIdLongWithTransaction[K, T <: WithId[K]] extends MonixRepositoryWithGeneratedIdLong[K, T] with WithTransaction
