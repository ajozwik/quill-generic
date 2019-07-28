package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.util.Try

trait RepositoryWithGeneratedId[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true): Try[K]

  def createOrUpdate(entity: T, generatedId: Boolean = true): Try[K]
}

trait Repository[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T): Try[K]

  def createOrUpdate(entity: T): Try[K]
}

trait BaseRepository[K, T <: WithId[K]] {

  def all: Try[Seq[T]]

  def read(id: K): Try[Option[T]]

  def update(t: T): Try[Long]

  def delete(id: K): Try[Boolean]

}
