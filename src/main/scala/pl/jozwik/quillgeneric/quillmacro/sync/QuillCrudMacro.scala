package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudMacro(val c: MacroContext) {

  import c.universe._

  def all[T](dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
          $dSchema
        )
      }
    """

  def createOrUpdate[T](entity: Tree, generateId: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction{
          val result = run(
            $dSchema.filter(_.id == entity.id).updateValue($entity)
          )
          if (result == 0) {
              run(
                $dSchema.insertValue($entity).returning(_.id)
              )
          } else {
            $entity.id
          }
        }
      }
    """

  def create[T](entity: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            $dSchema.insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndGenerateId[T](entity: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
           $dSchema.insertValue($entity).returningGenerated(_.id)
          )
       }
    """

  def update[T](entity: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
            $dSchema.updateValue($entity)
        )
      }
    """

  def read[T](id: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter(_.id == lift($id))
        ).headOption
      }
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).update(action, actions:_*)
        )
      }
    """

  def deleteByFilter[T](filter: Tree)(dSchema: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).delete
        ) > 0
      }
    """
}