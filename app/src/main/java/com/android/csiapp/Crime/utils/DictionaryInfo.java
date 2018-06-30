package com.android.csiapp.Crime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.csiapp.Databases.DictionaryProvider;
import com.android.csiapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2016/11/7.
 */
public class DictionaryInfo {
    private Context mContext;
    //private static HashMap<String,String> ParentData = new HashMap<>();
    //private static HashMap<String, HashMap<String,List<String>>> Data = new HashMap<>();
    public final static String mCaseTypeKey = "AJLBDM";
    public final static String mAreaKey = "GXSDM";
    public final static String mSceneConditionKey = "XCTJDM";
    public final static String mWeatherConditionKey = "XCKYTQQKDM";
    public final static String mWindDirectionKey = "XCFXDM";
    public final static String mIlluminationConditionKey = "XCKYGZTJDM";
    public final static String mSexKey = "XBDM";
    public final static String mToolCategoryKey = "ZAGJLMDM";
    public final static String mToolSourceKey = "ZAGJLYDM";
    public final static String mEvidenceHandKey = "SYHJLXDM";
    public final static String mMethodHandKey = "ZWTQFFDM";
    public final static String mEvidenceFootKey = "ZJHJLXDM";
    public final static String mMethodFootKey = "ZJTQFFDM";
    public final static String mEvidenceToolKey = "GJHJZLDM";
    public final static String mToolInferKey = "GJTDZLDM";
    public final static String mMethodToolKey = "GJHJTQFFDM";
    public final static String mPeopleNumberKey = "ZARSLDM";
    public final static String mCrimeEntranceExportKey = "ZACRKDM";
    public final static String mCrimeMeansKey = "ZASDFLDM";
    public final static String mCrimeCharacterKey = "AJXZDM";
    public final static String mCrimeTimingKey = "ZASJDFLDM";
    public final static String mSelectObjectKey = "XZDXFLDM";
    public final static String mCrimeFeatureKey = "ZATDFLDM";
    public final static String mIntrusiveMethodKey = "QRFSFLDM";
    public final static String mSelectLocationKey = "XZCSFLDM";
    public final static String mCrimePurposeKey = "ZADJMDDM";
    public final static String mSystemKey="SYSTEM";

    private static HashMap<String,String> mCaseTypeParentHashMap = new HashMap<String,String>();
    private static HashMap<String,String> mAreaParentHashMap = new HashMap<String,String>();
    private static HashMap<String,String> mSceneConditionParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mWeatherConditionParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mWindDirectionParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mIlluminationConditionParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSexParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolCategoryParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolSourceParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceHandParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodHandParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceFootParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodFootParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceToolParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolInferParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodToolParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mPeopleNumberParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeEntranceExportParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeMeansParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeCharacterParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeTimingParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSelectObjectParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeFeatureParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mIntrusiveMethodParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSelectLocationParentHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimePurposeParentHashMap  = new HashMap<String,String>();

    private static HashMap<String,String> mCaseTypeHashMap = new HashMap<String,String>();
    private static HashMap<String,String> mAreaHashMap = new HashMap<String,String>();
    private static HashMap<String,String> mSceneConditionHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mWeatherConditionHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mWindDirectionHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mIlluminationConditionHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSexHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolCategoryHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolSourceHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceHandHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodHandHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceFootHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodFootHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mEvidenceToolHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mToolInferHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mMethodToolHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mPeopleNumberHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeEntranceExportHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeMeansHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeCharacterHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeTimingHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSelectObjectHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimeFeatureHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mIntrusiveMethodHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSelectLocationHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mCrimePurposeHashMap  = new HashMap<String,String>();
    private static HashMap<String,String> mSystemKeyHashMap=new HashMap<String,String>();

    private static ArrayList<String> mCasetypeDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCasetypeNodes = new ArrayList<Integer>();
    private static ArrayList<String> mAreaDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mAreaNodes = new ArrayList<Integer>();
    private static ArrayList<String>  mSceneConditionDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mSceneConditionNodes = new ArrayList<Integer>();
    private static ArrayList<String> mWeatherConditionDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mWeatherConditionNodes = new ArrayList<Integer>();
    private static ArrayList<String> mWindDirectionDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mWindDirectionNodes = new ArrayList<Integer>();
    private static ArrayList<String> mIlluminationConditionDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mIlluminationConditionNodes = new ArrayList<Integer>();
    private static ArrayList<String> mSexDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mSexNodes = new ArrayList<Integer>();
    private static ArrayList<String> mToolCategoryDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mToolCategoryNodes = new ArrayList<Integer>();
    private static ArrayList<String> mToolSourceDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mToolSourceNodes = new ArrayList<Integer>();
    private static ArrayList<String> mEvidenceHandDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mEvidenceHandNodes = new ArrayList<Integer>();
    private static ArrayList<String> mMethodHandDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mMethodHandNodes = new ArrayList<Integer>();
    private static ArrayList<String> mEvidenceFootDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mEvidenceFootNodes = new ArrayList<Integer>();
    private static ArrayList<String> mMethodFootDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mMethodFootNodes = new ArrayList<Integer>();
    private static ArrayList<String> mEvidenceToolDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mEvidenceToolNodes = new ArrayList<Integer>();
    private static ArrayList<String> mToolInferDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mToolInferNodes = new ArrayList<Integer>();
    private static ArrayList<String> mMethodToolDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mMethodToolNodes = new ArrayList<Integer>();
    private static ArrayList<String> mPeopleNumberDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mPeopleNumberNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimeMeansDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimeMeansNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimeCharacterDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimeCharacterNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimeEntranceExportDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimeEntranceExportNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimeTimingDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimeTimingNodes = new ArrayList<Integer>();
    private static ArrayList<String> mSelectObjectDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mSelectObjectNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimeFeatureDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimeFeatureNodes = new ArrayList<Integer>();
    private static ArrayList<String> mIntrusiveMethodDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mIntrusiveMethodNodes = new ArrayList<Integer>();
    private static ArrayList<String> mSelectLocationDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mSelectLocationNodes = new ArrayList<Integer>();
    private static ArrayList<String> mCrimePurposeDictKey = new ArrayList<String>();
    private static ArrayList<Integer> mCrimePurposeNodes = new ArrayList<Integer>();

    public final static String mChangeOptionKey = "BDYYDM";
    private static ArrayList<String> mChangeOptionDictKey = new ArrayList<String>();
    private static HashMap<String,String> mChangeOptionHashMap  = new HashMap<String,String>();

    public DictionaryInfo(Context context){
        this.mContext = context;
    }

    public static void getInitialDictionary(Context context){
        SharedPreferences prefs = context.getSharedPreferences("InitialDevice", 0);
        String initstatus = prefs.getString("Initial", "0");
        DictionaryProvider dictionaryProvider = new DictionaryProvider(context);

        if(initstatus.equalsIgnoreCase("1")) {
            mCaseTypeParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCaseTypeKey);
            mAreaParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mAreaKey);
            mSceneConditionParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mSceneConditionKey);
            mWeatherConditionParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mWeatherConditionKey);
            mWindDirectionParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mWindDirectionKey);
            mIlluminationConditionParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mIlluminationConditionKey);
            mSexParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mSexKey);
            mToolCategoryParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mToolCategoryKey);
            mToolSourceParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mToolSourceKey);
            mEvidenceHandParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mEvidenceHandKey);
            mMethodHandParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mMethodHandKey);
            mEvidenceFootParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mEvidenceFootKey);
            mMethodFootParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mMethodFootKey);
            mEvidenceToolParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mEvidenceToolKey);
            mToolInferParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mToolInferKey);
            mMethodToolParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mMethodToolKey);
            mPeopleNumberParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mPeopleNumberKey);
            mCrimeEntranceExportParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimeEntranceExportKey);
            mCrimeMeansParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimeMeansKey);
            mCrimeCharacterParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimeCharacterKey);
            mCrimeTimingParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimeTimingKey);
            mSelectObjectParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mSelectObjectKey);
            mCrimeFeatureParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimeFeatureKey);
            mIntrusiveMethodParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mIntrusiveMethodKey);
            mSelectLocationParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mSelectLocationKey);
            mCrimePurposeParentHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetParentHashMap(mCrimePurposeKey);

            mCaseTypeHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCaseTypeKey);
            mAreaHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mAreaKey);
            mSceneConditionHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mSceneConditionKey);
            mWeatherConditionHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mWeatherConditionKey);
            mWindDirectionHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mWindDirectionKey);
            mIlluminationConditionHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mIlluminationConditionKey);
            mSexHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mSexKey);
            mToolCategoryHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mToolCategoryKey);
            mToolSourceHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mToolSourceKey);
            mEvidenceHandHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mEvidenceHandKey);
            mMethodHandHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mMethodHandKey);
            mEvidenceFootHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mEvidenceFootKey);
            mMethodFootHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mMethodFootKey);
            mEvidenceToolHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mEvidenceToolKey);
            mToolInferHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mToolInferKey);
            mMethodToolHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mMethodToolKey);
            mPeopleNumberHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mPeopleNumberKey);
            mCrimeEntranceExportHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimeEntranceExportKey);
            mCrimeMeansHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimeMeansKey);
            mCrimeCharacterHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimeCharacterKey);
            mCrimeTimingHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimeTimingKey);
            mSelectObjectHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mSelectObjectKey);
            mCrimeFeatureHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimeFeatureKey);
            mIntrusiveMethodHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mIntrusiveMethodKey);
            mSelectLocationHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mSelectLocationKey);
            mCrimePurposeHashMap  = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mCrimePurposeKey);
            mSystemKeyHashMap=(HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mSystemKey);

            mCasetypeDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCaseTypeKey);
            mCasetypeDictKey = sortWithTree(mCaseTypeKey, mCasetypeDictKey, mCaseTypeParentHashMap);
            mCasetypeNodes = getTreeNodes(mCasetypeDictKey, mCaseTypeParentHashMap);
            mAreaDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mAreaKey);
            mAreaDictKey = sortWithTree(mAreaKey, mAreaDictKey, mAreaParentHashMap);
            mAreaNodes = getTreeNodes(mAreaDictKey, mAreaParentHashMap);
            mSceneConditionDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mSceneConditionKey);
            mSceneConditionDictKey = sortWithTree(mSceneConditionKey, mSceneConditionDictKey, mSceneConditionParentHashMap);
            mSceneConditionNodes = getTreeNodes(mSceneConditionDictKey, mSceneConditionParentHashMap);
            mWeatherConditionDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mWeatherConditionKey);
            mWeatherConditionDictKey = sortWithTree(mWeatherConditionKey, mWeatherConditionDictKey, mWeatherConditionParentHashMap);
            mWeatherConditionNodes = getTreeNodes(mWeatherConditionDictKey, mWeatherConditionParentHashMap);
            mWindDirectionDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mWindDirectionKey);
            mWindDirectionDictKey = sortWithTree(mWindDirectionKey, mWindDirectionDictKey, mWindDirectionParentHashMap);
            mWindDirectionNodes = getTreeNodes(mWindDirectionDictKey, mWindDirectionParentHashMap);
            mIlluminationConditionDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mIlluminationConditionKey);
            mIlluminationConditionDictKey = sortWithTree(mIlluminationConditionKey, mIlluminationConditionDictKey, mIlluminationConditionParentHashMap);
            mIlluminationConditionNodes = getTreeNodes(mIlluminationConditionDictKey, mIlluminationConditionParentHashMap);
            mSexDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mSexKey);
            mSexDictKey = sortWithTree(mSexKey, mSexDictKey, mSexParentHashMap);
            mSexNodes = getTreeNodes(mSexDictKey, mSexParentHashMap);
            mToolCategoryDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mToolCategoryKey);
            mToolCategoryDictKey = sortWithTree(mToolCategoryKey, mToolCategoryDictKey, mToolCategoryParentHashMap);
            mToolCategoryNodes = getTreeNodes(mToolCategoryDictKey, mToolCategoryParentHashMap);
            mToolSourceDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mToolSourceKey);
            mToolSourceDictKey = sortWithTree(mToolSourceKey, mToolSourceDictKey, mToolSourceParentHashMap);
            mToolSourceNodes = getTreeNodes(mToolSourceDictKey, mToolSourceParentHashMap);
            mEvidenceHandDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mEvidenceHandKey);
            mEvidenceHandDictKey = sortWithTree(mEvidenceHandKey, mEvidenceHandDictKey, mEvidenceHandParentHashMap);
            mEvidenceHandNodes = getTreeNodes(mEvidenceHandDictKey, mEvidenceHandParentHashMap);
            mMethodHandDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mMethodHandKey);
            mMethodHandDictKey = sortWithTree(mMethodHandKey, mMethodHandDictKey, mMethodHandParentHashMap);
            mMethodHandNodes = getTreeNodes(mMethodHandDictKey, mMethodHandParentHashMap);
            mEvidenceFootDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mEvidenceFootKey);
            mEvidenceFootDictKey = sortWithTree(mEvidenceFootKey, mEvidenceFootDictKey, mEvidenceFootParentHashMap);
            mEvidenceFootNodes = getTreeNodes(mEvidenceFootDictKey, mEvidenceFootParentHashMap);
            mMethodFootDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mMethodFootKey);
            mMethodFootDictKey = sortWithTree(mMethodFootKey, mMethodFootDictKey, mMethodFootParentHashMap);
            mMethodFootNodes = getTreeNodes(mMethodFootDictKey, mMethodFootParentHashMap);
            mEvidenceToolDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mEvidenceToolKey);
            mEvidenceToolDictKey = sortWithTree(mEvidenceToolKey, mEvidenceToolDictKey, mEvidenceToolParentHashMap);
            mEvidenceToolNodes = getTreeNodes(mEvidenceToolDictKey, mEvidenceToolParentHashMap);
            mToolInferDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mToolInferKey);
            mToolInferDictKey = sortWithTree(mToolInferKey, mToolInferDictKey, mToolInferParentHashMap);
            mToolInferNodes = getTreeNodes(mToolInferDictKey, mToolInferParentHashMap);
            mMethodToolDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mMethodToolKey);
            mMethodToolDictKey = sortWithTree(mMethodToolKey, mMethodToolDictKey, mMethodToolParentHashMap);
            mMethodToolNodes = getTreeNodes(mMethodToolDictKey, mMethodToolParentHashMap);
            mPeopleNumberDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mPeopleNumberKey);
            mPeopleNumberDictKey = sortWithTree(mPeopleNumberKey, mPeopleNumberDictKey, mPeopleNumberParentHashMap);
            mPeopleNumberNodes = getTreeNodes(mPeopleNumberDictKey, mPeopleNumberParentHashMap);
            mCrimeMeansDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimeMeansKey);
            mCrimeMeansDictKey = sortWithTree(mCrimeMeansKey, mCrimeMeansDictKey, mCrimeMeansParentHashMap);
            mCrimeMeansNodes = getTreeNodes(mCrimeMeansDictKey, mCrimeMeansParentHashMap);
            mCrimeCharacterDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimeCharacterKey);
            mCrimeCharacterDictKey = sortWithTree(mCrimeCharacterKey, mCrimeCharacterDictKey, mCrimeCharacterParentHashMap);
            mCrimeCharacterNodes = getTreeNodes(mCrimeCharacterDictKey, mCrimeCharacterParentHashMap);
            mCrimeEntranceExportDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimeEntranceExportKey);
            mCrimeEntranceExportDictKey = sortWithTree(mCrimeEntranceExportKey, mCrimeEntranceExportDictKey, mCrimeEntranceExportParentHashMap);
            mCrimeEntranceExportNodes = getTreeNodes(mCrimeEntranceExportDictKey, mCrimeEntranceExportParentHashMap);
            mCrimeTimingDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimeTimingKey);
            mCrimeTimingDictKey = sortWithTree(mCrimeTimingKey, mCrimeTimingDictKey, mCrimeTimingParentHashMap);
            mCrimeTimingNodes = getTreeNodes(mCrimeTimingDictKey, mCrimeTimingParentHashMap);
            mSelectObjectDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mSelectObjectKey);
            mSelectObjectDictKey = sortWithTree(mSelectObjectKey, mSelectObjectDictKey, mSelectObjectParentHashMap);
            mSelectObjectNodes = getTreeNodes(mSelectObjectDictKey, mSelectObjectParentHashMap);
            mCrimeFeatureDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimeFeatureKey);
            mCrimeFeatureDictKey = sortWithTree(mCrimeFeatureKey, mCrimeFeatureDictKey, mCrimeFeatureParentHashMap);
            mCrimeFeatureNodes = getTreeNodes(mCrimeFeatureDictKey, mCrimeFeatureParentHashMap);
            mIntrusiveMethodDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mIntrusiveMethodKey);
            mIntrusiveMethodDictKey = sortWithTree(mIntrusiveMethodKey, mIntrusiveMethodDictKey, mIntrusiveMethodParentHashMap);
            mIntrusiveMethodNodes = getTreeNodes(mIntrusiveMethodDictKey, mIntrusiveMethodParentHashMap);
            mSelectLocationDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mSelectLocationKey);
            mSelectLocationDictKey = sortWithTree(mSelectLocationKey, mSelectLocationDictKey, mSelectLocationParentHashMap);
            mSelectLocationNodes = getTreeNodes(mSelectLocationDictKey, mSelectLocationParentHashMap);
            mCrimePurposeDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mCrimePurposeKey);
            mCrimePurposeDictKey = sortWithTree(mCrimePurposeKey, mCrimePurposeDictKey, mCrimePurposeParentHashMap);
            mCrimePurposeNodes = getTreeNodes(mCrimePurposeDictKey, mCrimePurposeParentHashMap);

            mChangeOptionDictKey = (ArrayList<String>) dictionaryProvider.queryToGetDictKey(mChangeOptionKey);;
            mChangeOptionHashMap = (HashMap<String,String>) dictionaryProvider.queryToGetHashMap(mChangeOptionKey);
        }
    }

    private static ArrayList<String> sortWithTree(final String mKey, ArrayList<String> mDicitonary, final HashMap<String, String> mParentHashMap){
        ArrayList<String> sortList = new ArrayList<String>();
        for(int i=0;i<mDicitonary.size();i++){
            if(mParentHashMap.get(mDicitonary.get(i)).equalsIgnoreCase(mKey))
                sortList.add(mDicitonary.get(i));
        }
        Collections.reverse(mDicitonary);
        for(int j=0;j<mDicitonary.size();j++){
            if(!mParentHashMap.get(mDicitonary.get(j)).equalsIgnoreCase(mKey))
                for(int k = 0;k<sortList.size();k++){
                    if(mParentHashMap.get(mDicitonary.get(j)).equalsIgnoreCase(sortList.get(k))) {
                        sortList.add(k + 1, mDicitonary.get(j));
                        break;
                    }
                }
        }
        return sortList;
    }

    private static ArrayList<Integer> getTreeNodes(ArrayList<String> mDicitonary, HashMap<String, String> mParentHashMap){
        ArrayList<Integer> mNodes = new ArrayList<Integer>();
        for(int z = 0; z<mDicitonary.size(); z++){
            int level=0;
            String Parent = mParentHashMap.get(mDicitonary.get(z));
            boolean next = false;
            if(Parent!=null) {
                do {
                    next = false;
                    for (int j = 0; j < mDicitonary.size(); j++) {
                        if (Parent.equalsIgnoreCase(mDicitonary.get(j))) {
                            level++;
                            next = true;
                            Parent = mParentHashMap.get(mDicitonary.get(j));
                            break;
                        }
                    }
                } while (next);
            }
            mNodes.add(level);
        }
        return mNodes;
    }

    public String getTitle(String rootkey){
        String title = "";
        switch (rootkey) {
            case mCaseTypeKey:
                title = mContext.getResources().getString(R.string.casetype);
                break;
            case mAreaKey:
                title = mContext.getResources().getString(R.string.area);
                break;
            case mSceneConditionKey:
                title = mContext.getResources().getString(R.string.scene_condition);
                break;
            case mWeatherConditionKey:
                title = mContext.getResources().getString(R.string.weather_condition);
                break;
            case mWindDirectionKey:
                title = mContext.getResources().getString(R.string.wind_direction);
                break;
            case mIlluminationConditionKey:
                title = mContext.getResources().getString(R.string.illumination_condition);
                break;
            case mSexKey:
                title = mContext.getResources().getString(R.string.sex);
                break;
            case mToolCategoryKey:
                title = mContext.getResources().getString(R.string.tool_category);
                break;
            case mToolSourceKey:
                title = mContext.getResources().getString(R.string.tool_source);
                break;
            case mEvidenceHandKey:
                title = mContext.getResources().getString(R.string.evidence_hand);
                break;
            case mEvidenceFootKey:
                title = mContext.getResources().getString(R.string.evidence_foot);
                break;
            case mEvidenceToolKey:
                title = mContext.getResources().getString(R.string.evidence_tool);
                break;
            case mToolInferKey:
                title = mContext.getResources().getString(R.string.infer);
                break;
            case mMethodHandKey:
            case mMethodFootKey:
            case mMethodToolKey:
                title = mContext.getResources().getString(R.string.extraction_method);
                break;
            case mPeopleNumberKey:
                title = mContext.getResources().getString(R.string.crime_people_number);
                break;
            case mCrimeMeansKey:
                title = mContext.getResources().getString(R.string.crime_means);
                break;
            case mCrimeCharacterKey:
                title = mContext.getResources().getString(R.string.crime_character);
                break;
            case mCrimeEntranceExportKey:
                title = "出入口";
                break;
            case mCrimeTimingKey:
                title = mContext.getResources().getString(R.string.crime_timing);
                break;
            case mSelectObjectKey:
                title = mContext.getResources().getString(R.string.select_object);
                break;
            case mCrimeFeatureKey:
                title = mContext.getResources().getString(R.string.crime_feature);
                break;
            case mIntrusiveMethodKey:
                title = mContext.getResources().getString(R.string.intrusive_method);
                break;
            case mSelectLocationKey:
                title = mContext.getResources().getString(R.string.select_location);
                break;
            case mCrimePurposeKey:
                title = mContext.getResources().getString(R.string.crime_purpose);
                break;
            default:
                break;
        }
        return title;
    }

    public String getMethod(String rootkey){
        String title = "Single";
        switch (rootkey) {
            case mCrimeMeansKey://作案手段
                title = "MULTIPLE";
                break;
            case mCrimeCharacterKey://案件性质
                title = "MULTIPLE";
                break;
            case mCrimeEntranceExportKey://出入口
                title = "MULTIPLE";
                break;
            case mCrimeTimingKey://作案时机
                title = "MULTIPLE";
                break;
            case mSelectObjectKey://选择对象
                title = "MULTIPLE";
                break;
            case mCrimeFeatureKey://作案特点
                title = "MULTIPLE";
                break;
            case mIntrusiveMethodKey://侵入方式
                title = "MULTIPLE";
                break;
            case mSelectLocationKey://选择处所
                title = "MULTIPLE";
                break;
            case mCrimePurposeKey://作案动机目的
                title = "MULTIPLE";
                break;
            default:
                break;
        }
        return title;
    }

    public static ArrayList<Integer> getNodes(String rootkey){
        ArrayList<Integer> result = new ArrayList<Integer>();
        switch (rootkey) {
            case mCaseTypeKey:
                result = DictionaryInfo.mCasetypeNodes;
                break;
            case mAreaKey:
                result = DictionaryInfo.mAreaNodes;
                break;
            case mSceneConditionKey:
                result = DictionaryInfo.mSceneConditionNodes;
                break;
            case mWeatherConditionKey:
                result = DictionaryInfo.mWeatherConditionNodes;
                break;
            case mWindDirectionKey:
                result = DictionaryInfo.mWindDirectionNodes;
                break;
            case mIlluminationConditionKey:
                result = DictionaryInfo.mIlluminationConditionNodes;
                break;
            case mSexKey:
                result = DictionaryInfo.mSexNodes;
                break;
            case mToolCategoryKey:
                result = DictionaryInfo.mToolCategoryNodes;
                break;
            case mToolSourceKey:
                result = DictionaryInfo.mToolSourceNodes;
                break;
            case mEvidenceHandKey:
                result = DictionaryInfo.mEvidenceHandNodes;
                break;
            case mMethodHandKey:
                result = DictionaryInfo.mMethodHandNodes;
                break;
            case mEvidenceFootKey:
                result = DictionaryInfo.mEvidenceFootNodes;
                break;
            case mMethodFootKey:
                result = DictionaryInfo.mMethodFootNodes;
                break;
            case mEvidenceToolKey:
                result = DictionaryInfo.mEvidenceToolNodes;
                break;
            case mToolInferKey:
                result = DictionaryInfo.mToolInferNodes;
                break;
            case mMethodToolKey:
                result = DictionaryInfo.mMethodToolNodes;
                break;
            case mPeopleNumberKey:
                result = DictionaryInfo.mPeopleNumberNodes;
                break;
            case mCrimeMeansKey:
                result = DictionaryInfo.mCrimeMeansNodes;
                break;
            case mCrimeCharacterKey:
                result = DictionaryInfo.mCrimeCharacterNodes;
                break;
            case mCrimeEntranceExportKey:
                result = DictionaryInfo.mCrimeEntranceExportNodes;
                break;
            case mCrimeTimingKey:
                result = DictionaryInfo.mCrimeTimingNodes;
                break;
            case mSelectObjectKey:
                result = DictionaryInfo.mSelectObjectNodes;
                break;
            case mCrimeFeatureKey:
                result = DictionaryInfo.mCrimeFeatureNodes;
                break;
            case mIntrusiveMethodKey:
                result = DictionaryInfo.mIntrusiveMethodNodes;
                break;
            case mSelectLocationKey:
                result = DictionaryInfo.mSelectLocationNodes;
                break;
            case mCrimePurposeKey:
                result = DictionaryInfo.mCrimePurposeNodes;
                break;
            default:
                break;
        }
        return result;
    }

    public static ArrayList<String> getDictKeyList(String rootkey){
        ArrayList<String> result = new ArrayList<String>();
        switch (rootkey) {
            case mCaseTypeKey:
                result = DictionaryInfo.mCasetypeDictKey;
                break;
            case mAreaKey:
                result = DictionaryInfo.mAreaDictKey;
                break;
            case mSceneConditionKey:
                result = DictionaryInfo.mSceneConditionDictKey;
                break;
            case mWeatherConditionKey:
                result = DictionaryInfo.mWeatherConditionDictKey;
                break;
            case mWindDirectionKey:
                result = DictionaryInfo.mWindDirectionDictKey;
                break;
            case mIlluminationConditionKey:
                result = DictionaryInfo.mIlluminationConditionDictKey;
                break;
            case mSexKey:
                result = DictionaryInfo.mSexDictKey;
                break;
            case mToolCategoryKey:
                result = DictionaryInfo.mToolCategoryDictKey;
                break;
            case mToolSourceKey:
                result = DictionaryInfo.mToolSourceDictKey;
                break;
            case mEvidenceHandKey:
                result = DictionaryInfo.mEvidenceHandDictKey;
                break;
            case mMethodHandKey:
                result = DictionaryInfo.mMethodHandDictKey;
                break;
            case mEvidenceFootKey:
                result = DictionaryInfo.mEvidenceFootDictKey;
                break;
            case mMethodFootKey:
                result = DictionaryInfo.mMethodFootDictKey;
                break;
            case mEvidenceToolKey:
                result = DictionaryInfo.mEvidenceToolDictKey;
                break;
            case mToolInferKey:
                result = DictionaryInfo.mToolInferDictKey;
                break;
            case mMethodToolKey:
                result = DictionaryInfo.mMethodToolDictKey;
                break;
            case mPeopleNumberKey:
                result = DictionaryInfo.mPeopleNumberDictKey;
                break;
            case mCrimeMeansKey:
                result = DictionaryInfo.mCrimeMeansDictKey;
                break;
            case mCrimeCharacterKey:
                result = DictionaryInfo.mCrimeCharacterDictKey;
                break;
            case mCrimeEntranceExportKey:
                result = DictionaryInfo.mCrimeEntranceExportDictKey;
                break;
            case mCrimeTimingKey:
                result = DictionaryInfo.mCrimeTimingDictKey;
                break;
            case mSelectObjectKey:
                result = DictionaryInfo.mSelectObjectDictKey;
                break;
            case mCrimeFeatureKey:
                result = DictionaryInfo.mCrimeFeatureDictKey;
                break;
            case mIntrusiveMethodKey:
                result = DictionaryInfo.mIntrusiveMethodDictKey;
                break;
            case mSelectLocationKey:
                result = DictionaryInfo.mSelectLocationDictKey;
                break;
            case mCrimePurposeKey:
                result = DictionaryInfo.mCrimePurposeDictKey;
                break;
            case  mChangeOptionKey:
                result = DictionaryInfo.mChangeOptionDictKey;
                break;
            default:
                break;
        }
        return result;
    }

    public static String getDictValue(String rootkey, String DictKey){
        String result = "";
        switch (rootkey){
            case mCaseTypeKey:
                if(mCaseTypeHashMap.size()!=0) result = mCaseTypeHashMap.get(DictKey);
                break;
            case mAreaKey:
                if(mAreaHashMap.size()!=0) result = mAreaHashMap.get(DictKey);
                break;
            case mSceneConditionKey:
                if(mSceneConditionHashMap.size()!=0) result = mSceneConditionHashMap.get(DictKey);
                break;
            case mWeatherConditionKey:
                if(mWeatherConditionHashMap.size()!=0) result = mWeatherConditionHashMap.get(DictKey);
                break;
            case mWindDirectionKey:
                if(mWindDirectionHashMap.size()!=0) result = mWindDirectionHashMap.get(DictKey);
                break;
            case mIlluminationConditionKey:
                if(mIlluminationConditionHashMap.size()!=0) result = mIlluminationConditionHashMap.get(DictKey);
                break;
            case mSexKey:
                if(mSexHashMap.size()!=0) result = mSexHashMap.get(DictKey);
                break;
            case mToolCategoryKey:
                if(mToolCategoryHashMap.size()!=0) result = mToolCategoryHashMap.get(DictKey);
                break;
            case mToolSourceKey:
                if(mToolSourceHashMap.size()!=0) result = mToolSourceHashMap.get(DictKey);
                break;
            case mEvidenceHandKey:
                if(mEvidenceHandHashMap.size()!=0) result = mEvidenceHandHashMap.get(DictKey);
                break;
            case mMethodHandKey:
                if(mMethodHandHashMap.size()!=0) result = mMethodHandHashMap.get(DictKey);
                break;
            case mEvidenceFootKey:
                if(mEvidenceFootHashMap.size()!=0) result = mEvidenceFootHashMap.get(DictKey);
                break;
            case mMethodFootKey:
                if(mMethodFootHashMap.size()!=0) result = mMethodFootHashMap.get(DictKey);
                break;
            case mEvidenceToolKey:
                if(mEvidenceToolHashMap.size()!=0) result = mEvidenceToolHashMap.get(DictKey);
                break;
            case mToolInferKey:
                if(mToolInferHashMap.size()!=0) result = mToolInferHashMap.get(DictKey);
                break;
            case mMethodToolKey:
                if(mMethodToolHashMap.size()!=0) result = mMethodToolHashMap.get(DictKey);
                break;
            case mPeopleNumberKey:
                if(mPeopleNumberHashMap.size()!=0) result = mPeopleNumberHashMap.get(DictKey);
                break;
            case mCrimeMeansKey:
                if(mCrimeMeansHashMap.size()!=0) result = mCrimeMeansHashMap.get(DictKey);
                break;
            case mCrimeCharacterKey:
                if(mCrimeCharacterHashMap.size()!=0) result = mCrimeCharacterHashMap.get(DictKey);
                break;
            case mCrimeEntranceExportKey:
                if(mCrimeEntranceExportHashMap.size()!=0) result = mCrimeEntranceExportHashMap.get(DictKey);
                break;
            case mCrimeTimingKey:
                if(mCrimeTimingHashMap.size()!=0) result = mCrimeTimingHashMap.get(DictKey);
                break;
            case mSelectObjectKey:
                if(mSelectObjectHashMap.size()!=0) result = mSelectObjectHashMap.get(DictKey);
                break;
            case mCrimeFeatureKey:
                if(mCrimeFeatureHashMap.size()!=0) result = mCrimeFeatureHashMap.get(DictKey);
                break;
            case mIntrusiveMethodKey:
                if(mIntrusiveMethodHashMap.size()!=0) result = mIntrusiveMethodHashMap.get(DictKey);
                break;
            case mSelectLocationKey:
                if(mSelectLocationHashMap.size()!=0) result = mSelectLocationHashMap.get(DictKey);
                break;
            case mCrimePurposeKey:
                if(mCrimePurposeHashMap.size()!=0) result = mCrimePurposeHashMap.get(DictKey);
                break;
            case mChangeOptionKey:
                if(mChangeOptionHashMap.size()!=0) result = mChangeOptionHashMap.get(DictKey);
                break;
            case mSystemKey:
                if(mSystemKeyHashMap.size()!=0) result=mSystemKeyHashMap.get(DictKey);
                break;
            default:
                break;
        }
        if(result==null) return "";
        return result;
    }

    public static String getDictKey(String rootkey, String DictValue){
        String result = "";
        switch (rootkey){
            case mCaseTypeKey:
                if(mCaseTypeHashMap.size()!=0) result = (String) valueGetKey(mCaseTypeHashMap, DictValue);
                break;
            case mAreaKey:
                if(mAreaHashMap.size()!=0) result = (String) valueGetKey(mAreaHashMap, DictValue);
                break;
            case mSceneConditionKey:
                if(mSceneConditionHashMap.size()!=0) result = (String) valueGetKey(mSceneConditionHashMap, DictValue);
                break;
            case mWeatherConditionKey:
                if(mWeatherConditionHashMap.size()!=0) result = (String) valueGetKey(mWeatherConditionHashMap, DictValue);
                break;
            case mWindDirectionKey:
                if(mWindDirectionHashMap.size()!=0) result = (String) valueGetKey(mWindDirectionHashMap, DictValue);
                break;
            case mIlluminationConditionKey:
                if(mIlluminationConditionHashMap.size()!=0) result = (String) valueGetKey(mIlluminationConditionHashMap, DictValue);
                break;
            case mSexKey:
                if(mSexHashMap.size()!=0) result = (String) valueGetKey(mSexHashMap, DictValue);
                break;
            case mToolCategoryKey:
                if(mToolCategoryHashMap.size()!=0) result = (String) valueGetKey(mToolCategoryHashMap, DictValue);
                break;
            case mToolSourceKey:
                if(mToolSourceHashMap.size()!=0) result = (String) valueGetKey(mToolSourceHashMap, DictValue);
                break;
            case mEvidenceHandKey:
                if(mEvidenceHandHashMap.size()!=0) result = (String) valueGetKey(mEvidenceHandHashMap, DictValue);
                break;
            case mMethodHandKey:
                if(mMethodHandHashMap.size()!=0) result = (String) valueGetKey(mMethodHandHashMap, DictValue);
                break;
            case mEvidenceFootKey:
                if(mEvidenceFootHashMap.size()!=0) result = (String) valueGetKey(mEvidenceFootHashMap, DictValue);
                break;
            case mMethodFootKey:
                if(mMethodFootHashMap.size()!=0) result = (String) valueGetKey(mMethodFootHashMap, DictValue);
                break;
            case mEvidenceToolKey:
                if(mEvidenceToolHashMap.size()!=0) result = (String) valueGetKey(mEvidenceToolHashMap, DictValue);
                break;
            case mToolInferKey:
                if(mToolInferHashMap.size()!=0) result = (String) valueGetKey(mToolInferHashMap, DictValue);
                break;
            case mMethodToolKey:
                if(mMethodToolHashMap.size()!=0) result = (String) valueGetKey(mMethodToolHashMap, DictValue);
                break;
            case mPeopleNumberKey:
                if(mPeopleNumberHashMap.size()!=0) result = (String) valueGetKey(mPeopleNumberHashMap, DictValue);
                break;
            case mCrimeMeansKey:
                if(mCrimeMeansHashMap.size()!=0) result = (String) valueGetKey(mCrimeMeansHashMap, DictValue);
                break;
            case mCrimeCharacterKey:
                if(mCrimeCharacterHashMap.size()!=0) result = (String) valueGetKey(mCrimeCharacterHashMap, DictValue);
                break;
            case mCrimeEntranceExportKey:
                if(mCrimeEntranceExportHashMap.size()!=0) result = (String) valueGetKey(mCrimeEntranceExportHashMap, DictValue);
                break;
            case mCrimeTimingKey:
                if(mCrimeTimingHashMap.size()!=0) result = (String) valueGetKey(mCrimeTimingHashMap, DictValue);
                break;
            case mSelectObjectKey:
                if(mSelectObjectHashMap.size()!=0) result = (String) valueGetKey(mSelectObjectHashMap, DictValue);
                break;
            case mCrimeFeatureKey:
                if(mCrimeFeatureHashMap.size()!=0) result = (String) valueGetKey(mCrimeFeatureHashMap, DictValue);
                break;
            case mIntrusiveMethodKey:
                if(mIntrusiveMethodHashMap.size()!=0) result = (String) valueGetKey(mIntrusiveMethodHashMap, DictValue);
                break;
            case mSelectLocationKey:
                if(mSelectLocationHashMap.size()!=0) result = (String) valueGetKey(mSelectLocationHashMap, DictValue);
                break;
            case mCrimePurposeKey:
                if(mCrimePurposeHashMap.size()!=0) result = (String) valueGetKey(mCrimePurposeHashMap, DictValue);
                break;
            case mChangeOptionKey:
                if(mChangeOptionHashMap.size()!=0) result = (String) valueGetKey(mChangeOptionHashMap, DictValue);
                break;
            default:
                break;
        }

        if(result==null) return "";
        return result;
    }

    private static String valueGetKey(Map map, String value) {
        Set set = map.entrySet();
        ArrayList arr = new ArrayList<>();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue().equals(value)) {
                String s = (String) entry.getKey();
                arr.add(s);
            }
        }
        if(arr.size()!=0) return (String) arr.get(0);
        else return "";
    }
}
