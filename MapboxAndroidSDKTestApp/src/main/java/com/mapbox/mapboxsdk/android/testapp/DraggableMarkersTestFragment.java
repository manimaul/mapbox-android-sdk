package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.android.testapp.views.DraggableMarkerMapView;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

public class DraggableMarkersTestFragment extends Fragment
        implements DraggableMarkerMapView.DraggableMarkerEventListener {

    DraggableMarkerMapView mv;
    Marker capitolMarker;
    Marker museumMarker;
    boolean amDraggingMuseum;
    boolean amDraggingCapitol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // add custom map view
        FrameLayout root = (FrameLayout)inflater.inflate(R.layout.fragment_draggablemarkermap, container, false);
        mv = new DraggableMarkerMapView(getActivity().getApplicationContext());
        mv.setAccessToken(getString(R.string.testAccessToken));
        mv.setTileSource(new MapboxTileLayer(getString(R.string.mapbox_id_street)));
        mv.setMapRotationEnabled(false);
        root.addView(mv);

        mv.setCenter(new LatLng(39.952532, -75.163664));
        mv.setZoom(14);

        mv.setLongClickable(true);
        mv.setMapViewListener(new DraggableMarkerMapViewListener());

        // listen for drag events from the custom MapView
        mv.setDragEventListener(this);

        capitolMarker = new Marker(mv, "City Hall", "", new LatLng(39.952532, -75.163664));
        capitolMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "town-hall", "#009933"));
        mv.addMarker(capitolMarker);
        amDraggingCapitol = false;

        museumMarker = new Marker(mv, "Museum of Art", "", new LatLng(39.965331, -75.180600));
        museumMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "museum", "#CC0066"));
        mv.addMarker(museumMarker);
        amDraggingMuseum = false;

        return root;
    }

    @Override
    public void onDragged(LatLng newPoint) {
        if (amDraggingCapitol) {
            capitolMarker.setPoint(newPoint);
        } else if (amDraggingMuseum) {
            museumMarker.setPoint(newPoint);
        }
    }

    @Override
    public void onDragEnd() {
        amDraggingMuseum = false;
        amDraggingCapitol = false;

        if (capitolMarker != null) {
            capitolMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "town-hall", "#009933"));
        }
        if (museumMarker != null) {
            museumMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "museum", "#CC0066"));
        }
    }

    private class DraggableMarkerMapViewListener implements MapViewListener {

        @Override
        public void onShowMarker(MapView mapView, Marker marker) {

        }

        @Override
        public void onHideMarker(MapView mapView, Marker marker) {

        }


        @Override
        public void onTapMarker(MapView mapView, Marker marker) {

        }

        @Override
        public void onLongPressMarker(MapView mapView, Marker marker) {
            // Listen for a long press on a marker to start dragging
            if (marker.equals(capitolMarker)) {
                ((DraggableMarkerMapView)mapView).startDragging();
                amDraggingCapitol = true;
                capitolMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "star", "#66FF66"));
            } else if (marker.equals(museumMarker)) {
                ((DraggableMarkerMapView)mapView).startDragging();
                amDraggingMuseum = true;
                museumMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "star", "#FF6699"));
            }
        }

        @Override
        public void onTapMap(MapView mapView, ILatLng iLatLng) {

        }

        @Override
        public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

        }
    }
}
