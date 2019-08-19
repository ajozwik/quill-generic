package pl.jozwik.quillgeneric.cassandra

import com.datastax.driver.core.{ Cluster, Session }
import com.datastax.driver.extras.codecs.jdk8.LocalDateTimeCodec
import io.getquill.{ CassandraMonixContext, SnakeCase }
import monix.execution.Scheduler
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContext

class CassandraTest extends AbstractSpec with BeforeAndAfterAll {
  protected implicit val scheduler: Scheduler = Scheduler.Implicits.global
  sys.props.put("quill.binds.log", true.toString)
  protected lazy val cluster: Cluster = {
    //EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    EmbeddedCassandraServerHelper.getCluster
  }
  protected lazy val session: Session = cluster.connect()

  protected val keySpace = "demo"

  lazy val ctx = new CassandraMonixContext(SnakeCase, "cassandraMonix") with MonixWithContext[Unit] with DateQuotes

  override def beforeAll(): Unit = {
    cluster.getConfiguration.getCodecRegistry.register(LocalDateTimeCodec.instance)
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
    val dataLoader = new CQLDataLoader(session)
    dataLoader.load(new ClassPathCQLDataSet("scripts/create.cql", keySpace))
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    session.close()
    cluster.close()
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
  }

  "really simple transformation" should {
      "run monix" in {
        val id      = AddressId.random
        val address = Address(id, "country", "city")
        import ctx._
        val schema = ctx.dynamicQuerySchema[Address]("Address")
        ctx
          .run {
            schema.insertValue(address)
          }
          .runSyncUnsafe()
        val v = ctx
          .run(
            schema.filter(_.id == lift(id))
          )
          .runSyncUnsafe()
        logger.debug(s"$v")
        ctx
          .run(
            schema.filter(_.id == lift(id)).delete
          )
          .runSyncUnsafe()
      }

    }

}
