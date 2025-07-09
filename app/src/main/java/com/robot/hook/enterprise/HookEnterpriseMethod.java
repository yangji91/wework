package com.robot.hook.enterprise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.robot.common.Global;
import com.robot.entity.EnterpriseEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;
import java.nio.charset.StandardCharsets;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/**
 * 获取企业id
 */
public class HookEnterpriseMethod extends HookBaseMethod<EnterpriseEntity> {

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<EnterpriseEntity> callBack) {

    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<EnterpriseEntity> paramCall) {
        boolean succ = false;
//        Class<?> clazzCC = RobotHelpers.findClassIfExists("com.tencent.wework.enterprisemgr.api.IEnterprise$-CC",loadPackageParam.classLoader);
//        Object objEnterprise = RobotHelpers.callStaticMethod(clazzCC,"get");
//        long corpId = (long) RobotHelpers.callMethod(objEnterprise,"getCurrentCorpId");
//        String corpName = (String) RobotHelpers.callMethod(objEnterprise,"getCurrentCorpFullName");
//        Object objEnterPriseEntity = RobotHelpers.callMethod(objEnterprise,"getSelectedApplyEnterpriseEntity");
//        long cggId = (long) RobotHelpers.callMethod(objEnterPriseEntity,"cgg");
//        String strCqD = (String) RobotHelpers.callMethod(objEnterPriseEntity,"cqD");
//        if(paramCall != null){
//            EnterpriseEntity paramEntity = new EnterpriseEntity();
//            paramEntity.corpId = corpId;
//            paramEntity.corpName = corpName;
//            ResEntity resEntity = ResEntity.genSucc(paramEntity,-1);
//            paramCall.onCall(resEntity);
//            MyLog.debug(TAG,"[onInvokeMethod]" + " cggId:" + cggId + " cqD:" + strCqD + " corpId:" + corpId + " corpName:" + corpName);
//        }
        //com.tencent.wework.foundation.logic.GetProfileManager
        //com.tencent.wework.foundation.logic.Application
        //com.tencent.wework.foundation.logic.Profile
        //com.tencent.wework.foundation.model.pb.Corpinfo.CorpConfig
        Class<?> clazz = RobotHelpers.findClassIfExists(KeyConst.C_Application,loadPackageParam.classLoader);
        Object objIntance = RobotHelpers.callStaticMethod(clazz,KeyConst.C_Application_getInstance);
        Object objProfileManager = RobotHelpers.callMethod(objIntance,KeyConst.M_Application_GetProfileManager);
        long corpId = (long) RobotHelpers.callMethod(objIntance,KeyConst.M_Application_getCorpId);
        long vId = (long) RobotHelpers.callMethod(objIntance,KeyConst.M_Application_getVid);
        Object objCurrentProfile = RobotHelpers.callMethod(objProfileManager,KeyConst.M_Application_GetProfileManager_GetCurrentProfile);
        Object objCorpCfg = RobotHelpers.callMethod(objCurrentProfile,KeyConst.M_Application_GetProfileManager_getCorpInfo);
//        Gson gson = new Gson();
//        String str = gson.toJson(objCorpCfg);
//        String strResult = toPrettyFormat(str);
//        System.out.println(strResult);
        long bbsId = RobotHelpers.getLongField(objCorpCfg,KeyConst.F_Application_GetProfileManager_bbsId);
        byte[] bbsName = (byte[]) RobotHelpers.getObjectField(objCorpCfg,KeyConst.F_Application_GetProfileManager_bbsName);
        String strBbsName = null;
        strBbsName = new String(bbsName, StandardCharsets.UTF_8);
        String name = (String) RobotHelpers.getObjectField(objCorpCfg,KeyConst.F_Application_GetProfileManager_name);
//        long id = RobotHelpers.getLongField(objCorpCfg,"id");
        String corpFullName = (String) RobotHelpers.getObjectField(objCorpCfg,KeyConst.F_Application_GetProfileManager_corpFullName);
        Global.bbsId = bbsId;
        Global.bbsName = strBbsName;

        //skfly add begin
        Global.corpId = corpId;
        Global.corpName = name;
        Global.vId = vId;
        //skfly add end

        if(paramCall != null){
             EnterpriseEntity paramEntity = new  EnterpriseEntity();
            paramEntity.bbsName = strBbsName;
            paramEntity.bbsId = bbsId;
            paramEntity.corpId = corpId;
            paramEntity.name = corpFullName;
            ResEntity resEntity = ResEntity.genSucc(paramEntity,-1);
            paramCall.onCall(resEntity);
            MyLog.debug(TAG,"[onInvokeMethod]" + " bbsId:" + bbsId + " bbsName:" + strBbsName + " corpId:" + corpId + " corpFullName:" + corpFullName + " vId:" + vId);
        }
        return succ;
    }

    public static String toPrettyFormat(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
}
/**
 *  public AdminCorpConfigPart adminConfig;
 *         public boolean allPurposeAdd;
 *         public boolean anonymousmsgOpen;
 *         public boolean attendanceAntiFake;
 *         public boolean bAuthedLicence;
 *         public boolean bCloseCorpRoom;
 *         public boolean bCorpCustomerService;
 *         public boolean bCreateFromApp;
 *         public boolean bEducationIndustry;
 *         public boolean bHasSetServiceEntry;
 *         public boolean bHideCorpCustomer;
 *         public boolean bIsCreator;
 *         public boolean bMedicalIndustry;
 *         public boolean bNeedShowMoreBar;
 *         public boolean bOpenChatArchive;
 *         public boolean bOpenCustomerService;
 *         public boolean bOpenGroupRobot;
 *         public boolean bOpenMultLanguage;
 *         public boolean bOpenSession;
 *         public boolean bOpenVote;
 *         public boolean bOpenWxAddRoomMember;
 *         public boolean bOpenWxConn;
 *         public boolean bShenpiUseMp;
 *         public boolean bShowAppstoreToEveryone;
 *         public boolean bShowDeviceStore;
 *         public boolean bShowLanFind;
 *         public boolean bShowOpenHardwareDevice;
 *         public boolean bShowReserveserviceLogo;
 *         public boolean bUseCardsHolder;
 *         public boolean bUseProxy;
 *         public boolean bUseProxyMobile;
 *         public boolean bVip;
 *         public int bWxUseCgiDefault;
 *         public int bWxUseModuleDefault;
 *         public long bbsId;
 *         public byte[] bbsName;
 *         public int bindType;
 *         public byte[][] bizmailDomains;
 *         public int[] bussinessIds;
 *         public boolean bwelcomehbEnd;
 *         public CareSplashScreen careSplash;
 *         public String chatTitleInfo;
 *         public boolean checkinClientCalculationLocation;
 *         public WXCheckInContrlInfo checkinContrlInfo;
 *         public int checkinLocationMaxThreshold;
 *         public int checkinLocationMiniThreshold;
 *         public boolean clientCalculateQk;
 *         public boolean closeIncreSyncDepartment;
 *         public boolean clouddiskThumbOpen;
 *         public boolean continousReceipt;
 *         public byte[] corpAddress;
 *         public CorpAttrInfoList corpExternAttrList;
 *         public CorpAttrInfoList corpExternAttrListUnvisible;
 *         public String corpFullName;
 *         public CorpLicenseInfo corpLicenseInfo;
 *         public int createType;
 *         public long[] cusservRoomids;
 *         public boolean cusservroomOpen;
 *         public byte[] customHomeJumpUrl;
 *         public byte[] customHomeUrl;
 *         public int customHomeUrlBegintime;
 *         public int customHomeUrlEndtime;
 *         public int customerMaxCount;
 *         public DefaultApplication[] defaultApp;
 *         public DefaultApplication[] defaultSdkApp;
 *         public String description;
 *         public long deviceOpenFlag;
 *         public int entranceCloseFlag;
 *         public boolean externAutoBinaryCheckin;
 *         public boolean externJobEnable;
 *         public int[] externalDisplayFields;
 *         public CorpSelfAttrInfoList externalSelfAttrList;
 *         public boolean forceReceipt;
 *         public boolean ftnFileEncrypt;
 *         public int gapUpReadTimeout;
 *         public int gapUpTaskTimeout;
 *         public int getArchLimit;
 *         public long halfAttr;
 *         public HalfSelfAttr[] halfSelfAttr;
 *         public boolean hasBbs;
 *         public boolean hasBind;
 *         public boolean hasBizmail;
 *         public boolean hasHongbao;
 *         public boolean hasHongbaoAnnounce;
 *         public boolean hideMobile;
 *         public int hongbaoIsenable;
 *         public int id;
 *         public CorpIndustryInfo industryInfo;
 *         public boolean invitehongbaoClose;
 *         public boolean isAccepted;
 *         public boolean isCloseChat;
 *         public boolean isClosePstn;
 *         public boolean isContactWatermaking;
 *         public boolean isCorpFiltermode;
 *         public boolean isCorpVerify;
 *         public boolean isFinancailCorp;
 *         public boolean isOpenConverge;
 *         public boolean isOpenExternalContact;
 *         public boolean isOpenGapUp;
 *         public boolean isOpenRoomWatermaking;
 *         public boolean isOpenWxRoomInvite;
 *         public boolean isWorkbencgTab;
 *         public boolean isWorkbenchSquared;
 *         public boolean isXhbClose;
 *         public JobSummaryMngInfo[] jobsummaryCliMngInfo;
 *         public boolean joinNeedVerify;
 *         public boolean kqClientLocalpush;
 *         public boolean kqCloseAuto;
 *         public boolean kqUsenewConvergencelocation;
 *         public int language;
 *         public LeaderItem[] leaderlist;
 *         public boolean limitShowVip;
 *         public String logo;
 *         public MsgControlList msgcontrollist;
 *         public String name;
 *         public byte[][] nonEditableExternalAttrFieldId;
 *         public byte[][] nonEditableExternalField;
 *         public String[] nonEditableField;
 *         public int[] nonEditableId;
 *         public byte[][] nonEditableSelfAttrFieldId;
 *         public boolean offlinemsgOpen;
 *         public boolean openBbsAnonymous;
 *         public boolean openBbsAnonymousReply;
 *         public boolean openFtnP2PAcc;
 *         public boolean openFtnP2PAccMobile;
 *         public boolean openJobSummary;
 *         public boolean openP2PGrp;
 *         public boolean openP2PImage;
 *         public config_option[] options;
 *         public boolean p2PCorpOpen;
 *         public int pstnFeatures;
 *         public boolean pushstateJs;
 *         public int replaceMyCustomerService;
 *         public boolean replaceRedDot;
 *         public int reserved;
 *         public boolean roomReadReceipt;
 *         public CorpSelfAttrInfoList selfAttrList;
 *         public ServiceBubble[] serviceBuble;
 *         public boolean sessionDbSafeMode;
 *         public int shortTimePushCorpMsgInterval;
 *         public long showRootDepartmentId;
 *         public long showTcntpartnerDepartmentId;
 *         public int sortRule;
 *         public SplashScreen splash;
 *         public int status;
 *         public String superAdminName;
 *         public long superadminVid;
 *         public boolean supportItilhongbaoInvitewx;
 *         public boolean supportOnlineStatus;
 *         public boolean supportWorkTimeStatus;
 *         public int tabSelectUiFlag;
 *         public ThirdApplication[] thirdApp;
 *         public d[] trdPhonenumInfo;
 *         public UrlTransRule urlTransRule;
 *         public long vSuperadminVid;
 *         public int version;
 *         public VoipAbTestConfig voipAbtestConfig;
 *         public int voipmaxsize;
 *         public WebTrafficReportConfig webTrafficReportConfig;
 *         public int weixinContactApplyStat;
 *         public WelfareClientMngInfo[] welfareInfos;
 *         public int workbenchShowMode;
 *         public int workstatusSyncEntranceFlag;
 *         public byte[] wxUseWxCgiBlackList;
 *         public byte[] wxUseWxCgiWhiteList;
 *         public int xcxCgiFromWework;
 *         public int xcxPreloading;
 */
