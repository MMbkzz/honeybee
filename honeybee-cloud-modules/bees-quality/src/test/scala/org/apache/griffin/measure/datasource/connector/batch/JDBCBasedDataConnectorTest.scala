package com.stackstech.honeybee.bees.datasource.connector.batch

import java.sql.DriverManager
import java.util.Properties

import org.scalatest.Matchers

import com.stackstech.honeybee.bees.SparkSuiteBase
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.datasource.TimestampStorage

class JDBCBasedDataConnectorTest extends SparkSuiteBase with Matchers {

  val url = "jdbc:h2:mem:test"
  var conn: java.sql.Connection = _
  val properties = new Properties()
  properties.setProperty("user", "user")
  properties.setProperty("password", "password")
  properties.setProperty("rowId", "false")

  override def beforeAll(): Unit = {
    super.beforeAll()
    DriverManager.registerDriver(new org.h2.Driver)
    Class.forName("org.h2.Driver", false, this.getClass.getClassLoader)
    conn = DriverManager.getConnection(url, properties)
    conn.prepareStatement("create schema griffin").executeUpdate()

    conn.prepareStatement("drop table if exists griffin.employee").executeUpdate()
    conn
      .prepareStatement(
        "create table griffin.employee (name VARCHAR NOT NULL, id INTEGER NOT NULL)")
      .executeUpdate()
    conn.prepareStatement("insert into griffin.employee values ('emp1', 1)").executeUpdate()
    conn.prepareStatement("insert into griffin.employee values ('emp2', 2)").executeUpdate()
    conn.commit()
  }

  private final val dcParam =
    DataConnectorParam("jdbc", "test_df", Map.empty[String, String], Nil)
  private final val timestampStorage = TimestampStorage()

  "JDBC based data connector" should "be able to read data from relational database" in {
    val configs = Map(
      "database" -> "griffin",
      "tablename" -> "employee",
      "url" -> url,
      "user" -> "user",
      "password" -> "password",
      "driver" -> "org.h2.Driver")
    val dc = JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    val result = dc.data(1000L)
    assert(result._1.isDefined)
    assert(result._1.get.collect().length == 2)
  }

  "JDBC based data connector" should "be able to read data from relational database with where condition" in {
    val configs = Map(
      "database" -> "griffin",
      "tablename" -> "employee",
      "url" -> url,
      "user" -> "user",
      "password" -> "password",
      "driver" -> "org.h2.Driver",
      "where" -> "id=1 and name='emp1'")
    val dc = JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    val result = dc.data(1000L)
    assert(result._1.isDefined)
    assert(result._1.get.collect().length == 1)
  }

  "JDBC data connector" should "have URL field in config" in {
    the[java.lang.IllegalArgumentException] thrownBy {
      val configs = Map(
        "database" -> "griffin",
        "tablename" -> "employee",
        "user" -> "user",
        "password" -> "password",
        "driver" -> "org.h2.Driver")
      JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    } should have message "requirement failed: JDBC connection: connection url is mandatory"
  }

  "JDBC data connector" should "have table name field in config" in {
    the[java.lang.IllegalArgumentException] thrownBy {
      val configs = Map(
        "database" -> "griffin",
        "url" -> url,
        "user" -> "user",
        "password" -> "password",
        "driver" -> "org.h2.Driver")
      JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    } should have message "requirement failed: JDBC connection: table is mandatory"
  }

  "JDBC data connector" should "have user name field in config" in {
    the[java.lang.IllegalArgumentException] thrownBy {
      val configs = Map(
        "database" -> "griffin",
        "url" -> url,
        "tablename" -> "employee",
        "password" -> "password",
        "driver" -> "org.h2.Driver")
      JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    } should have message "requirement failed: JDBC connection: user name is mandatory"
  }

  "JDBC data connector" should "have table password field in config" in {
    the[java.lang.IllegalArgumentException] thrownBy {
      val configs = Map(
        "database" -> "griffin",
        "url" -> url,
        "tablename" -> "employee",
        "user" -> "user",
        "driver" -> "org.h2.Driver")
      JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    } should have message "requirement failed: JDBC connection: password is mandatory"
  }

  "JDBC data connector" should "have driver provided in config in classpath" in {
    the[AssertionError] thrownBy {
      val configs = Map(
        "database" -> "griffin",
        "url" -> url,
        "tablename" -> "employee",
        "user" -> "user",
        "password" -> "password",
        "driver" -> "org.postgresql.Driver")
      JDBCBasedDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
    }
  }

  override def afterAll(): Unit = {
    super.afterAll()
    conn.close()
  }
}
