package pl.jozwik.quillgeneric.quillmacro.async

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.DateQuotes
import pl.jozwik.quillgeneric.repository.WithId

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.experimental.macros

object AsyncCrudWithContext {

  type AsyncCrudWithContextUnit = AsyncCrudWithContext[Unit]
  type AsyncCrudWithContextLong = AsyncCrudWithContext[Long]

}

trait AsyncCrudWithContext[U] extends DateQuotes {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def all[T](implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Seq[T]] = macro AsyncCrudMacro.all[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] = macro AsyncCrudMacro.create[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] =
    macro AsyncCrudMacro.createAndGenerateId[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] =
    macro AsyncCrudMacro.createWithGenerateIdAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] = macro AsyncCrudMacro.createOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] =
    macro AsyncCrudMacro.createAndGenerateIdOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] =
    macro AsyncCrudMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.update[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Option[T]] = macro AsyncCrudMacro.read[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.delete[K]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteAll[T](implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.deleteAll[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Long] = macro AsyncCrudMacro.deleteByFilter

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Seq[T]] =
    macro AsyncCrudMacro.searchByFilter[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Long] = macro AsyncCrudMacro.count
}
