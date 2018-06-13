/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.v3.util

import com.fatboyindustrial.gsonjodatime.DateTimeConverter
import com.fatboyindustrial.gsonjodatime.LocalDateConverter
import com.fatboyindustrial.gsonjodatime.LocalDateTimeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import ffc.v3.Identity
import ffc.v3.ThaiCitizenId
import ffc.v3.ThaiHouseholdId
import me.piruin.geok.LatLng
import me.piruin.geok.geometry.Geometry
import me.piruin.geok.gson.GeometrySerializer
import me.piruin.geok.gson.LatLngSerializer
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.lang.reflect.Type

inline fun <reified T> Gson.parse(json: String): T? =
  fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> GsonBuilder.adapterFor(adapter: Any): GsonBuilder {
  return registerTypeAdapter(object : TypeToken<T>() {}.type, adapter)
}

inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type

inline fun Any.toJson(gson: Gson = defaultGson) = gson.toJson(this)

inline fun <reified T> String.parseTo(gson: Gson = defaultGson): T? =
  gson.fromJson(this, object : TypeToken<T>() {}.type)

val defaultGson: Gson by lazy {
  GsonBuilder()
    .adapterFor<Identity>(IdentityDeserializer())
    .adapterFor<Geometry>(GeometrySerializer())
    .adapterFor<LatLng>(LatLngSerializer())
    .adapterFor<DateTime>(DateTimeConverter())
    .adapterFor<LocalDate>(LocalDateConverter())
    .adapterFor<LocalDateTime>(LocalDateTimeConverter())
    .create()
}

class IdentityDeserializer : JsonDeserializer<Identity>, JsonSerializer<Identity> {
  override fun serialize(
    src: Identity,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    return context.serialize(src)
  }

  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): Identity {
    val jsonObj = json.asJsonObject
    return when (jsonObj.get("type").asString) {
      "thailand-citizen-id" -> ThaiCitizenId(jsonObj.get("id").asString)
      "thailand-household-id" -> ThaiHouseholdId(jsonObj.get("id").asString)
      else -> throw IllegalArgumentException("Not support Identity type")
    }
  }
}
