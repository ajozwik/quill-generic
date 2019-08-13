package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.util.Try

trait RepositoryWithGeneratedId[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true): Create

  def createAndRead(entity: T, generatedId: Boolean = true): CreateAndRead

  def createOrUpdate(entity: T, generatedId: Boolean = true): Create

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): CreateAndRead
}

trait RepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends Repository[K, T]

trait Repository[K, T <: WithId[K]] extends BaseRepository[K, T] {

  def create(entity: T): Create

  def createAndRead(entity: T): CreateAndRead

  def createOrUpdate(entity: T): Create

  def createOrUpdateAndRead(entity: T): CreateAndRead
}

trait BaseRepository[K, T <: WithId[K]] {

  type CreateAndRead

  type Create

  type All

  type Delete

  type Read

  type Update

  type UpdateAndRead

  def all: All

  def read(id: K): Read

  def update(t: T): Update

  def updateAndRead(t: T): UpdateAndRead

  def delete(id: K): Delete

}

trait SqlRepository[K, T <: WithId[K]] {
  type All = Try[Seq[T]]

  type CreateAndRead = Try[T]

  type Delete = Try[Long]

  type Read = Try[Option[T]]

  type Update = Try[Long]

  type UpdateAndRead = Try[T]

  type Create = Try[K]

}
