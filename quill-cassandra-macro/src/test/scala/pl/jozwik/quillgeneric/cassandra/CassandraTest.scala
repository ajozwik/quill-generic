package pl.jozwik.quillgeneric.cassandra

import com.datastax.driver.core.{ Cluster, Session }
import com.datastax.driver.extras.codecs.jdk8.LocalDateTimeCodec
import io.getquill.{ CassandraAsyncContext, CassandraSyncContext, SnakeCase }
import monix.execution.Scheduler
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.DateQuotes

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

  lazy val syncCtx = new CassandraSyncContext(SnakeCase, "cassandra") with DateQuotes

  lazy val asyncCtx = new CassandraAsyncContext(SnakeCase, "cassandra") with DateQuotes

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

  private def createAddress(id: AddressId) = Address(id, "country", "city")

  "really simple transformation" should {

//      "run sync" in {
//        val id      = AddressId.random
//        val address = createAddress(id)
//        import syncCtx._
//        val schema = syncCtx.dynamicQuerySchema[Address]("Address")
//        syncCtx
//          .run {
//            schema.insertValue(address)
//          }
//        syncCtx
//          .run {
//            schema.insertValue(address)
//          }
//        val v = syncCtx
//          .run(
//            schema.filter(_.id == lift(id))
//          )
//        logger.debug(s"$v")
//        syncCtx
//          .run(
//            schema.filter(_.id == lift(id)).delete
//          )
//      }

      "run async" in {
        val id      = AddressId.random
        val address = createAddress(id)
        import asyncCtx._
        val schema = asyncCtx.dynamicQuerySchema[Address]("Address")
        asyncCtx
          .run {
            schema.insertValue(address)
          }
      }
    }

}
