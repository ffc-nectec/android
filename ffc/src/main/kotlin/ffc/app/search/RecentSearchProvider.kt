package ffc.app.search

import android.app.SearchManager
import android.app.SearchManager.SUGGEST_URI_PATH_QUERY
import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.net.Uri

class RecentSearchProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "ffc.app.search.RecentSearchProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
        val URI = Uri.parse("content://" + AUTHORITY + '/' + SUGGEST_URI_PATH_QUERY)

        fun query(context: Context, query: String? = "", limit: Int = 8): List<String> {
            val suggestion = mutableListOf<String>()
            context.contentResolver.query(URI, null, null, arrayOf(query), null)?.let { cursor ->
                val loop = if (cursor.count > limit) limit else cursor.count - 1
                for (i in 0..loop) {
                    cursor.moveToPosition(i)
                    suggestion.add(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)))
                }
                cursor.close()
            }
            return suggestion
        }
    }
}
