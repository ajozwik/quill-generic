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
  def create(entity: T): Try[K]

  def createAndRead(entity: T): Try[T]

  def createOrUpdate(entity: T): Try[K]

  def createOrUpdateAndRead(entity: T): Try[T]
}

trait BaseRepository[K, T <: WithId[K]] {

  def all: Try[Seq[T]]

  def read(id: K): Try[Option[T]]

  def update(t: T): Try[Long]

  def updateAndRead(t: T): Try[T]

  def delete(id: K): Try[Long]

}
