package com.mapbox.mapboxsdk.android.testapp.views;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MotionEvent;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Override MapBox MapView to implement draggable markers
 */

public class DraggableMarkerMapView extends MapView {

    private DraggableMarkerEventListener mDragEventListener;
    private boolean amDragging = false;

    public DraggableMarkerMapView(final Context aContext) {
        super(aContext);
    }

    public interface DraggableMarkerEventListener {
        void onDragged(LatLng newPoint);
        void onDragEnd();
    }

    public void startDragging() {
        amDragging = true;
    }

    public void setDragEventListener(DraggableMarkerEventListener listener) {
        mDragEventListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (amDragging) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_SCROLL:
                case MotionEvent.ACTION_MOVE:
                    if (mDragEventListener != null) {
                        // Must get x and y from event on UI thread so they'll be relative to view
                        (new ProcessMoveEvents(this)).execute(event.getX(), event.getY());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    amDragging = false;
                    if (mDragEventListener != null) {
                        mDragEventListener.onDragEnd();
                    }
                    break;
            }
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Handle drag events on a background thread, as converting to LatLng is somewhat
     * CPU-intensive. Pass in x and y to execute.
     */
    private class ProcessMoveEvents extends AsyncTask<Float, Integer, LatLng> {
        private MapView mapView;

        public ProcessMoveEvents(MapView mapView) {
            super();
            this.mapView = mapView;
        }

        @Override
        protected LatLng doInBackground(Float... xy) {
            ILatLng newLatLng = mapView.getProjection().fromPixels(xy[0], xy[1]);
            LatLng newPt = new LatLng(newLatLng.getLatitude(), newLatLng.getLongitude());
            return newPt;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            mDragEventListener.onDragged(latLng);
        }
    }
}
