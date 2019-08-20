package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.concurrent.{ ExecutionContext, Future }

trait AsyncRepositoryWithGeneratedId[K, T <: WithId[K]] extends AsyncBaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[K]

  def createAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[T]
}

trait AsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends AsyncRepository[K, T]

trait AsyncRepository[K, T <: WithId[K]] extends AsyncBaseRepository[K, T] {
  def create(entity: T)(implicit ex: ExecutionContext): Future[K]

  def createAndRead(entity: T)(implicit ex: ExecutionContext): Future[T]

  def createOrUpdate(entity: T)(implicit ex: ExecutionContext): Future[K]

  def createOrUpdateAndRead(entity: T)(implicit ex: ExecutionContext): Future[T]
}

trait AsyncBaseRepository[K, T <: WithId[K]] {

  type U

  def all(implicit ex: ExecutionContext): Future[Seq[T]]

  def read(id: K)(implicit ex: ExecutionContext): Future[Option[T]]

  def update(t: T)(implicit ex: ExecutionContext): Future[U]

  def updateAndRead(t: T)(implicit ex: ExecutionContext): Future[T]

  def delete(id: K)(implicit ex: ExecutionContext): Future[U]

  def deleteAll(implicit ex: ExecutionContext): Future[U]

}
