package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId, WithTransaction }

import scala.util.Try

trait SyncRepository[K, T <: WithId[K], UP] extends Repository[Try, K, T, UP]

trait SyncRepositoryWithGeneratedId[K, T <: WithId[K], UP] extends RepositoryWithGeneratedId[Try, K, T, UP]

trait SyncRepositoryWithTransaction[K, T <: WithId[K], UP] extends SyncRepository[K, T, UP] with WithTransaction[Try]

trait SyncRepositoryWithGeneratedIdWithTransaction[K, T <: WithId[K], UP] extends SyncRepositoryWithGeneratedId[K, T, UP] with WithTransaction[Try]
