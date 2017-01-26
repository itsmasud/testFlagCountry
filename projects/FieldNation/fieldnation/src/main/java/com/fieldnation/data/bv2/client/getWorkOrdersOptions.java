package com.fieldnation.data.bv2.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class getWorkOrdersOptions implements Parcelable {
    private static final String TAG = "getWorkOrdersOptions";

    @Json(name = "list")
    private String _list;

    @Json(name = "columns")
    private String _columns;

    @Json(name = "page")
    private int _page;

    @Json(name = "perPage")
    private int _perPage;

    @Json(name = "view")
    private String _view;

    @Json(name = "sticky")
    private Boolean _sticky;

    @Json(name = "sort")
    private String _sort;

    @Json(name = "order")
    private String _order;

    @Json(name = "f")
    private String _f;

    @Json(name = "fMaxApprovalTime")
    private int _fMaxApprovalTime;

    @Json(name = "fRating")
    private String _fRating;

    @Json(name = "fRequests")
    private Boolean _fRequests;

    @Json(name = "fCounterOffers")
    private Boolean _fCounterOffers;

    @Json(name = "fHourly")
    private String _fHourly;

    @Json(name = "fFixed")
    private String _fFixed;

    @Json(name = "fDevice")
    private String _fDevice;

    @Json(name = "fPay")
    private String _fPay;

    @Json(name = "fTemplates")
    private String _fTemplates;

    @Json(name = "fTypeOfWork")
    private String _fTypeOfWork;

    @Json(name = "fTimeZone")
    private String _fTimeZone;

    @Json(name = "fMode")
    private String _fMode;

    @Json(name = "fCompany")
    private String _fCompany;

    @Json(name = "fWorkedWith")
    private String _fWorkedWith;

    @Json(name = "fManager")
    private String _fManager;

    @Json(name = "fClient")
    private String _fClient;

    @Json(name = "fProject")
    private String _fProject;

    @Json(name = "fApprovalWindow")
    private String _fApprovalWindow;

    @Json(name = "fReviewWindow")
    private String _fReviewWindow;

    @Json(name = "fNetwork")
    private String _fNetwork;

    @Json(name = "fAutoAssign")
    private String _fAutoAssign;

    @Json(name = "fSchedule")
    private String _fSchedule;

    @Json(name = "fCreated")
    private String _fCreated;

    @Json(name = "fPublished")
    private String _fPublished;

    @Json(name = "fRouted")
    private String _fRouted;

    @Json(name = "fPublishedRouted")
    private String _fPublishedRouted;

    @Json(name = "fCompleted")
    private String _fCompleted;

    @Json(name = "fApprovedCancelled")
    private String _fApprovedCancelled;

    @Json(name = "fConfirmed")
    private String _fConfirmed;

    @Json(name = "fAssigned")
    private String _fAssigned;

    @Json(name = "fSavedLocation")
    private String _fSavedLocation;

    @Json(name = "fSavedLocationGroup")
    private String _fSavedLocationGroup;

    @Json(name = "fCity")
    private String _fCity;

    @Json(name = "fState")
    private String _fState;

    @Json(name = "fPostalCode")
    private String _fPostalCode;

    @Json(name = "fCountry")
    private String _fCountry;

    @Json(name = "fFlags")
    private String _fFlags;

    @Json(name = "fAssignment")
    private String _fAssignment;

    @Json(name = "fConfirmation")
    private String _fConfirmation;

    @Json(name = "fFinancing")
    private String _fFinancing;

    @Json(name = "fGeo")
    private String _fGeo;

    @Json(name = "fSearch")
    private String _fSearch;

    public getWorkOrdersOptions() {
    }

    public void setList(String list) {
        _list = list;
    }

    public String getList() {
        return _list;
    }

    public getWorkOrdersOptions list(String list) {
        _list = list;
        return this;
    }

    public void setColumns(String columns) {
        _columns = columns;
    }

    public String getColumns() {
        return _columns;
    }

    public getWorkOrdersOptions columns(String columns) {
        _columns = columns;
        return this;
    }

    public void setPage(int page) {
        _page = page;
    }

    public int getPage() {
        return _page;
    }

    public getWorkOrdersOptions page(int page) {
        _page = page;
        return this;
    }

    public void setPerPage(int perPage) {
        _perPage = perPage;
    }

    public int getPerPage() {
        return _perPage;
    }

    public getWorkOrdersOptions perPage(int perPage) {
        _perPage = perPage;
        return this;
    }

    public void setView(String view) {
        _view = view;
    }

    public String getView() {
        return _view;
    }

    public getWorkOrdersOptions view(String view) {
        _view = view;
        return this;
    }

    public void setSticky(Boolean sticky) {
        _sticky = sticky;
    }

    public Boolean getSticky() {
        return _sticky;
    }

    public getWorkOrdersOptions sticky(Boolean sticky) {
        _sticky = sticky;
        return this;
    }

    public void setSort(String sort) {
        _sort = sort;
    }

    public String getSort() {
        return _sort;
    }

    public getWorkOrdersOptions sort(String sort) {
        _sort = sort;
        return this;
    }

    public void setOrder(String order) {
        _order = order;
    }

    public String getOrder() {
        return _order;
    }

    public getWorkOrdersOptions order(String order) {
        _order = order;
        return this;
    }

    public void setF(String f) {
        _f = f;
    }

    public String getF() {
        return _f;
    }

    public getWorkOrdersOptions f(String f) {
        _f = f;
        return this;
    }

    public void setFMaxApprovalTime(int fMaxApprovalTime) {
        _fMaxApprovalTime = fMaxApprovalTime;
    }

    public int getFMaxApprovalTime() {
        return _fMaxApprovalTime;
    }

    public getWorkOrdersOptions fMaxApprovalTime(int fMaxApprovalTime) {
        _fMaxApprovalTime = fMaxApprovalTime;
        return this;
    }

    public void setFRating(String fRating) {
        _fRating = fRating;
    }

    public String getFRating() {
        return _fRating;
    }

    public getWorkOrdersOptions fRating(String fRating) {
        _fRating = fRating;
        return this;
    }

    public void setFRequests(Boolean fRequests) {
        _fRequests = fRequests;
    }

    public Boolean getFRequests() {
        return _fRequests;
    }

    public getWorkOrdersOptions fRequests(Boolean fRequests) {
        _fRequests = fRequests;
        return this;
    }

    public void setFCounterOffers(Boolean fCounterOffers) {
        _fCounterOffers = fCounterOffers;
    }

    public Boolean getFCounterOffers() {
        return _fCounterOffers;
    }

    public getWorkOrdersOptions fCounterOffers(Boolean fCounterOffers) {
        _fCounterOffers = fCounterOffers;
        return this;
    }

    public void setFHourly(String fHourly) {
        _fHourly = fHourly;
    }

    public String getFHourly() {
        return _fHourly;
    }

    public getWorkOrdersOptions fHourly(String fHourly) {
        _fHourly = fHourly;
        return this;
    }

    public void setFFixed(String fFixed) {
        _fFixed = fFixed;
    }

    public String getFFixed() {
        return _fFixed;
    }

    public getWorkOrdersOptions fFixed(String fFixed) {
        _fFixed = fFixed;
        return this;
    }

    public void setFDevice(String fDevice) {
        _fDevice = fDevice;
    }

    public String getFDevice() {
        return _fDevice;
    }

    public getWorkOrdersOptions fDevice(String fDevice) {
        _fDevice = fDevice;
        return this;
    }

    public void setFPay(String fPay) {
        _fPay = fPay;
    }

    public String getFPay() {
        return _fPay;
    }

    public getWorkOrdersOptions fPay(String fPay) {
        _fPay = fPay;
        return this;
    }

    public void setFTemplates(String fTemplates) {
        _fTemplates = fTemplates;
    }

    public String getFTemplates() {
        return _fTemplates;
    }

    public getWorkOrdersOptions fTemplates(String fTemplates) {
        _fTemplates = fTemplates;
        return this;
    }

    public void setFTypeOfWork(String fTypeOfWork) {
        _fTypeOfWork = fTypeOfWork;
    }

    public String getFTypeOfWork() {
        return _fTypeOfWork;
    }

    public getWorkOrdersOptions fTypeOfWork(String fTypeOfWork) {
        _fTypeOfWork = fTypeOfWork;
        return this;
    }

    public void setFTimeZone(String fTimeZone) {
        _fTimeZone = fTimeZone;
    }

    public String getFTimeZone() {
        return _fTimeZone;
    }

    public getWorkOrdersOptions fTimeZone(String fTimeZone) {
        _fTimeZone = fTimeZone;
        return this;
    }

    public void setFMode(String fMode) {
        _fMode = fMode;
    }

    public String getFMode() {
        return _fMode;
    }

    public getWorkOrdersOptions fMode(String fMode) {
        _fMode = fMode;
        return this;
    }

    public void setFCompany(String fCompany) {
        _fCompany = fCompany;
    }

    public String getFCompany() {
        return _fCompany;
    }

    public getWorkOrdersOptions fCompany(String fCompany) {
        _fCompany = fCompany;
        return this;
    }

    public void setFWorkedWith(String fWorkedWith) {
        _fWorkedWith = fWorkedWith;
    }

    public String getFWorkedWith() {
        return _fWorkedWith;
    }

    public getWorkOrdersOptions fWorkedWith(String fWorkedWith) {
        _fWorkedWith = fWorkedWith;
        return this;
    }

    public void setFManager(String fManager) {
        _fManager = fManager;
    }

    public String getFManager() {
        return _fManager;
    }

    public getWorkOrdersOptions fManager(String fManager) {
        _fManager = fManager;
        return this;
    }

    public void setFClient(String fClient) {
        _fClient = fClient;
    }

    public String getFClient() {
        return _fClient;
    }

    public getWorkOrdersOptions fClient(String fClient) {
        _fClient = fClient;
        return this;
    }

    public void setFProject(String fProject) {
        _fProject = fProject;
    }

    public String getFProject() {
        return _fProject;
    }

    public getWorkOrdersOptions fProject(String fProject) {
        _fProject = fProject;
        return this;
    }

    public void setFApprovalWindow(String fApprovalWindow) {
        _fApprovalWindow = fApprovalWindow;
    }

    public String getFApprovalWindow() {
        return _fApprovalWindow;
    }

    public getWorkOrdersOptions fApprovalWindow(String fApprovalWindow) {
        _fApprovalWindow = fApprovalWindow;
        return this;
    }

    public void setFReviewWindow(String fReviewWindow) {
        _fReviewWindow = fReviewWindow;
    }

    public String getFReviewWindow() {
        return _fReviewWindow;
    }

    public getWorkOrdersOptions fReviewWindow(String fReviewWindow) {
        _fReviewWindow = fReviewWindow;
        return this;
    }

    public void setFNetwork(String fNetwork) {
        _fNetwork = fNetwork;
    }

    public String getFNetwork() {
        return _fNetwork;
    }

    public getWorkOrdersOptions fNetwork(String fNetwork) {
        _fNetwork = fNetwork;
        return this;
    }

    public void setFAutoAssign(String fAutoAssign) {
        _fAutoAssign = fAutoAssign;
    }

    public String getFAutoAssign() {
        return _fAutoAssign;
    }

    public getWorkOrdersOptions fAutoAssign(String fAutoAssign) {
        _fAutoAssign = fAutoAssign;
        return this;
    }

    public void setFSchedule(String fSchedule) {
        _fSchedule = fSchedule;
    }

    public String getFSchedule() {
        return _fSchedule;
    }

    public getWorkOrdersOptions fSchedule(String fSchedule) {
        _fSchedule = fSchedule;
        return this;
    }

    public void setFCreated(String fCreated) {
        _fCreated = fCreated;
    }

    public String getFCreated() {
        return _fCreated;
    }

    public getWorkOrdersOptions fCreated(String fCreated) {
        _fCreated = fCreated;
        return this;
    }

    public void setFPublished(String fPublished) {
        _fPublished = fPublished;
    }

    public String getFPublished() {
        return _fPublished;
    }

    public getWorkOrdersOptions fPublished(String fPublished) {
        _fPublished = fPublished;
        return this;
    }

    public void setFRouted(String fRouted) {
        _fRouted = fRouted;
    }

    public String getFRouted() {
        return _fRouted;
    }

    public getWorkOrdersOptions fRouted(String fRouted) {
        _fRouted = fRouted;
        return this;
    }

    public void setFPublishedRouted(String fPublishedRouted) {
        _fPublishedRouted = fPublishedRouted;
    }

    public String getFPublishedRouted() {
        return _fPublishedRouted;
    }

    public getWorkOrdersOptions fPublishedRouted(String fPublishedRouted) {
        _fPublishedRouted = fPublishedRouted;
        return this;
    }

    public void setFCompleted(String fCompleted) {
        _fCompleted = fCompleted;
    }

    public String getFCompleted() {
        return _fCompleted;
    }

    public getWorkOrdersOptions fCompleted(String fCompleted) {
        _fCompleted = fCompleted;
        return this;
    }

    public void setFApprovedCancelled(String fApprovedCancelled) {
        _fApprovedCancelled = fApprovedCancelled;
    }

    public String getFApprovedCancelled() {
        return _fApprovedCancelled;
    }

    public getWorkOrdersOptions fApprovedCancelled(String fApprovedCancelled) {
        _fApprovedCancelled = fApprovedCancelled;
        return this;
    }

    public void setFConfirmed(String fConfirmed) {
        _fConfirmed = fConfirmed;
    }

    public String getFConfirmed() {
        return _fConfirmed;
    }

    public getWorkOrdersOptions fConfirmed(String fConfirmed) {
        _fConfirmed = fConfirmed;
        return this;
    }

    public void setFAssigned(String fAssigned) {
        _fAssigned = fAssigned;
    }

    public String getFAssigned() {
        return _fAssigned;
    }

    public getWorkOrdersOptions fAssigned(String fAssigned) {
        _fAssigned = fAssigned;
        return this;
    }

    public void setFSavedLocation(String fSavedLocation) {
        _fSavedLocation = fSavedLocation;
    }

    public String getFSavedLocation() {
        return _fSavedLocation;
    }

    public getWorkOrdersOptions fSavedLocation(String fSavedLocation) {
        _fSavedLocation = fSavedLocation;
        return this;
    }

    public void setFSavedLocationGroup(String fSavedLocationGroup) {
        _fSavedLocationGroup = fSavedLocationGroup;
    }

    public String getFSavedLocationGroup() {
        return _fSavedLocationGroup;
    }

    public getWorkOrdersOptions fSavedLocationGroup(String fSavedLocationGroup) {
        _fSavedLocationGroup = fSavedLocationGroup;
        return this;
    }

    public void setFCity(String fCity) {
        _fCity = fCity;
    }

    public String getFCity() {
        return _fCity;
    }

    public getWorkOrdersOptions fCity(String fCity) {
        _fCity = fCity;
        return this;
    }

    public void setFState(String fState) {
        _fState = fState;
    }

    public String getFState() {
        return _fState;
    }

    public getWorkOrdersOptions fState(String fState) {
        _fState = fState;
        return this;
    }

    public void setFPostalCode(String fPostalCode) {
        _fPostalCode = fPostalCode;
    }

    public String getFPostalCode() {
        return _fPostalCode;
    }

    public getWorkOrdersOptions fPostalCode(String fPostalCode) {
        _fPostalCode = fPostalCode;
        return this;
    }

    public void setFCountry(String fCountry) {
        _fCountry = fCountry;
    }

    public String getFCountry() {
        return _fCountry;
    }

    public getWorkOrdersOptions fCountry(String fCountry) {
        _fCountry = fCountry;
        return this;
    }

    public void setFFlags(String fFlags) {
        _fFlags = fFlags;
    }

    public String getFFlags() {
        return _fFlags;
    }

    public getWorkOrdersOptions fFlags(String fFlags) {
        _fFlags = fFlags;
        return this;
    }

    public void setFAssignment(String fAssignment) {
        _fAssignment = fAssignment;
    }

    public String getFAssignment() {
        return _fAssignment;
    }

    public getWorkOrdersOptions fAssignment(String fAssignment) {
        _fAssignment = fAssignment;
        return this;
    }

    public void setFConfirmation(String fConfirmation) {
        _fConfirmation = fConfirmation;
    }

    public String getFConfirmation() {
        return _fConfirmation;
    }

    public getWorkOrdersOptions fConfirmation(String fConfirmation) {
        _fConfirmation = fConfirmation;
        return this;
    }

    public void setFFinancing(String fFinancing) {
        _fFinancing = fFinancing;
    }

    public String getFFinancing() {
        return _fFinancing;
    }

    public getWorkOrdersOptions fFinancing(String fFinancing) {
        _fFinancing = fFinancing;
        return this;
    }

    public void setFGeo(String fGeo) {
        _fGeo = fGeo;
    }

    public String getFGeo() {
        return _fGeo;
    }

    public getWorkOrdersOptions fGeo(String fGeo) {
        _fGeo = fGeo;
        return this;
    }

    public void setFSearch(String fSearch) {
        _fSearch = fSearch;
    }

    public String getFSearch() {
        return _fSearch;
    }

    public getWorkOrdersOptions fSearch(String fSearch) {
        _fSearch = fSearch;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static getWorkOrdersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(getWorkOrdersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(getWorkOrdersOptions getWorkOrdersOptions) {
        try {
            return Serializer.serializeObject(getWorkOrdersOptions);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<getWorkOrdersOptions> CREATOR = new Parcelable.Creator<getWorkOrdersOptions>() {

        @Override
        public getWorkOrdersOptions createFromParcel(Parcel source) {
            try {
                return getWorkOrdersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public getWorkOrdersOptions[] newArray(int size) {
            return new getWorkOrdersOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
