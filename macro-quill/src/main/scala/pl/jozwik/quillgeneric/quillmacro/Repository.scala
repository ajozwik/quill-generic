package pl.jozwik.quillgeneric.quillmacro

import scala.language.higherKinds

trait WithTransaction[F[_]] {
  def inTransaction[A](task: F[A]): F[A]
}

trait RepositoryWithGeneratedId[F[_], K, T <: WithId[K], UP] extends BaseRepository[F, K, T, UP] {
  def create(entity: T, generatedId: Boolean = true): F[K]

  def createAndRead(entity: T, generatedId: Boolean = true): F[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): F[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): F[T]
}

trait RepositoryCompositeKey[F[_], K <: CompositeKey[_, _], T <: WithId[K], UP] extends Repository[F, K, T, UP]

trait Repository[F[_], K, T <: WithId[K], UP] extends BaseRepository[F, K, T, UP] {
  def create(entity: T): F[K]

  def createAndRead(entity: T): F[T]

  def createOrUpdate(entity: T): F[K]

  def createOrUpdateAndRead(entity: T): F[T]
}

trait BaseRepository[F[_], K, T <: WithId[K], UP] {

  def all: F[Seq[T]]

  def read(id: K): F[Option[T]]

  def readUnsafe(id: K): F[T]

  def update(t: T): F[UP]

  def updateAndRead(t: T): F[T]

  def delete(id: K): F[UP]

  def deleteAll: F[UP]

}
