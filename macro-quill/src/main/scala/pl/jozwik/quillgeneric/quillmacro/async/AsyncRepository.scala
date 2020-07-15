package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.concurrent.ExecutionContext

trait AsyncRepositoryWithGeneratedId[K, T <: WithId[K]] extends AsyncBaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): F[K]

  def createAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): F[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): F[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): F[T]
}

trait AsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends AsyncRepository[K, T]

trait AsyncRepository[K, T <: WithId[K]] extends AsyncBaseRepository[K, T] {
  def create(entity: T)(implicit ex: ExecutionContext): F[K]

  def createAndRead(entity: T)(implicit ex: ExecutionContext): F[T]

  def createOrUpdate(entity: T)(implicit ex: ExecutionContext): F[K]

  def createOrUpdateAndRead(entity: T)(implicit ex: ExecutionContext): F[T]
}

trait AsyncBaseRepository[K, T <: WithId[K]] extends WithFuture {

  type UP

  def all(implicit ex: ExecutionContext): F[Seq[T]]

  def read(id: K)(implicit ex: ExecutionContext): F[Option[T]]

  def readUnsafe(id: K)(implicit ex: ExecutionContext): F[T]

  def update(t: T)(implicit ex: ExecutionContext): F[UP]

  def updateAndRead(t: T)(implicit ex: ExecutionContext): F[T]

  def delete(id: K)(implicit ex: ExecutionContext): F[UP]

  def deleteAll(implicit ex: ExecutionContext): F[UP]

}
