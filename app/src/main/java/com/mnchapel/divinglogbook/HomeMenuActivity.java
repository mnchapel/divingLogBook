package com.mnchapel.divinglogbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;
import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.DiveXmlDocument;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class HomeMenuActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // The list of items in the navigation drawer
    private ListView drawer_list;

    //
    private  ActionBarDrawerToggle drawerToggle;

    // The list of all dives.
    private List<Dive> diveList;


    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_menu);

        // Read external storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }

        // Write external storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }

        }

        diveList = new ArrayList<>();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*try {
            deleteDive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }*/

        // Load all dive files
        try {
            loadDive();
            Log.i("HomeMenuActivity", "Load dive, nb dives: "+diveList.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    public void onBackPressed() {
        Log.i("onBackPressed","pass inside");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;

        switch (id) {
            case R.id.drawer_item_log_book:
                fragment = new DiveBookActivity();
                break;
            case R.id.drawer_item_statistics:
                fragment = new StatisticsFragment();
                break;
            case R.id.drawer_item_load_new_dives:
                UploadDiveActivity u = new UploadDiveActivity();
                try {
                    u.uploadDive(this, diveList);
                    loadDive();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                // Close de drawer
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            default:
                return false;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragment_manager = getSupportFragmentManager();
        fragment_manager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Close de drawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        // Set the title
        setTitle(item.getTitle());

        return false;
    }



    /*
     *
     */
    private void deleteDive() throws IOException, XmlPullParserException {
        File[] diveFileList = getFilesDir().listFiles();

        for(File diveFile: diveFileList) {
            diveFile.delete();
        }
    }



    /*
     * @brief Load dive from internal storage.
     */
    private void loadDive() throws IOException, XmlPullParserException {
        DiveXmlDocument diveXmlDocument = new DiveXmlDocument();
        File[] diveFileList = getFilesDir().listFiles();

        int a = diveFileList.length;
        for(File diveFile: diveFileList) {
            InputStream is = new FileInputStream(diveFile);

            Dive dive = diveXmlDocument.readDive(this, is);
            boolean res = diveList.contains(dive);
            if(!res)
                diveList.add(dive);

            is.close();
        }

        Collections.sort(diveList);
    }



    /*
     * TODO: delete (code for test)
     */
    private void copyDiveToInternalStorage() throws IOException {
        ArrayList<String> dive_files = new ArrayList<String>();
        dive_files.add("dive_2016_03_17_2120.xml");

        AssetManager am = getAssets();

        for(int i=0; i<dive_files.size(); i++) {
            File file_internal_storage = new File(getFilesDir(), dive_files.get(i));
            InputStream is_assets = am.open(dive_files.get(i));
            copy(is_assets, file_internal_storage);
        }
    }



    /**
     *
     * @param src:
     * @param dst:
     * @throws IOException:
     */
    public void copy(InputStream src, File dst) throws IOException {
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = src.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            src.close();
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }



    /*
     * Needed to open the navigation bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }



    /**
     * Getter
     * @return the list of all dives.
     */
    public List<Dive> getDiveList() {
        return diveList;
    }



    /**
     * Getter
     *
     * @param divePosition:
     *
     * @return the dive at divePosition.
     */
    public Dive getDive(int divePosition) {
        return diveList.get(divePosition);
    }



    /**
     * Get the number of dives.
     *
     * @return the number of dives.
     */
    public int getDiveSize() {
        return diveList.size();
    }



    /**
     * Setter
     *
     * @param divePosition: the position of the dive in the list.
     * @param dive: the dive.
     */
    public void setDive(int divePosition, Dive dive) {
        diveList.set(divePosition, dive);
    }
}
