
package com.stackstech.honeybee.bees.configuration.enums

import com.stackstech.honeybee.bees.configuration.enums

/**
 * Supported Sink types
 *  <li>{@link #Console #Log} -  console sink, will sink metric in console (alias log)</li>
 *  <li>{@link #Hdfs} - hdfs sink, will sink metric and record in hdfs</li>
 *  <li>{@link #Es #Elasticsearch #Http} - elasticsearch sink, will sink metric
 *  in elasticsearch (alias Es and Http)</li>
 *  <li>{@link #Mongo #MongoDB} - mongo sink, will sink metric in mongo db (alias MongoDb)</li>
 *  <li>{@link #Custom} - custom sink (needs using extra jar-file-extension)</li>
 *  <li>{@link #Unknown} - </li>
 */
object SinkType extends GriffinEnum {
  type SinkType = Value

  val Console, Log, Hdfs, Es, Http, ElasticSearch, MongoDB, Mongo, Custom =
    Value

  def validSinkTypes(sinkTypeSeq: Seq[String]): Seq[SinkType] = {
    sinkTypeSeq
      .map(s => SinkType.withNameWithDefault(s))
      .filter(_ != SinkType.Unknown)
      .distinct
  }

  override def withNameWithDefault(name: String): enums.SinkType.Value = {
    val sinkType = super.withNameWithDefault(name)
    sinkType match {
      case Console | Log => Console
      case Es | ElasticSearch | Http => ElasticSearch
      case MongoDB | Mongo => MongoDB
      case _ => sinkType
    }
  }
}
