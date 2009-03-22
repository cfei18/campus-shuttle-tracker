package com.nextdoorhacker.shuttletracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class CampusShuttleTracker extends MapActivity  {
    /** Called when the activity is first created. */
	 private RSSFeed feed = null;
	 private final String RSSLOCATION = "http://trinity.ublip.com/readings/public";
	 private final String TAG = "CampusShuttleTracker";
	 private  MapView mMapView;
	 private  MapController mMapController;

	 private final int UPDATE_MAP =1;
	 private Handler handlerTimer = new Handler();
	 private boolean running=true;
	 @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.map);
        mMapView = (MapView) findViewById(R.id.mapper);
        
        mMapController = mMapView.getController();
        handlerTimer.removeCallbacks(taskUpdateMap );
        handlerTimer.postDelayed(taskUpdateMap , 100); 
        
    }
	 
	 private Handler handlerEvent = new Handler() {

		    @Override
		    public void handleMessage(Message msg) {
		        switch (msg.what) {
		        case UPDATE_MAP: {

		             // have a go at your GUI stuff here. enable/disable stuff, flash things, change text...

		        }
		            break;          
		        default: {
		            super.handleMessage(msg);
		        }
		            break;          
		        }
		    }
		};
		
		private Runnable taskUpdateMap= new Runnable() {
		       public void run() {      

		            /*
		               Do some lengthy stuff here !!!
		            */
		    	   if (running)
		    	    UpdateMap();
		    	   
		            // handling be in the dialog
		            // don't mess with GUI from within a thread
		            Message msg = new Message();
		            msg.what = UPDATE_MAP;
		            handlerEvent.sendMessage(msg);  

		            //Do this again in 30 seconds           
		            handlerTimer.postDelayed(this, 30000);
		    }
		};
		
	
    private void UpdateMap() {
    	feed = getFeed(RSSLOCATION);
    	ArrayList<OverlayItem> mapPoints = new ArrayList<OverlayItem>();
    	for (RSSItem r: feed.getAllItems()) {
        	String points = r.getPoint();
        	Pattern p = Pattern.compile("(.+) (.+)");
        	Matcher m = p.matcher(points);
        	if (m.matches()) {
        	//locations.append(m.group(1)+"|");
        	//locations.append(m.group(2)+"|");
        	double latitude = Double.parseDouble(m.group(1))*1E6;
        	double longitude = Double.parseDouble(m.group(2))*1E6;
        	mapPoints.add(new OverlayItem(new GeoPoint((int)latitude, (int)longitude),
					r.getTitle(), r.getDescription()));
        	}
        	
        }
    	Drawable marker = getResources().getDrawable(R.drawable.flag);
    	marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		mMapView.getOverlays().add(new SitesOverlay(marker,mapPoints));
		mMapView.displayZoomControls(true);
		//mMapController.setCenter(points.get(0).getPoint());
		Log.i(TAG,mapPoints.size()+"");
		GeoPoint locationPoint = mapPoints.get(0).getPoint();
		GeoPoint mapPoint = mapPoints.get(1).getPoint();
		mMapController.zoomToSpan(
			    (locationPoint.getLatitudeE6() > mapPoint.getLatitudeE6()
			        ? locationPoint.getLatitudeE6() - mapPoint.getLatitudeE6()
			        : mapPoint.getLatitudeE6() - locationPoint.getLatitudeE6()),
			    (locationPoint.getLongitudeE6() > mapPoint.getLongitudeE6()
			        ? locationPoint.getLongitudeE6() - mapPoint.getLongitudeE6()
			        : mapPoint.getLongitudeE6() - locationPoint.getLongitudeE6()));
		mMapController.animateTo(
			        new GeoPoint(
			            locationPoint.getLatitudeE6() - ((locationPoint.getLatitudeE6() - mapPoint.getLatitudeE6())/2),
			             locationPoint.getLongitudeE6() - ((locationPoint.getLongitudeE6() - mapPoint.getLongitudeE6())/2)
			        )); 
		
		Toast.makeText(this, "UPDATING", Toast.LENGTH_SHORT).show();
    }
    
    /*private void UpdateDisplay()
    {
        TextView feedtitle = (TextView) findViewById(R.id.feedtitle);
        TextView feedpubdate = (TextView) findViewById(R.id.feedpubdate);
        TextView locations = (TextView) findViewById(R.id.locations);
        ListView itemlist = (ListView) findViewById(R.id.itemlist);
        
        
        if (feed == null)
        {
        	feedtitle.setText("No RSS Feed Available");
        	return;
        }
        
        feedtitle.setText(feed.getTitle());
        feedpubdate.setText(feed.getPubDate());

        ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(this,android.R.layout.simple_list_item_1,feed.getAllItems());
        
        locations.append(feed.getItem(1).getDirection());
        for (RSSItem r: feed.getAllItems()) {
        	String points = r.getPoint();
        	Pattern p = Pattern.compile("(.+) (.+)");
        	Matcher m = p.matcher(points);
        	if (m.matches()) {
        	locations.append(m.group(1)+"|");
        	locations.append(m.group(2)+"|");
        	}
        	String[] items = p.split(points);
        	for (String s: items) {
        		locations.append(s+"|");
        	}
        	//locations.append(r.getPoint()+" ");
        
        }
        
        itemlist.setAdapter(adapter);
        
       // itemlist.setOnItemClickListener(this);
        
        itemlist.setSelection(0);
    }  
*/
    
    
    private RSSFeed getFeed(String urlToRssFeed)
    {
        try
        {
            // setup the url
           URL url = new URL(urlToRssFeed);

           // create the factory
           SAXParserFactory factory = SAXParserFactory.newInstance();
           // create a parser
           SAXParser parser = factory.newSAXParser();

           // create the reader (scanner)
           XMLReader xmlreader = parser.getXMLReader();
           // instantiate our handler
           RSSHandler theRssHandler = new RSSHandler();
           // assign our handler
           xmlreader.setContentHandler(theRssHandler);
           // get our data through the url class
           InputSource is = new InputSource(url.openStream());
           // perform the synchronous parse           
           xmlreader.parse(is);
           // get the results - should be a fully populated RSSFeed instance, 
		   // or null on error
           return theRssHandler.getFeed();
        }
        catch (Exception ee)
        {
            // if you have a problem, simply return null
            return null;
        }
    }
    @Override
	protected boolean isRouteDisplayed() {
		return true;
	}
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			// Zoom In
			int level = mMapView.getZoomLevel();
			mMapController.zoomIn();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			// Zoom Out
			int level = mMapView.getZoomLevel();
			mMapController.zoomOut();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			// Switch on the satellite images
			
				mMapView.setSatellite(!mMapView.isSatellite());
			
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			// Switch on traffic overlays
			mMapView.setTraffic(!mMapView.isTraffic());
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			running = false;
			finish();
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			// showDialog();
		}
		return false;
	}
	

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;

		public SitesOverlay(Drawable marker, ArrayList<OverlayItem> _items) {
			super(boundCenterBottom(marker));
			items = _items;
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));

		}

		@Override
		protected boolean onTap(int pIndex) {
			// show the description
			Toast.makeText(CampusShuttleTracker.this, items.get(pIndex).getSnippet(),
					Toast.LENGTH_LONG).show();

			return true;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return items.size();
		}

	}


	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
    
}