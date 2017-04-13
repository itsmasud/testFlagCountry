package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.v2.data.model.*;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class GetWorkOrdersOptions implements Parcelable {
    private static final String TAG = "GetWorkOrdersOptions";

    @Json(name = "list")
    private String _list;

    @Json(name = "columns")
    private String _columns = "work_order_id,title,type_of_work,company,location,bundle,pay,schedule,actions,time_logs,status,requests,eta";

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

    @Json(name = "fFlightboardTomorrow")
    private Boolean _fFlightboardTomorrow;

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

    @Json(name = "fLocationRadius")
    private String[] _fLocationRadius;

    @Json(name = "fRemoteWork")
    private Boolean _fRemoteWork;

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

    public boolean isListSet() {
        return _list != null;
    }

    public String getListUrlParam() {
        return "list=" + _list;
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

    public boolean isColumnsSet() {
        return _columns != null;
    }

    public String getColumnsUrlParam() {
        return "columns=" + _columns;
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

    public boolean isPageSet() {
        return _page != null;
    }

    public String getPageUrlParam() {
        return "page=" + _page;
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

    public boolean isPerPageSet() {
        return _perPage != null;
    }

    public String getPerPageUrlParam() {
        return "per_page=" + _perPage;
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

    public boolean isViewSet() {
        return _view != null;
    }

    public String getViewUrlParam() {
        return "view=" + _view;
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

    public boolean isStickySet() {
        return _sticky != null;
    }

    public String getStickyUrlParam() {
        return "sticky=" + _sticky;
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

    public boolean isSortSet() {
        return _sort != null;
    }

    public String getSortUrlParam() {
        return "sort=" + _sort;
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

    public boolean isOrderSet() {
        return _order != null;
    }

    public String getOrderUrlParam() {
        return "order=" + _order;
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

    public boolean isFSet() {
        return _f != null;
    }

    public String getFUrlParam() {
        return "f_=" + _f;
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

    public boolean isFMaxApprovalTimeSet() {
        return _fMaxApprovalTime != null;
    }

    public String getFMaxApprovalTimeUrlParam() {
        return "f_max_approval_time=" + _fMaxApprovalTime;
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

    public boolean isFRatingSet() {
        return _fRating != null;
    }

    public String getFRatingUrlParam() {
        return "f_rating=" + _fRating;
    }

    public void setFFlightboardTomorrow(Boolean fFlightboardTomorrow) {
        _fFlightboardTomorrow = fFlightboardTomorrow;
    }

    public Boolean getFFlightboardTomorrow() {
        return _fFlightboardTomorrow;
    }

    public GetWorkOrdersOptions fFlightboardTomorrow(Boolean fFlightboardTomorrow) {
        _fFlightboardTomorrow = fFlightboardTomorrow;
        return this;
    }

    public boolean isFFlightboardTomorrowSet() {
        return _fFlightboardTomorrow != null;
    }

    public String getFFlightboardTomorrowUrlParam() {
        return "f_flightboard_tomorrow=" + _fFlightboardTomorrow;
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

    public boolean isFRequestsSet() {
        return _fRequests != null;
    }

    public String getFRequestsUrlParam() {
        return "f_requests=" + _fRequests;
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

    public boolean isFCounterOffersSet() {
        return _fCounterOffers != null;
    }

    public String getFCounterOffersUrlParam() {
        return "f_counter_offers=" + _fCounterOffers;
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

    public boolean isFHourlySet() {
        return _fHourly != null;
    }

    public String getFHourlyUrlParam() {
        return "f_hourly=" + _fHourly;
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

    public boolean isFFixedSet() {
        return _fFixed != null;
    }

    public String getFFixedUrlParam() {
        return "f_fixed=" + _fFixed;
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

    public boolean isFDeviceSet() {
        return _fDevice != null;
    }

    public String getFDeviceUrlParam() {
        return "f_device=" + _fDevice;
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

    public boolean isFPaySet() {
        return _fPay != null;
    }

    public String getFPayUrlParam() {
        return "f_pay=" + _fPay;
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

    public boolean isFTemplatesSet() {
        return _fTemplates != null;
    }

    public String getFTemplatesUrlParam() {
        return "f_templates=" + _fTemplates;
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

    public boolean isFTypeOfWorkSet() {
        return _fTypeOfWork != null;
    }

    public String getFTypeOfWorkUrlParam() {
        return "f_type_of_work=" + _fTypeOfWork;
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

    public boolean isFTimeZoneSet() {
        return _fTimeZone != null;
    }

    public String getFTimeZoneUrlParam() {
        return "f_time_zone=" + _fTimeZone;
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

    public boolean isFModeSet() {
        return _fMode != null;
    }

    public String getFModeUrlParam() {
        return "f_mode=" + _fMode;
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

    public boolean isFCompanySet() {
        return _fCompany != null;
    }

    public String getFCompanyUrlParam() {
        return "f_company=" + _fCompany;
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

    public boolean isFWorkedWithSet() {
        return _fWorkedWith != null;
    }

    public String getFWorkedWithUrlParam() {
        return "f_worked_with=" + _fWorkedWith;
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

    public boolean isFManagerSet() {
        return _fManager != null;
    }

    public String getFManagerUrlParam() {
        return "f_manager=" + _fManager;
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

    public boolean isFClientSet() {
        return _fClient != null;
    }

    public String getFClientUrlParam() {
        return "f_client=" + _fClient;
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

    public boolean isFProjectSet() {
        return _fProject != null;
    }

    public String getFProjectUrlParam() {
        return "f_project=" + _fProject;
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

    public boolean isFApprovalWindowSet() {
        return _fApprovalWindow != null;
    }

    public String getFApprovalWindowUrlParam() {
        return "f_approval_window=" + _fApprovalWindow;
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

    public boolean isFReviewWindowSet() {
        return _fReviewWindow != null;
    }

    public String getFReviewWindowUrlParam() {
        return "f_review_window=" + _fReviewWindow;
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

    public boolean isFNetworkSet() {
        return _fNetwork != null;
    }

    public String getFNetworkUrlParam() {
        return "f_network=" + _fNetwork;
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

    public boolean isFAutoAssignSet() {
        return _fAutoAssign != null;
    }

    public String getFAutoAssignUrlParam() {
        return "f_auto_assign=" + _fAutoAssign;
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

    public boolean isFScheduleSet() {
        return _fSchedule != null;
    }

    public String getFScheduleUrlParam() {
        return "f_schedule=" + _fSchedule;
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

    public boolean isFCreatedSet() {
        return _fCreated != null;
    }

    public String getFCreatedUrlParam() {
        return "f_created=" + _fCreated;
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

    public boolean isFPublishedSet() {
        return _fPublished != null;
    }

    public String getFPublishedUrlParam() {
        return "f_published=" + _fPublished;
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

    public boolean isFRoutedSet() {
        return _fRouted != null;
    }

    public String getFRoutedUrlParam() {
        return "f_routed=" + _fRouted;
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

    public boolean isFPublishedRoutedSet() {
        return _fPublishedRouted != null;
    }

    public String getFPublishedRoutedUrlParam() {
        return "f_published_routed=" + _fPublishedRouted;
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

    public boolean isFCompletedSet() {
        return _fCompleted != null;
    }

    public String getFCompletedUrlParam() {
        return "f_completed=" + _fCompleted;
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

    public boolean isFApprovedCancelledSet() {
        return _fApprovedCancelled != null;
    }

    public String getFApprovedCancelledUrlParam() {
        return "f_approved_cancelled=" + _fApprovedCancelled;
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

    public boolean isFConfirmedSet() {
        return _fConfirmed != null;
    }

    public String getFConfirmedUrlParam() {
        return "f_confirmed=" + _fConfirmed;
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

    public boolean isFAssignedSet() {
        return _fAssigned != null;
    }

    public String getFAssignedUrlParam() {
        return "f_assigned=" + _fAssigned;
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

    public boolean isFSavedLocationSet() {
        return _fSavedLocation != null;
    }

    public String getFSavedLocationUrlParam() {
        return "f_saved_location=" + _fSavedLocation;
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

    public boolean isFSavedLocationGroupSet() {
        return _fSavedLocationGroup != null;
    }

    public String getFSavedLocationGroupUrlParam() {
        return "f_saved_location_group=" + _fSavedLocationGroup;
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

    public boolean isFCitySet() {
        return _fCity != null;
    }

    public String getFCityUrlParam() {
        return "f_city=" + _fCity;
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

    public boolean isFStateSet() {
        return _fState != null;
    }

    public String getFStateUrlParam() {
        return "f_state=" + _fState;
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

    public boolean isFPostalCodeSet() {
        return _fPostalCode != null;
    }

    public String getFPostalCodeUrlParam() {
        return "f_postal_code=" + _fPostalCode;
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

    public boolean isFCountrySet() {
        return _fCountry != null;
    }

    public String getFCountryUrlParam() {
        return "f_country=" + _fCountry;
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

    public boolean isFFlagsSet() {
        return _fFlags != null;
    }

    public String getFFlagsUrlParam() {
        return "f_flags=" + _fFlags;
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

    public boolean isFAssignmentSet() {
        return _fAssignment != null;
    }

    public String getFAssignmentUrlParam() {
        return "f_assignment=" + _fAssignment;
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

    public boolean isFConfirmationSet() {
        return _fConfirmation != null;
    }

    public String getFConfirmationUrlParam() {
        return "f_confirmation=" + _fConfirmation;
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

    public boolean isFFinancingSet() {
        return _fFinancing != null;
    }

    public String getFFinancingUrlParam() {
        return "f_financing=" + _fFinancing;
    }

    public void setFLocationRadius(String[] fLocationRadius) {
        _fLocationRadius = fLocationRadius;
    }

    public String[] getFLocationRadius() {
        return _fLocationRadius;
    }

    public GetWorkOrdersOptions fLocationRadius(String[] fLocationRadius) {
        _fLocationRadius = fLocationRadius;
        return this;
    }

    public boolean isFLocationRadiusSet() {
        return _fLocationRadius != null;
    }

    public String getFLocationRadiusUrlParam() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String val : _fLocationRadius) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            sb.append("f_location_radius[]=" + val);
        }

        return sb.toString();
    }

    public void setFRemoteWork(Boolean fRemoteWork) {
        _fRemoteWork = fRemoteWork;
    }

    public Boolean getFRemoteWork() {
        return _fRemoteWork;
    }

    public GetWorkOrdersOptions fRemoteWork(Boolean fRemoteWork) {
        _fRemoteWork = fRemoteWork;
        return this;
    }

    public boolean isFRemoteWorkSet() {
        return _fRemoteWork != null;
    }

    public String getFRemoteWorkUrlParam() {
        return "f_remote_work=" + _fRemoteWork;
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

    public boolean isFSearchSet() {
        return _fSearch != null;
    }

    public String getFSearchUrlParam() {
        return "f_search=" + _fSearch;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetWorkOrdersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetWorkOrdersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
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
