package pl.jozwik.quillgeneric.quillmacro.async

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.{ DateQuotes, WithId }

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.experimental.macros

object AsyncCrudWithContext {

  type AsyncCrudWithContextDateQuotesUnit = AsyncCrudWithContextDateQuotes[Unit]
  type AsyncCrudWithContextDateQuotesLong = AsyncCrudWithContextDateQuotes[Long]

}

trait AsyncCrudWithContextDateQuotes[U] extends AsyncCrudWithContext[U] with DateQuotes {
  this: Context[_, _] =>
}

trait AsyncCrudWithContext[U] {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Seq[T]] = macro AsyncCrudMacro.all[T]

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] = macro AsyncCrudMacro.create[K, T]

  def createAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] = macro AsyncCrudMacro.createAndRead[K, T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] =
    macro AsyncCrudMacro.createAndGenerateId[K, T]

  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] =
    macro AsyncCrudMacro.createWithGenerateIdAndRead[K, T]

  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] = macro AsyncCrudMacro.createOrUpdate[K, T]

  def createOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] =
    macro AsyncCrudMacro.createOrUpdateAndRead[K, T]

  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[K] =
    macro AsyncCrudMacro.createAndGenerateIdOrUpdate[K, T]

  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] =
    macro AsyncCrudMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.update[K, T]

  def updateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[T] = macro AsyncCrudMacro.updateAndRead[K, T]

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Option[T]] = macro AsyncCrudMacro.read[K, T]

  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.delete[K]

  def deleteAll[T](implicit dSchema: dQuery[T], ex: ExecutionContext): Future[U] = macro AsyncCrudMacro.deleteAll[T]

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Long] = macro AsyncCrudMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Seq[T]] =
    macro AsyncCrudMacro.searchByFilter[T]

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T], ex: ExecutionContext): Future[Long] = macro AsyncCrudMacro.count
}
