package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

private class CrudMacro(val c: MacroContext) {

  import c.universe._

  private def callFilterOnId[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]) = {
    q"$dSchema.filter(_.id == lift($id))"
  }

  private def callFilter[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]) = {
    val id = c.Expr[K](q"$entity.id")
    callFilterOnId(id)(dSchema)
  }

  def all(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema)
      }
    """

  def createAndGenerateIdOrUpdate[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
          val q = $filter
          val result = run(
            q.updateValue($entity)
          )
          if (result == 0) {
            run($dSchema.insertValue($entity).returningGenerated(_.id))
          } else {
            id
          }
        }
      }
    """
  }

  def createWithGenerateIdOrUpdateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
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
        }
      }
    """
  }

  def createWithGenerateIdAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    q"""
      import ${c.prefix}._
      util.Try {
       transaction {
         val newId = run($dSchema.insertValue($entity).returningGenerated(_.id))
         val q = $dSchema.filter(_.id == lift(newId))
         run(q)
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$newId"))
        }
       }
    """
  }

  def createOrUpdate[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
         val q = $filter
         val result = run(
             q.updateValue($entity)
          )
          if(result == 0){
            run($dSchema.insertValue($entity))
          }
        }
        $entity.id
      }
    """
  }

  def createOrUpdateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
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
        }
      }
    """
  }

  def create[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            $dSchema.insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        transaction {
          run($dSchema.insertValue($entity))
          val q = $filter
          run(q)
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
       }
    """
  }

  def createAndGenerateId[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run($dSchema.insertValue($entity).returningGenerated(_.id))
       }
    """

  def update[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        val q = $filter
        run(q.updateValue($entity))
      }
    """
  }

  def updateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilter(entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
        val q = $filter
        run(q.updateValue($entity))
        run(q)
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
       }
      }
    """
  }

  def read[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId(id)(dSchema)
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $filter
        run(q)
        .headOption
      }
    """
  }

  def delete[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree = {
    val filter = callFilterOnId(id)(dSchema)
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $filter
        run(
           q.delete
        ) > 0
      }
    """
  }

  def deleteByFilter(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).delete
        ) > 0
      }
    """

  def searchByFilter(filter: Tree)(offset: c.Expr[Int], limit: c.Expr[Int])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
        )
      }
    """

  def count(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).size
        )
      }
    """
}
