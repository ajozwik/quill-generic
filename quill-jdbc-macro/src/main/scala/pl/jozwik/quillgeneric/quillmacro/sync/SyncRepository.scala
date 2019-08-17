package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId }

trait SyncRepository[K, T <: WithId[K]] extends Repository[K, T] with WithSync

trait SyncRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithSync
