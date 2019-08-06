package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros
import scala.util.Try

trait QuillCrudWithContext {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T]): Try[Seq[T]] = macro QuillCrudMacro.all

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro QuillCrudMacro.create

  def createAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro QuillCrudMacro.createAndRead

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro QuillCrudMacro.createAndGenerateId

  def createWithGenerateIdAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro QuillCrudMacro.createWithGenerateIdAndRead

  def createOrUpdate[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro QuillCrudMacro.createOrUpdate

  def createOrUpdateAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro QuillCrudMacro.createOrUpdateAndRead

  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro QuillCrudMacro.createAndGenerateIdOrUpdate

  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro QuillCrudMacro.createWithGenerateIdOrUpdateAndRead

  def update[T](entity: T)(implicit dSchema: dQuery[T]): Try[Long] = macro QuillCrudMacro.update

  def updateAndRead[T](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro QuillCrudMacro.updateAndRead

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Option[T]] = macro QuillCrudMacro.read

  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Boolean] = macro QuillCrudMacro.delete

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Boolean] = macro QuillCrudMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(
    offset: Int,
    limit: Int)(implicit dSchema: dQuery[T]): Try[Seq[T]] = macro QuillCrudMacro.searchByFilter

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Long] = macro QuillCrudMacro.count
}
