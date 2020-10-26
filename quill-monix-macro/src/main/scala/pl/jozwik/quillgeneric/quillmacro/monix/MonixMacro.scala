package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.AbstractCrudMacro

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class MonixMacro(val c: MacroContext) extends AbstractCrudMacro {

  import c.universe._

  def createAndGenerateIdOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        t <- if(result == 0){ run($dSchema.insertValue($entity).returningGenerated(_.id)) } else { monix.eval.Task.now(id)}
       } yield {
        t
       }
    """
  }

  def createWithGenerateIdOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        newId <- if(result == 0){ run($dSchema.insertValue($entity).returningGenerated(_.id)) } else { monix.eval.Task.now(id)}
        r <- run($dSchema.filter(_.id == lift(newId)))
       } yield {
        r.headOption.getOrElse(throw new NoSuchElementException(s"$$newId"))
       }
    """
  }

  def createWithGenerateIdAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      for {
         newId <- run($dSchema.insertValue($entity).returningGenerated(_.id))
         r <- run($dSchema.filter(_.id == lift(newId)))
       } yield {
         r.headOption.getOrElse(throw new NoSuchElementException(s"$$newId"))
      }
    """

  def createOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        t <- if(result == 0){ run($dSchema.insertValue($entity)) } else { monix.eval.Task.now(id) }
       } yield {
        id
       }
    """
  }

  def createOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        t <- if(result == 0){ run($dSchema.insertValue($entity)) } else { monix.eval.Task.unit }
        r <- run(q)
       } yield {
         r.headOption.getOrElse(throw new NoSuchElementException(s"$$id"))
       }
    """
  }

  def create[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      for{
        _ <- run($dSchema.insertValue($entity))
      } yield {
        $entity.id
      }
    """

  def createAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for{
        _ <- run($dSchema.insertValue($entity))
        r <- run(q)
      } yield {
        r.headOption
        .getOrElse(throw new NoSuchElementException(s"$$id"))
      }
    """
  }

  def updateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      for{
         _ <-  run(q.updateValue($entity))
         seq <- run(q)
       } yield {
        seq.headOption
        .getOrElse{
          val id = $entity.id
          throw new NoSuchElementException(s"$$id")
         }
       }
    """
  }

  def read[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId[K](id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      for {
        r <- run(q)
      } yield {
        r.headOption
      }
    """
  }

  def readUnsafe[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId[K](id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      for {
        r <- run(q)
      } yield {
        r.headOption.getOrElse(throw new NoSuchElementException(s"$$id"))
      }
    """
  }

}
