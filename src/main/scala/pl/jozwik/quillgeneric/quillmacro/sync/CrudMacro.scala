package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

private class CrudMacro(val c: MacroContext) {

  import c.universe._

  def all(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema)
      }
    """

  def createAndGenerateIdOrUpdate(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
          val result = run(
            $dSchema.filter(_.id == lift(id)).updateValue($entity)
          )
          if (result == 0) {
            run($dSchema.insertValue($entity).returningGenerated(_.id))
          } else {
            $entity.id
          }
        }
      }
    """

  def createWithGenerateIdOrUpdateAndRead(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
          val result = run(
            $dSchema.filter(_.id == lift(id)).updateValue($entity)
          )
          val newId =
            if (result == 0) {
              run($dSchema.insertValue($entity).returningGenerated(_.id))
            } else {
              $entity.id
            }
          run($dSchema.filter(_.id == lift(newId)))
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$newId"))
        }
      }
    """

  def createOrUpdate(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
         val result = run(
             $dSchema.filter(_.id == lift(id)).updateValue($entity)
          )
          if(result == 0){
            run($dSchema.insertValue($entity))
          }
        }
        $entity.id
      }
    """

  def createOrUpdateAndRead(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
         val result = run(
             $dSchema.filter(_.id == lift(id)).updateValue($entity)
          )
          if(result == 0){
            run($dSchema.insertValue($entity))
          }
        }
        run($dSchema.filter(_.id == lift(id)))
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$id"))
      }
    """

  def create(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            $dSchema.insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndRead(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        transaction {
          run($dSchema.insertValue($entity))
          run($dSchema.filter(_.id == lift(id)))
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
       }
    """

  def createAndGenerateId(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run($dSchema.insertValue($entity).returningGenerated(_.id))
       }
    """

  def createWithGenerateIdAndRead(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction {
         val id = run($dSchema.insertValue($entity).returningGenerated(_.id))
         run($dSchema.filter(_.id == lift(id)))
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
       }
    """

  def update(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        run($dSchema.filter(_.id == lift(id)).updateValue($entity))
      }
    """

  def updateAndRead(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
        run($dSchema.filter(_.id == lift(id)).updateValue($entity))
        run($dSchema.filter(_.id == lift(id)))
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
       }
      }
    """

  def read(id: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema.filter(_.id == lift($id)))
        .headOption
      }
    """

  def delete(id: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter(_.id == lift($id)).delete
        ) > 0
      }
    """

  def deleteByFilter(filter: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).delete
        ) > 0
      }
    """

  def searchByFilter(filter: Tree)(offset: Tree, limit: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
        )
      }
    """

  def count(filter: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).size
        )
      }
    """
}
