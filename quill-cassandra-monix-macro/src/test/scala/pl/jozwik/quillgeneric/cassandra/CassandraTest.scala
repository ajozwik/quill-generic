package pl.jozwik.quillgeneric.cassandra

import com.datastax.driver.core.{ Cluster, Session }
import io.getquill.{ CassandraMonixContext, SnakeCase }
import monix.execution.Scheduler
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.cassandra.model.{ SimpleAddress, SimpleAddressId }

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

  lazy val ctx = new CassandraMonixContext(SnakeCase, "cassandraMonix")

  override def beforeAll(): Unit = {
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
      "run " in {
        val id      = SimpleAddressId.random
        val address = SimpleAddress(id, "country", "city")
        import ctx._
        val schema = ctx.dynamicQuerySchema[SimpleAddress]("Address")
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
      }

    }

}
