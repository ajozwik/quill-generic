package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

trait MirrorRepository[K, T <: WithId[K], D <: Idiom, N <: NamingStrategy] extends Repository[K, T] {
  protected val context: MirrorContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  override type All = context.QueryMirror[T]

  override type CreateAndRead = context.QueryMirror[T]

  override type Delete = context.ActionMirror

  override type Read = context.QueryMirror[T]

  override type Update = context.ActionMirror

  override type UpdateAndRead = context.QueryMirror[T]

  override type Create = K
}

trait MirrorRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: Idiom, N <: NamingStrategy] extends MirrorRepository[K, T, D, N]
