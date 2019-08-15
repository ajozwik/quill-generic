package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId }

trait MonixRepository[K, T <: WithId[K]] extends Repository[K, T] with WithMonix

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix
