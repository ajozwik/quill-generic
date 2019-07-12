package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudMacro(val c: MacroContext) {

  import c.universe._

  def all[T](t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(quote {
          query[$t]
        })
      }
    """

  def createOrUpdate[T](entity: Tree, generateId: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction{
          val result = run(quote {
            query[$t].filter(_.id == lift(entity.id)).update(lift($entity))
          })
          if (result == 0) {
              run(quote {
                query[$t].insert(lift($entity)).returning(_.id)
              })
          } else {
            ${entity}.id
          }
        }
      }
    """

  def create[T](entity: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(quote {
            query[$t].insert(lift($entity))
          })
          ${entity}.id
       }
    """

  def createAndGenerateId[T](entity: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(quote {
            query[$t].insert(lift($entity)).returning(_.id)
          })
       }
    """

  def update[T](entity: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(quote {
          query[$t].update(lift($entity))
        })
      }
    """

  def read[T](id: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(quote {
          query[$t].filter(_.id == lift(${id}))
        }).headOption
      }
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(quote {
          query[$t].filter($filter).update(action, actions:_*)
        })
      }
    """

  def deleteByFilter[T](filter: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(quote {
          query[$t].filter($filter).delete
        }) > 0
      }
    """
}