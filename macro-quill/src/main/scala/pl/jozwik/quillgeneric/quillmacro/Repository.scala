package pl.jozwik.quillgeneric.quillmacro

import scala.language.higherKinds

trait RepositoryWithGeneratedId[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true): F[K]

  def createAndRead(entity: T, generatedId: Boolean = true): F[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): F[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): F[T]
}

trait RepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends Repository[K, T]

trait Repository[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T): F[K]

  def createAndRead(entity: T): F[T]

  def createOrUpdate(entity: T): F[K]

  def createOrUpdateAndRead(entity: T): F[T]
}

trait BaseRepository[K, T <: WithId[K]] extends WithMonad {

  type U

  def all: F[Seq[T]]

  def read(id: K): F[Option[T]]

  def update(t: T): F[U]

  def updateAndRead(t: T): F[T]

  def delete(id: K): F[U]

  def deleteAll: F[U]

}

trait WithMonad {
  type F[_]
}
