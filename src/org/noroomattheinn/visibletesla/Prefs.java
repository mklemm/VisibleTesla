/*
 * Prefs.java - Copyright(c) 2013 Joe Pasqua
 * Provided under the MIT License. See the LICENSE file for details.
 * Created: Nov 17, 2013
 */
package org.noroomattheinn.visibletesla;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.noroomattheinn.utils.PWUtils;

/**
 * Prefs - Stores and Manages Preferences data for all of the tabs.
 *
 * @author Joe Pasqua <joe at NoRoomAtTheInn dot org>
 */
public class Prefs {
    
    private final AppContext appContext;
    
    public Prefs(AppContext ac) {
        appContext = ac;
        loadGeneralPrefs();
        loadGraphPrefs();
        loadSchedulerPrefs();
        loadLocationPrefs();
        loadOtherPrefs();
    }
    
/*------------------------------------------------------------------------------
 *
 * General Application Preferences
 * 
 *----------------------------------------------------------------------------*/
    
    public IntegerProperty  idleThresholdInMinutes = new SimpleIntegerProperty();
    public BooleanProperty  wakeOnTabChange = new SimpleBooleanProperty();
    public BooleanProperty  offerExperimental = new SimpleBooleanProperty();
    public BooleanProperty  enableProxy = new SimpleBooleanProperty();
    public StringProperty   proxyHost = new SimpleStringProperty();
    public IntegerProperty  proxyPort = new SimpleIntegerProperty();
    public BooleanProperty  useCustomGoogleAPIKey = new SimpleBooleanProperty();
    public StringProperty   googleAPIKey = new SimpleStringProperty();
    public BooleanProperty  useCustomMailGunKey = new SimpleBooleanProperty();
    public StringProperty   mailGunKey = new SimpleStringProperty();
    public IntegerProperty  fontScale = new SimpleIntegerProperty();
    public BooleanProperty  enableRest = new SimpleBooleanProperty();
    public IntegerProperty  restPort = new SimpleIntegerProperty();
    public StringProperty   authCode = new SimpleStringProperty();

    private static final String WakeOnTCKey = "APP_WAKE_ON_TC";
    private static final String IdleThresholdKey = "APP_IDLE_THRESHOLD";
    private static final String OfferExpKey = "APP_OFFER_EXP";
    private static final String EnableProxyKey = "APP_ENABLE_PROXY";
    private static final String ProxyHostKey = "APP_PROXY_HOST";
    private static final String ProxyPortKey = "APP_PROXY_PORT";
    private static final String UseCustomGoogleKey = "APP_USE_CUSTOM_GKEY";
    private static final String CustomGoogleKey = "APP_CUSTOM_GKEY";
    private static final String UseCustomMailGunKey = "APP_USE_CUSTOM_MGKEY";
    private static final String CustomMailGunKey = "APP_CUSTOM_MGKEY";
    private static final String FontScaleKey = "APP_FONT_SCALE";
    private static final String RestPortKey = "APP_REST_PORT";
    private static final String EnableRestKey = "APP_ENABLE_REST";
    private static final String AuthCodeKey = "APP_AUTH_CODE";
    
    private void loadGeneralPrefs() {
        booleanPref(WakeOnTCKey, wakeOnTabChange, true);
        booleanPref(OfferExpKey, offerExperimental, false);
        integerPref(IdleThresholdKey, idleThresholdInMinutes, 15);
        booleanPref(EnableProxyKey, enableProxy, false);
        stringPref(ProxyHostKey, proxyHost, "");
        integerPref(ProxyPortKey, proxyPort, 8080);
        booleanPref(UseCustomGoogleKey, useCustomGoogleAPIKey, false);
        stringPref(CustomGoogleKey, googleAPIKey, AppContext.GoogleMapsAPIKey);
        booleanPref(UseCustomMailGunKey, useCustomMailGunKey, false);
        stringPref(CustomMailGunKey, mailGunKey, AppContext.MailGunKey);
        integerPref(FontScaleKey, fontScale, 100);
        booleanPref(EnableRestKey, enableRest, false);
        integerPref(RestPortKey, restPort, 9090);
        
        stringPref(AuthCodeKey, authCode, "");
        // Break down the external representation into the salt and password
        String externalRep = authCode.get();
        List<byte[]> internalForm = (new PWUtils()).internalRep(externalRep);
        appContext.restSalt = internalForm.get(0);
        appContext.restEncPW = internalForm.get(1);
    }
    
/*------------------------------------------------------------------------------
 *
 * Preferences related to the Graphs Tab
 * 
 *----------------------------------------------------------------------------*/
    private static final String GraphIgnoreGapsKey = "GRAPH_GAP_IGNORE";
    private static final String GraphGapTimeKey = "GRAPH_GAP_TIME";
    private static final String GraphPeriodPrefKey = "GRAPH_PERIOD";
     
    public StringProperty   loadPeriod = new SimpleStringProperty();
    public BooleanProperty  ignoreGraphGaps = new SimpleBooleanProperty();
    public IntegerProperty  graphGapTime = new SimpleIntegerProperty();
    
    private void loadGraphPrefs() {
        stringPref(GraphPeriodPrefKey, loadPeriod, StatsStore.LoadPeriod.All.name());
        booleanPref(GraphIgnoreGapsKey, ignoreGraphGaps, false);
        integerPref(GraphGapTimeKey, graphGapTime, 15); // 15 minutes        
    }
    
/*------------------------------------------------------------------------------
 *
 * Preferences related to the Scheduler Tab
 * 
 *----------------------------------------------------------------------------*/
    
    public BooleanProperty safeIncludesMinCharge = new SimpleBooleanProperty();
    public BooleanProperty safeIncludesPluggedIn = new SimpleBooleanProperty();
    
    private static final String SchedSafeIncludesBattery = "SCHED_SAFE_BATTERY";
    private static final String SchedSafeIncludesPlugged = "SCHED_SAFE_PLUGGED_IN";
    
    private void loadSchedulerPrefs() {
        booleanPref(SchedSafeIncludesBattery, safeIncludesMinCharge, true);
        booleanPref(SchedSafeIncludesPlugged, safeIncludesPluggedIn, false);
    }
    
/*------------------------------------------------------------------------------
 *
 * Preferences related to the Location Tab
 * 
 *----------------------------------------------------------------------------*/
    
    public BooleanProperty collectLocationData = new SimpleBooleanProperty();
    public BooleanProperty streamWhenPossible = new SimpleBooleanProperty();
    public IntegerProperty locMinTime = new SimpleIntegerProperty();
    public IntegerProperty locMinDist = new SimpleIntegerProperty();
    
    private static final String LocCollectData = "LOC_COLLECT_DATA";
    private static final String LocStreamMore = "LOC_STREAM_MORE";
    private static final String LocMinTime = "LOC_MIN_TIME";
    private static final String LocMinDist = "LOC_MIN_DIST";
    
    private void loadLocationPrefs() {
        booleanPref(LocCollectData, collectLocationData, true);
        booleanPref(LocStreamMore, streamWhenPossible, false);
        integerPref(LocMinTime, locMinTime, 5); // 5 Seconds
        integerPref(LocMinDist, locMinDist, 5); // 5 Meters
    }
    

/*------------------------------------------------------------------------------
 *
 * OTHER Preferences
 * 
 *----------------------------------------------------------------------------*/
    
    public StringProperty   notificationAddress = new SimpleStringProperty();

    private static final String NotificationAddressKey = "NOTIFICATION_ADDR";
    
    private void loadOtherPrefs() {
        stringPref(NotificationAddressKey, notificationAddress, "");
    }
    
/*------------------------------------------------------------------------------
 *
 * PRIVATE - Convenience Methods for handling preferences
 * 
 *----------------------------------------------------------------------------*/

    private void integerPref(final String key, IntegerProperty property, int defaultValue) {
        property.set(appContext.persistentState.getInt(key, defaultValue));
        property.addListener(new ChangeListener<Number>() {
            @Override public void changed(
                ObservableValue<? extends Number> ov, Number old, Number cur) {
                    appContext.persistentState.putInt(key, cur.intValue());
            }
        });
    }
    
    private void booleanPref(final String key, BooleanProperty property, boolean defaultValue) {
        property.set(appContext.persistentState.getBoolean(key, defaultValue));
        property.addListener(new ChangeListener<Boolean>() {
            @Override public void changed(
                ObservableValue<? extends Boolean> ov, Boolean old, Boolean cur) {
                    appContext.persistentState.putBoolean(key, cur);
            }
        });
    }
    
    private void stringPref(final String key, StringProperty property, String defaultValue) {
        property.set(appContext.persistentState.get(key, defaultValue));
        property.addListener(new ChangeListener<String>() {
            @Override public void changed(
                ObservableValue<? extends String> ov, String old, String cur) {
                    appContext.persistentState.put(key, cur);
            }
        });
    }
    
}

