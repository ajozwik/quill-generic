package pl.jozwik.quillgeneric.cassandra

import com.datastax.driver.core.{ Cluster, Session }
import com.datastax.driver.extras.codecs.jdk8.LocalDateTimeCodec
import monix.execution.Scheduler
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }

trait AbstractCassandraSpec extends AbstractSpec with BeforeAndAfterAll {
  protected implicit val scheduler: Scheduler = Scheduler.Implicits.global
  sys.props.put("quill.binds.log", true.toString)
  protected def createAddress(id: AddressId) = Address(id, "country", "city")

  protected lazy val cluster: Cluster = {
    //EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    EmbeddedCassandraServerHelper.getCluster
  }
  protected lazy val session: Session = cluster.connect()

  protected val keySpace = "demo"

  override def beforeAll(): Unit = {
    cluster.getConfiguration.getCodecRegistry.register(LocalDateTimeCodec.instance)
    val dataLoader = new CQLDataLoader(session)
    dataLoader.load(new ClassPathCQLDataSet("scripts/create.cql", keySpace))
  }

  override protected def afterAll(): Unit =
    super.afterAll()

}
