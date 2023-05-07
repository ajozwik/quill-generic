package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.{ CrudMacro, DateQuotes }
import pl.jozwik.quillgeneric.repository.WithId

import scala.language.experimental.macros

object CrudWithContext {
  type CrudWithContextDateQuotesUnit = CrudWithContextDateQuotes[Unit]
  type CrudWithContextDateQuotesLong = CrudWithContextDateQuotes[Long]
}
@SuppressWarnings(Array("org.wartremover.warts.Any"))
trait CrudWithContextDateQuotes[U] extends DateQuotes {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def all[T](implicit dSchema: dQuery[T]): Seq[T] = macro CrudMacro.all[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.create[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createAndGenerateId[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T = macro CrudMacro.createWithGenerateIdAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createAndGenerateIdOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T =
    macro CrudMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): U = macro CrudMacro.update[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Option[T] = macro CrudMacro.read[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): U = macro CrudMacro.delete[K]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteAll[T](implicit dSchema: dQuery[T]): U = macro CrudMacro.deleteAll[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Long = macro CrudMacro.deleteByFilter

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T]): Seq[T] = macro CrudMacro.searchByFilter[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Long = macro CrudMacro.count
}
