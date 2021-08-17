package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.concurrent.{ ExecutionContext, Future }

trait AsyncRepositoryWithGeneratedId[K, T <: WithId[K], UP] extends AsyncBaseRepository[K, T, UP] {
  def create(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[K]

  def createAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[T]
}

trait AsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], UP] extends AsyncRepository[K, T, UP]

trait AsyncRepository[K, T <: WithId[K], UP] extends AsyncBaseRepository[K, T, UP] {
  def create(entity: T)(implicit ex: ExecutionContext): Future[K]

  def createAndRead(entity: T)(implicit ex: ExecutionContext): Future[T]

  def createOrUpdate(entity: T)(implicit ex: ExecutionContext): Future[K]

  def createOrUpdateAndRead(entity: T)(implicit ex: ExecutionContext): Future[T]
}

trait AsyncBaseRepository[K, T <: WithId[K], UP] {

  def all(implicit ex: ExecutionContext): Future[Seq[T]]

  def read(id: K)(implicit ex: ExecutionContext): Future[Option[T]]

  def readUnsafe(id: K)(implicit ex: ExecutionContext): Future[T]

  def update(t: T)(implicit ex: ExecutionContext): Future[UP]

  def updateAndRead(t: T)(implicit ex: ExecutionContext): Future[T]

  def delete(id: K)(implicit ex: ExecutionContext): Future[UP]

  def deleteAll(implicit ex: ExecutionContext): Future[UP]

}
