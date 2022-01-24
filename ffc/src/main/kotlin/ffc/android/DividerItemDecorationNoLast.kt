package ffc.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

/**
 * [Link](https://gist.github.com/mohsenoid/8ffdfa53f0465533833b0b44257aa641)
 *
 * DividerItemDecorationNoLast is a [RecyclerView.ItemDecoration] that can be used as a divider
 * between items of a [LinearLayoutManager]. It supports both [.HORIZONTAL] and
 * [.VERTICAL] orientations.
 *
 * <pre>
 * mDividerItemDecorationNoLast = new DividerItemDecorationNoLast(recyclerView.getContext(),
 * mLayoutManager.getOrientation());
 * recyclerView.addItemDecoration(mDividerItemDecorationNoLast);
 * </pre>
 */
class DividerItemDecorationNoLast(context: Context?, orientation: Int) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    var startOffset: Int = 0
    var endOffset: Int = 0

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var mOrientation: Int = 0

    private val mBounds = Rect()

    init {
        val a = context?.obtainStyledAttributes(ATTRS)
        mDivider = a?.getDrawable(0)
        if (mDivider == null) {
            Timber.w("""
                @android:attr/listDivider was not set in the theme used for this DividerItemDecorationNoLast.
                Please set that attribute all call setDrawable()
                """.trimIndent())
        }
        a?.recycle()
        setOrientation(orientation)
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw IllegalArgumentException(
                "Invalid orientation. It should be either HORIZONTAL or VERTICAL")
        }
        mOrientation = orientation
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable?) {
        if (drawable == null) {
            throw IllegalArgumentException("Drawable cannot be null.")
        }
        mDivider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft + startOffset
            right = parent.width - (parent.paddingRight + endOffset)
            canvas.clipRect(left, parent.paddingTop, right,
                parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop + startOffset
            bottom = parent.height - (parent.paddingBottom + endOffset)
            canvas.clipRect(parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + Math.round(child.translationX)
            val left = right - mDivider!!.intrinsicWidth
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
        }
    }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        private const val TAG = "DividerItem"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
