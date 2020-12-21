package pl.jozwik.quillgeneric.quillmacro.monix

import io.getquill.context.Context
import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.{ DateQuotes, WithId }

import scala.language.experimental.macros

trait MonixWithContext[U] extends DateQuotes {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def all[T](implicit dSchema: dQuery[T]): Task[Seq[T]] = macro MonixMacro.all[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.create[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createAndGenerateId[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createWithGenerateIdAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.createOrUpdateAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[K] = macro MonixMacro.createAndGenerateIdOrUpdate[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] =
    macro MonixMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[U] = macro MonixMacro.update[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def updateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.updateAndRead[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Task[Option[T]] = macro MonixMacro.read[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def readUnsafe[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Task[T] = macro MonixMacro.readUnsafe[K, T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Task[U] = macro MonixMacro.delete[K]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteAll[T](implicit dSchema: dQuery[T]): Task[U] = macro MonixMacro.deleteAll[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Task[Long] = macro MonixMacro.deleteByFilter

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T]): Task[Seq[T]] = macro MonixMacro.searchByFilter[T]

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Task[Long] = macro MonixMacro.count
}
