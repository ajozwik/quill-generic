package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.{ Repository, RepositoryWithGeneratedId, WithId, WithUpdate }

trait MonixRepository[K, T <: WithId[K]] extends Repository[K, T] with WithMonix

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix

trait MonixRepositoryLong[K, T <: WithId[K]] extends Repository[K, T] with WithMonix with WithUpdate[Long]

trait MonixRepositoryWithGeneratedIdLong[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix with WithUpdate[Long]

trait MonixRepositoryUnit[K, T <: WithId[K]] extends Repository[K, T] with WithMonix with WithUpdate[Unit]

trait MonixRepositoryWithGeneratedIdUnit[K, T <: WithId[K]] extends RepositoryWithGeneratedId[K, T] with WithMonix with WithUpdate[Unit]
