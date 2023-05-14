package pl.jozwik.quillgeneric.repository

import scala.concurrent.{ExecutionContext, Future}

trait AsyncRepositoryWithGeneratedId[K, T <: WithId[K], UP] extends AsyncBaseRepository[K, T, UP] {
  def create(entity: T, generatedId: Boolean = true): Future[K]

  def createAndRead(entity: T, generatedId: Boolean = true): Future[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): Future[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): Future[T]
}

trait AsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], UP] extends AsyncRepository[K, T, UP]

trait AsyncRepository[K, T <: WithId[K], UP] extends AsyncBaseRepository[K, T, UP] {
  def create(entity: T): Future[K]

  def createAndRead(entity: T): Future[T]

  def createOrUpdate(entity: T): Future[K]

  def createOrUpdateAndRead(entity: T): Future[T]
}

trait AsyncBaseRepository[K, T <: WithId[K], UP] {

  implicit protected def ec: ExecutionContext

  def all: Future[Seq[T]]

  def read(id: K): Future[Option[T]]

  def readUnsafe(id: K): Future[T] =
    for {
      opt <- read(id)
    } yield {
      opt.getOrElse(throw new NoSuchElementException(s"$id"))
    }

  def update(t: T): Future[UP]

  def updateAndRead(t: T): Future[T] =
    for {
      _  <- update(t)
      el <- readUnsafe(t.id)
    } yield {
      el
    }

  def delete(id: K): Future[UP]

  def deleteAll: Future[UP]

  protected final def pure[E](el: E): Future[E] = Future.successful(el)

}
