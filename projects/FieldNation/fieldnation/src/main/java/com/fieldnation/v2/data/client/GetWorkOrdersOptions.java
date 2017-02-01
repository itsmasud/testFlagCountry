package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class GetWorkOrdersOptions implements Parcelable {
    private static final String TAG = "GetWorkOrdersOptions";

    @Json(name = "list")
    private String _list;

    @Json(name = "columns")
    private String _columns = "work_order_id,title,type_of_work,company,location,bundle,pay,schedule,actions";

    @Json(name = "page")
    private Integer _page;

    @Json(name = "perPage")
    private Integer _perPage;

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
    private Integer _fMaxApprovalTime;

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

    public GetWorkOrdersOptions() {
    }

    public void setList(String list) {
        _list = list;
    }

    public String getList() {
        return _list;
    }

    public GetWorkOrdersOptions list(String list) {
        _list = list;
        return this;
    }

    public void setColumns(String columns) {
        _columns = columns;
    }

    public String getColumns() {
        return _columns;
    }

    public GetWorkOrdersOptions columns(String columns) {
        _columns = columns;
        return this;
    }

    public void setPage(Integer page) {
        _page = page;
    }

    public Integer getPage() {
        return _page;
    }

    public GetWorkOrdersOptions page(Integer page) {
        _page = page;
        return this;
    }

    public void setPerPage(Integer perPage) {
        _perPage = perPage;
    }

    public Integer getPerPage() {
        return _perPage;
    }

    public GetWorkOrdersOptions perPage(Integer perPage) {
        _perPage = perPage;
        return this;
    }

    public void setView(String view) {
        _view = view;
    }

    public String getView() {
        return _view;
    }

    public GetWorkOrdersOptions view(String view) {
        _view = view;
        return this;
    }

    public void setSticky(Boolean sticky) {
        _sticky = sticky;
    }

    public Boolean getSticky() {
        return _sticky;
    }

    public GetWorkOrdersOptions sticky(Boolean sticky) {
        _sticky = sticky;
        return this;
    }

    public void setSort(String sort) {
        _sort = sort;
    }

    public String getSort() {
        return _sort;
    }

    public GetWorkOrdersOptions sort(String sort) {
        _sort = sort;
        return this;
    }

    public void setOrder(String order) {
        _order = order;
    }

    public String getOrder() {
        return _order;
    }

    public GetWorkOrdersOptions order(String order) {
        _order = order;
        return this;
    }

    public void setF(String f) {
        _f = f;
    }

    public String getF() {
        return _f;
    }

    public GetWorkOrdersOptions f(String f) {
        _f = f;
        return this;
    }

    public void setFMaxApprovalTime(Integer fMaxApprovalTime) {
        _fMaxApprovalTime = fMaxApprovalTime;
    }

    public Integer getFMaxApprovalTime() {
        return _fMaxApprovalTime;
    }

    public GetWorkOrdersOptions fMaxApprovalTime(Integer fMaxApprovalTime) {
        _fMaxApprovalTime = fMaxApprovalTime;
        return this;
    }

    public void setFRating(String fRating) {
        _fRating = fRating;
    }

    public String getFRating() {
        return _fRating;
    }

    public GetWorkOrdersOptions fRating(String fRating) {
        _fRating = fRating;
        return this;
    }

    public void setFRequests(Boolean fRequests) {
        _fRequests = fRequests;
    }

    public Boolean getFRequests() {
        return _fRequests;
    }

    public GetWorkOrdersOptions fRequests(Boolean fRequests) {
        _fRequests = fRequests;
        return this;
    }

    public void setFCounterOffers(Boolean fCounterOffers) {
        _fCounterOffers = fCounterOffers;
    }

    public Boolean getFCounterOffers() {
        return _fCounterOffers;
    }

    public GetWorkOrdersOptions fCounterOffers(Boolean fCounterOffers) {
        _fCounterOffers = fCounterOffers;
        return this;
    }

    public void setFHourly(String fHourly) {
        _fHourly = fHourly;
    }

    public String getFHourly() {
        return _fHourly;
    }

    public GetWorkOrdersOptions fHourly(String fHourly) {
        _fHourly = fHourly;
        return this;
    }

    public void setFFixed(String fFixed) {
        _fFixed = fFixed;
    }

    public String getFFixed() {
        return _fFixed;
    }

    public GetWorkOrdersOptions fFixed(String fFixed) {
        _fFixed = fFixed;
        return this;
    }

    public void setFDevice(String fDevice) {
        _fDevice = fDevice;
    }

    public String getFDevice() {
        return _fDevice;
    }

    public GetWorkOrdersOptions fDevice(String fDevice) {
        _fDevice = fDevice;
        return this;
    }

    public void setFPay(String fPay) {
        _fPay = fPay;
    }

    public String getFPay() {
        return _fPay;
    }

    public GetWorkOrdersOptions fPay(String fPay) {
        _fPay = fPay;
        return this;
    }

    public void setFTemplates(String fTemplates) {
        _fTemplates = fTemplates;
    }

    public String getFTemplates() {
        return _fTemplates;
    }

    public GetWorkOrdersOptions fTemplates(String fTemplates) {
        _fTemplates = fTemplates;
        return this;
    }

    public void setFTypeOfWork(String fTypeOfWork) {
        _fTypeOfWork = fTypeOfWork;
    }

    public String getFTypeOfWork() {
        return _fTypeOfWork;
    }

    public GetWorkOrdersOptions fTypeOfWork(String fTypeOfWork) {
        _fTypeOfWork = fTypeOfWork;
        return this;
    }

    public void setFTimeZone(String fTimeZone) {
        _fTimeZone = fTimeZone;
    }

    public String getFTimeZone() {
        return _fTimeZone;
    }

    public GetWorkOrdersOptions fTimeZone(String fTimeZone) {
        _fTimeZone = fTimeZone;
        return this;
    }

    public void setFMode(String fMode) {
        _fMode = fMode;
    }

    public String getFMode() {
        return _fMode;
    }

    public GetWorkOrdersOptions fMode(String fMode) {
        _fMode = fMode;
        return this;
    }

    public void setFCompany(String fCompany) {
        _fCompany = fCompany;
    }

    public String getFCompany() {
        return _fCompany;
    }

    public GetWorkOrdersOptions fCompany(String fCompany) {
        _fCompany = fCompany;
        return this;
    }

    public void setFWorkedWith(String fWorkedWith) {
        _fWorkedWith = fWorkedWith;
    }

    public String getFWorkedWith() {
        return _fWorkedWith;
    }

    public GetWorkOrdersOptions fWorkedWith(String fWorkedWith) {
        _fWorkedWith = fWorkedWith;
        return this;
    }

    public void setFManager(String fManager) {
        _fManager = fManager;
    }

    public String getFManager() {
        return _fManager;
    }

    public GetWorkOrdersOptions fManager(String fManager) {
        _fManager = fManager;
        return this;
    }

    public void setFClient(String fClient) {
        _fClient = fClient;
    }

    public String getFClient() {
        return _fClient;
    }

    public GetWorkOrdersOptions fClient(String fClient) {
        _fClient = fClient;
        return this;
    }

    public void setFProject(String fProject) {
        _fProject = fProject;
    }

    public String getFProject() {
        return _fProject;
    }

    public GetWorkOrdersOptions fProject(String fProject) {
        _fProject = fProject;
        return this;
    }

    public void setFApprovalWindow(String fApprovalWindow) {
        _fApprovalWindow = fApprovalWindow;
    }

    public String getFApprovalWindow() {
        return _fApprovalWindow;
    }

    public GetWorkOrdersOptions fApprovalWindow(String fApprovalWindow) {
        _fApprovalWindow = fApprovalWindow;
        return this;
    }

    public void setFReviewWindow(String fReviewWindow) {
        _fReviewWindow = fReviewWindow;
    }

    public String getFReviewWindow() {
        return _fReviewWindow;
    }

    public GetWorkOrdersOptions fReviewWindow(String fReviewWindow) {
        _fReviewWindow = fReviewWindow;
        return this;
    }

    public void setFNetwork(String fNetwork) {
        _fNetwork = fNetwork;
    }

    public String getFNetwork() {
        return _fNetwork;
    }

    public GetWorkOrdersOptions fNetwork(String fNetwork) {
        _fNetwork = fNetwork;
        return this;
    }

    public void setFAutoAssign(String fAutoAssign) {
        _fAutoAssign = fAutoAssign;
    }

    public String getFAutoAssign() {
        return _fAutoAssign;
    }

    public GetWorkOrdersOptions fAutoAssign(String fAutoAssign) {
        _fAutoAssign = fAutoAssign;
        return this;
    }

    public void setFSchedule(String fSchedule) {
        _fSchedule = fSchedule;
    }

    public String getFSchedule() {
        return _fSchedule;
    }

    public GetWorkOrdersOptions fSchedule(String fSchedule) {
        _fSchedule = fSchedule;
        return this;
    }

    public void setFCreated(String fCreated) {
        _fCreated = fCreated;
    }

    public String getFCreated() {
        return _fCreated;
    }

    public GetWorkOrdersOptions fCreated(String fCreated) {
        _fCreated = fCreated;
        return this;
    }

    public void setFPublished(String fPublished) {
        _fPublished = fPublished;
    }

    public String getFPublished() {
        return _fPublished;
    }

    public GetWorkOrdersOptions fPublished(String fPublished) {
        _fPublished = fPublished;
        return this;
    }

    public void setFRouted(String fRouted) {
        _fRouted = fRouted;
    }

    public String getFRouted() {
        return _fRouted;
    }

    public GetWorkOrdersOptions fRouted(String fRouted) {
        _fRouted = fRouted;
        return this;
    }

    public void setFPublishedRouted(String fPublishedRouted) {
        _fPublishedRouted = fPublishedRouted;
    }

    public String getFPublishedRouted() {
        return _fPublishedRouted;
    }

    public GetWorkOrdersOptions fPublishedRouted(String fPublishedRouted) {
        _fPublishedRouted = fPublishedRouted;
        return this;
    }

    public void setFCompleted(String fCompleted) {
        _fCompleted = fCompleted;
    }

    public String getFCompleted() {
        return _fCompleted;
    }

    public GetWorkOrdersOptions fCompleted(String fCompleted) {
        _fCompleted = fCompleted;
        return this;
    }

    public void setFApprovedCancelled(String fApprovedCancelled) {
        _fApprovedCancelled = fApprovedCancelled;
    }

    public String getFApprovedCancelled() {
        return _fApprovedCancelled;
    }

    public GetWorkOrdersOptions fApprovedCancelled(String fApprovedCancelled) {
        _fApprovedCancelled = fApprovedCancelled;
        return this;
    }

    public void setFConfirmed(String fConfirmed) {
        _fConfirmed = fConfirmed;
    }

    public String getFConfirmed() {
        return _fConfirmed;
    }

    public GetWorkOrdersOptions fConfirmed(String fConfirmed) {
        _fConfirmed = fConfirmed;
        return this;
    }

    public void setFAssigned(String fAssigned) {
        _fAssigned = fAssigned;
    }

    public String getFAssigned() {
        return _fAssigned;
    }

    public GetWorkOrdersOptions fAssigned(String fAssigned) {
        _fAssigned = fAssigned;
        return this;
    }

    public void setFSavedLocation(String fSavedLocation) {
        _fSavedLocation = fSavedLocation;
    }

    public String getFSavedLocation() {
        return _fSavedLocation;
    }

    public GetWorkOrdersOptions fSavedLocation(String fSavedLocation) {
        _fSavedLocation = fSavedLocation;
        return this;
    }

    public void setFSavedLocationGroup(String fSavedLocationGroup) {
        _fSavedLocationGroup = fSavedLocationGroup;
    }

    public String getFSavedLocationGroup() {
        return _fSavedLocationGroup;
    }

    public GetWorkOrdersOptions fSavedLocationGroup(String fSavedLocationGroup) {
        _fSavedLocationGroup = fSavedLocationGroup;
        return this;
    }

    public void setFCity(String fCity) {
        _fCity = fCity;
    }

    public String getFCity() {
        return _fCity;
    }

    public GetWorkOrdersOptions fCity(String fCity) {
        _fCity = fCity;
        return this;
    }

    public void setFState(String fState) {
        _fState = fState;
    }

    public String getFState() {
        return _fState;
    }

    public GetWorkOrdersOptions fState(String fState) {
        _fState = fState;
        return this;
    }

    public void setFPostalCode(String fPostalCode) {
        _fPostalCode = fPostalCode;
    }

    public String getFPostalCode() {
        return _fPostalCode;
    }

    public GetWorkOrdersOptions fPostalCode(String fPostalCode) {
        _fPostalCode = fPostalCode;
        return this;
    }

    public void setFCountry(String fCountry) {
        _fCountry = fCountry;
    }

    public String getFCountry() {
        return _fCountry;
    }

    public GetWorkOrdersOptions fCountry(String fCountry) {
        _fCountry = fCountry;
        return this;
    }

    public void setFFlags(String fFlags) {
        _fFlags = fFlags;
    }

    public String getFFlags() {
        return _fFlags;
    }

    public GetWorkOrdersOptions fFlags(String fFlags) {
        _fFlags = fFlags;
        return this;
    }

    public void setFAssignment(String fAssignment) {
        _fAssignment = fAssignment;
    }

    public String getFAssignment() {
        return _fAssignment;
    }

    public GetWorkOrdersOptions fAssignment(String fAssignment) {
        _fAssignment = fAssignment;
        return this;
    }

    public void setFConfirmation(String fConfirmation) {
        _fConfirmation = fConfirmation;
    }

    public String getFConfirmation() {
        return _fConfirmation;
    }

    public GetWorkOrdersOptions fConfirmation(String fConfirmation) {
        _fConfirmation = fConfirmation;
        return this;
    }

    public void setFFinancing(String fFinancing) {
        _fFinancing = fFinancing;
    }

    public String getFFinancing() {
        return _fFinancing;
    }

    public GetWorkOrdersOptions fFinancing(String fFinancing) {
        _fFinancing = fFinancing;
        return this;
    }

    public void setFGeo(String fGeo) {
        _fGeo = fGeo;
    }

    public String getFGeo() {
        return _fGeo;
    }

    public GetWorkOrdersOptions fGeo(String fGeo) {
        _fGeo = fGeo;
        return this;
    }

    public void setFSearch(String fSearch) {
        _fSearch = fSearch;
    }

    public String getFSearch() {
        return _fSearch;
    }

    public GetWorkOrdersOptions fSearch(String fSearch) {
        _fSearch = fSearch;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetWorkOrdersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetWorkOrdersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetWorkOrdersOptions getWorkOrdersOptions) {
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
    public static final Parcelable.Creator<GetWorkOrdersOptions> CREATOR = new Parcelable.Creator<GetWorkOrdersOptions>() {

        @Override
        public GetWorkOrdersOptions createFromParcel(Parcel source) {
            try {
                return GetWorkOrdersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetWorkOrdersOptions[] newArray(int size) {
            return new GetWorkOrdersOptions[size];
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
