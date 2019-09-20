package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId, WithTransaction }

trait SyncRepository[K, T <: WithId[K]] extends Repository[K, T] with WithSync

trait SyncRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithSync

trait SyncRepositoryWithTransaction[K, T <: WithId[K]] extends SyncRepository[K, T] with WithTransaction

trait SyncRepositoryWithGeneratedIdWithTransaction[K, T <: WithId[K]] extends SyncRepositoryWithGeneratedId[K, T] with WithTransaction
