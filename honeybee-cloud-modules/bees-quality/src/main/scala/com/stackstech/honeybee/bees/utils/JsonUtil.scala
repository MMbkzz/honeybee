
package com.stackstech.honeybee.bees.utils

import java.io.InputStream

import scala.reflect._

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonUtil {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k, v) => k.name -> v })
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def fromJson[T: ClassTag](json: String): T = {
    mapper.readValue[T](json, classTag[T].runtimeClass.asInstanceOf[Class[T]])
  }

  def fromJson[T: ClassTag](is: InputStream): T = {
    mapper.readValue[T](is, classTag[T].runtimeClass.asInstanceOf[Class[T]])
  }

  def toAnyMap(json: String): Map[String, Any] = {
    mapper.readValue(json, classOf[Map[String, Any]])
  }
}
