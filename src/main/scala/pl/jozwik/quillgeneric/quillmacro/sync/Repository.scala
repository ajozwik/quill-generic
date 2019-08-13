package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.util.Try

trait RepositoryWithGeneratedId[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true): Try[K]

  def createAndRead(entity: T, generatedId: Boolean = true): Try[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): Try[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): Try[T]
}

trait RepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends Repository[K, T]

trait Repository[K, T <: WithId[K]] extends BaseRepository[K, T] {
  type CreateAndRead

  type Create

  def create(entity: T): Create

  def createAndRead(entity: T): CreateAndRead

  def createOrUpdate(entity: T): Create

  def createOrUpdateAndRead(entity: T): CreateAndRead
}

trait BaseRepository[K, T <: WithId[K]] {

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
