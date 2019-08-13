package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey2, CompositeKey3, CompositeKey4 }

import scala.reflect.macros.whitebox.{ Context => MacroContext }

object Keys {
  private[sync] val compositeKey2Name = classOf[CompositeKey2[_, _]].getName
  private[sync] val compositeKey3Name = classOf[CompositeKey3[_, _, _]].getName
  private[sync] val compositeKey4Name = classOf[CompositeKey4[_, _, _, _]].getName
  private[sync] val compositeSet      = Set(compositeKey2Name, compositeKey3Name, compositeKey4Name)
}

private[sync] class CrudMacro(val c: MacroContext) {

  import Keys._
  import c.universe._

  private def callFilterOnId[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]) = {
    val t = weakTypeOf[K]

    t.baseClasses.find(c => compositeSet.contains(c.asClass.fullName)) match {
      case None =>
        q"$dSchema.filter(_.id == lift($id))"
      case Some(base) =>
        val query = q"$dSchema.filter(_.id.fk1 == lift($id.fk1)).filter(_.id.fk2 == lift($id.fk2))"
        base.fullName match {
          case `compositeKey4Name` =>
            q"$query.filter(_.id.fk3 == lift($id.fk3)).filter(_.id.fk4 == lift($id.fk4))"
          case `compositeKey3Name` =>
            q"$query.filter(_.id.fk3 == lift($id.fk3))"
          case `compositeKey2Name` =>
            query
          case x =>
            c.abort(NoPosition, s"$x not supported")

        }
    }
  }

  private def callFilter[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]) = {
    val id = c.Expr[K](q"$entity.id")
    callFilterOnId(id)(dSchema)
  }

  def all[T: c.WeakTypeTag](dSchema: c.Expr[_]): c.Expr[Seq[T]] =
    c.Expr[Seq[T]] { q"""
      import ${c.prefix}._
        run($dSchema)
    """ }

  def createAndGenerateIdOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[K] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[K] { q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
        q.updateValue($entity)
      )
      if (result == 0) {
        run($dSchema.insertValue($entity).returningGenerated(_.id))
      } else {
        id
      }
    """ }
  }

  def createWithGenerateIdOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[T] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[T] { q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
        q.updateValue($entity)
      )
      val newId =
        if (result == 0) {
          run($dSchema.insertValue($entity).returningGenerated(_.id))
        } else {
          id
        }
      run($dSchema.filter(_.id == lift(newId)))
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$newId"))
    """ }
  }

  def createWithGenerateIdAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[T] =
    c.Expr[T] { q"""
      import ${c.prefix}._
      val newId = run($dSchema.insertValue($entity).returningGenerated(_.id))
      val q = $dSchema.filter(_.id == lift(newId))
      run(q)
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$newId"))
    """ }

  def createOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[K] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[K] { q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(q.updateValue($entity))
      if(result == 0){
          run($dSchema.insertValue($entity))
      } 
      id
    """ }
  }

  def createOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[T] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[T] { q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      val result = run(
          q.updateValue($entity)
       )
       if(result == 0){
         run($dSchema.insertValue($entity))
       }
       run(q)
       .headOption
       .getOrElse(throw new NoSuchElementException(s"$$id"))
    """ }
  }

  def create[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[K] =
    c.Expr[K] { q"""
      import ${c.prefix}._
      run(
        $dSchema.insertValue($entity)
      )
      $entity.id
    """ }

  def createAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[T] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[T] { q"""
      import ${c.prefix}._
      val id = $entity.id
      run($dSchema.insertValue($entity))
      val q = $filter
      run(q)
      .headOption
      .getOrElse(throw new NoSuchElementException(s"$$id"))
    """ }
  }

  def createAndGenerateId[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[K] =
    c.Expr[K] { q"""
      import ${c.prefix}._
      run($dSchema.insertValue($entity).returningGenerated(_.id))
    """ }

  def update[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[Long] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[Long] { q"""
      import ${c.prefix}._
      val q = $filter
      run(q.updateValue($entity))
    """ }
  }

  def updateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): c.Expr[T] = {
    val filter = callFilter[K, T](entity)(dSchema)
    c.Expr[T] { q"""
      import ${c.prefix}._
      val q = $filter
      run(q.updateValue($entity))
      run(q)
      .headOption
      .getOrElse{
        val id = $entity.id
        throw new NoSuchElementException(s"$$id")
       }
    """ }
  }

  def read[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): c.Expr[Option[T]] = {
    val filter = callFilterOnId(id)(dSchema)
    c.Expr[Option[T]] { q"""
      import ${c.prefix}._
      val q = $filter
      run(q)
      .headOption
    """ }
  }

  def delete[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): c.Expr[Long] = {
    val filter = callFilterOnId(id)(dSchema)
    c.Expr[Long] { q"""
      import ${c.prefix}._
      val q = $filter
      run(
         q.delete
      )
    """ }
  }

  def deleteByFilter(filter: Tree)(dSchema: c.Expr[_]): c.Expr[Long] =
    c.Expr[Long] { q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).delete
      )
    """ }

  def searchByFilter[T: c.WeakTypeTag](filter: Tree)(offset: c.Expr[Int], limit: c.Expr[Int])(dSchema: c.Expr[_]): c.Expr[Seq[T]] =
    c.Expr[Seq[T]] { q"""
      import ${c.prefix}._
      run(
        $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
      )
    """ }

  def count(filter: Tree)(dSchema: c.Expr[_]): c.Expr[Long] =
    c.Expr[Long] { q"""
      import ${c.prefix}._
      run(
         $dSchema.filter($filter).size
      )
    """ }
}
