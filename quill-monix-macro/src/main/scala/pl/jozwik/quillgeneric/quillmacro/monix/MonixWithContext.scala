package pl.jozwik.quillgeneric.quillmacro.monix

import io.getquill.context.Context
import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros

trait MonixWithContext[U] {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T]): Task[Seq[T]] = macro MonixMacro.all[T]

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.create[K, T]

  def createAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createAndRead[K, T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createAndGenerateId[K, T]

  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createWithGenerateIdAndRead[K, T]

  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createOrUpdate[K, T]

  def createOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createOrUpdateAndRead[K, T]

  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createAndGenerateIdOrUpdate[K, T]

  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] =
    macro MonixMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[U] = macro MonixMacro.update[K, T]

  def updateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.updateAndRead[K, T]

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Task[Option[T]] = macro MonixMacro.read[K, T]

  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Task[U] = macro MonixMacro.delete[K]

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Task[Long] = macro MonixMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T]): Task[Seq[T]] = macro MonixMacro.searchByFilter[T]

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Task[Long] = macro MonixMacro.count
}
