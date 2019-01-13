package com.exampleapps.mapcontacts.ui.main.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.exampleapps.mapcontacts.R;
import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.di.component.ActivityComponent;
import com.exampleapps.mapcontacts.ui.base.BaseFragment;
import com.exampleapps.mapcontacts.ui.main.MainActivity;
import com.exampleapps.mapcontacts.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MapFragment extends BaseFragment implements
        MapContract.View, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, MainActivity.Callback {

    public static final String TAG = "MapFragment";

    private static final int LOCATION_REQUEST_CODE = PermissionUtils.LOCATION_REQUEST_CODE;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static int zoom = 15;

    @Inject
    MapContract.Presenter<MapContract.View> mPresenter;

    private MainActivity mMainActivity;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;

    private Marker mMyMarker;
    private Map<Marker, Long> markers;
    private List<Marker> markersList;

    @BindView(R.id.fab_location)
    FloatingActionButton fabLocation;

    public static MapFragment newInstance(){
        Bundle bundle = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null){
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;
            mMainActivity = activity;
            activity.onFragmentAttached();
            mMainActivity.setCallback(this);
        }
    }

    @Override
    protected void setUp(View view) {
        if (mGoogleMap == null){
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_mapfragment);
            markers = new HashMap<>();
            markersList = new ArrayList<>();
            mapFragment.getMapAsync(this);
            Timber.i("Map initialized in setUp()");
        }
    }

    private boolean hasMap(){
        return mGoogleMap != null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setOnInfoWindowClickListener(this);

        mGoogleMap = googleMap;
        mPresenter.setupMap();

        if (PermissionUtils.hasPermission(mMainActivity, LOCATION_PERMISSION)){
            enableMapLocation(googleMap);
        } else {
            requestPermissions(new String[]{LOCATION_PERMISSION}, LOCATION_REQUEST_CODE);
        }

    }

    @SuppressLint("MissingPermission")
    private void enableMapLocation(GoogleMap googleMap){
        googleMap.setMyLocationEnabled(false);
        mPresenter.centerMapOnLocation();
    }

    @OnClick(R.id.fab_location)
    protected void updateMyLocation(){
        if (PermissionUtils.hasPermission(mMainActivity, LOCATION_PERMISSION)){
            mPresenter.centerMapOnLocation();
        } else {
            requestPermissions(new String[]{LOCATION_PERMISSION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void animateMapCamera(Location location) {
        if (hasMap()) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMyMarker != null)
                mMyMarker.remove();
            mMyMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                    .title("You're here :)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
    }

    @Override
    public void showNoLocationAvailable() {
        showMessage(R.string.error_accessing_location);
    }

    @Override
    public void showGenericError() {
        showMessage(R.string.error_generic);
    }

    @Override
    public void addMapMarkers(List<Contact> contacts) {
        if (hasMap()){
            for (Contact contact : contacts){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(contact.getLatitude(), contact.getLongitude()))
                        .title(contact.getDisplayName())
                        .snippet("Phone: " + contact.getPhoneNumber())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                Marker marker = mGoogleMap.addMarker(markerOptions);
                markersList.add(marker);
                markers.put(marker, contact.getId());
            }
            hideLoading();
            Timber.i("IS IN addMapMarkers!");
        }
    }

    @Override
    public void updateMarkers() {
        removeMarkers(markersList);
        mPresenter.getContactsFromDb();
    }

    @Override
    public void deleteMarkers() {
        removeMarkers(markersList);
    }

    @Override
    public void startLocationFromOnActivityResult() {
        updateMyLocation();
    }

    private void removeMarkers(List<Marker> markersList){
        for (Marker marker : markersList){
            marker.remove();
        }
        markers.clear();
        markersList.clear();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE: {
                if (PermissionUtils.hasAllPermissionsGranted(grantResults)){
                    enableMapLocation(mGoogleMap);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (hasMap()){
            mGoogleMap.setOnInfoWindowClickListener(null);
        }
        mGoogleMap = null;
        mPresenter.onDetach();
        mMainActivity = null;
        markers.clear();
        markers = null;
    }



}
