package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros
import scala.util.Try

trait CrudWithContext {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T]): Try[Seq[T]] = macro CrudMacro.all

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CrudMacro.create[T]

  def createAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CrudMacro.createAndRead[T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CrudMacro.createAndGenerateId[T]

  def createWithGenerateIdAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CrudMacro.createWithGenerateIdAndRead[T]

  def createOrUpdate[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CrudMacro.createOrUpdate[T]

  def createOrUpdateAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CrudMacro.createOrUpdateAndRead[T]

  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CrudMacro.createAndGenerateIdOrUpdate[T]

  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CrudMacro.createWithGenerateIdOrUpdateAndRead[T]

  def update[T](entity: T)(implicit dSchema: dQuery[T]): Try[Long] = macro CrudMacro.update[T]

  def updateAndRead[T](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CrudMacro.updateAndRead[T]

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Option[T]] = macro CrudMacro.read[K]

  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Boolean] = macro CrudMacro.delete[K]

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Boolean] = macro CrudMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(
    offset: Int,
    limit: Int)(implicit dSchema: dQuery[T]): Try[Seq[T]] = macro CrudMacro.searchByFilter

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Long] = macro CrudMacro.count
}
