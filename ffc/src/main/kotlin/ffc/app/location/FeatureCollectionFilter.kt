package ffc.app.location

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.geometry.*

data class FeatureCollectionFilter <T>(var features: List<Feature<T>>) {

    constructor(vararg features: Feature<T>) : this(features.toList())

    val type = "FeatureCollection"
    var bbox: BBox?

    init {
        require(features.isNotEmpty()) { "Feature Collection should not empty" }

        val latLng = features.toLatLngs()
        bbox = if (latLng.size > 1) BBox.from(latLng) else null
    }

    private fun List<Feature<T>>.toLatLngs(): List<LatLng> {
        return flatMap {
            when (it.geometry) {
                is Point -> listOf((it.geometry as Point).coordinates)
                is LineString -> (it.geometry as LineString).coordinates
                is Polygon -> (it.geometry as Polygon).boundary
                is MultiPoint -> (it.geometry as MultiPoint).points.map { it.coordinates }
                is MultiLineString -> (it.geometry as MultiLineString).lines.flatMap { it.coordinates }
                is MultiPolygon -> (it.geometry as MultiPolygon).polygons.flatMap { it.boundary }
                else -> throw IllegalStateException("Not support ${it.type}")
            }
        }
    }
}
