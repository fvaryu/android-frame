//package com.lowett.core.net;
//
//import android.text.TextUtils;
//
//import com.fit.android.model.Goods;
//import com.fit.android.model.OrderAmount;
//import com.fit.android.model.PageInfo;
//import com.fit.android.model.User;
//import com.fit.android.net.internal.RequestParams;
//import com.fit.android.utils.MD5;
//
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
///**
// * Created by Hyu on 2016/11/29.
// * Email: fvaryu@qq.com
// */
//
//class NetParams {
//    static RequestParams getParams() {
//        return new RequestParams();
//    }
//
//    static RequestParams getMobileParams(String mobile) {
//        return new RequestParams("mobile", mobile);
//    }
//
//    static RequestParams getLoginParams(String mobile, String password) {
//        return getMobileParams(mobile).put("pwd", MD5.toMD5(password));
//    }
//
//    static RequestParams getRegisterParams(String mobile, String password, String smsCode, String inviteCode) {
//        RequestParams params = getLoginParams(mobile, password).put("smsCode", smsCode);
//        if (!TextUtils.isEmpty(inviteCode)) {
//            params.put("inviteCode", inviteCode);
//        }
//        return params;
//    }
//
//    static RequestParams getPageInfoParams(PageInfo info) {
//        return getParams().put("pageSize", info.getPageSize()).put("pageNum", info.nextPage());
//    }
//
//    static RequestParams getGoodsListParams(int categoryId, PageInfo info) {
//        return getPageInfoParams(info).put("categoryId", categoryId);
//    }
//
//    static RequestParams getOrderListParams(int type, PageInfo info) {
//        RequestParams params = getPageInfoParams(info);
//        if (type >= 0) {
//            params.put("status", type);
//        }
//        return params;
//    }
//
//    // userCouponId  useAccount payPwd
//    static RequestParams getShoppingGoodsParams(Map<Integer, Goods> map) {
//        RequestParams params = getParams();
//        for (Integer i : map.keySet()) {
//            params.put(String.format(Locale.getDefault(), "orderGoodsParams[%d].goodsId", i), map.get(i).getId());
//            params.put(String.format(Locale.getDefault(), "orderGoodsParams[%d].buyNum", i), map.get(i).getCount());
//        }
//        return params;
//    }
//
//    // userCouponId  useAccount payPwd
//    static RequestParams getPayParams(List<Goods> goodses, int payChannel, OrderAmount orderAmount) {
//        RequestParams params = getParams();
//        for (int i = 0; i < goodses.size(); i++) {
//            params.put(String.format(Locale.getDefault(), "orderGoodsParams[%d].goodsId", i), goodses.get(i).getId());
//            params.put(String.format(Locale.getDefault(), "orderGoodsParams[%d].buyNum", i), goodses.get(i).getCount());
//        }
//
//        params.put("originPrice", orderAmount.getOriginPrice());
//        params.put("actualPrice", orderAmount.getActualPrice());
//        params.put("accountPrice", orderAmount.getAccountPrice());
//        params.put("accountPrice", orderAmount.getAccountPrice());
//        params.put("couponPrice", orderAmount.getCouponPrice());
//        params.put("payPrice", orderAmount.getPayPrice());
//        params.put("logisticsPrice", orderAmount.getLogisticsPrice());
//
//        params.put("channel", payChannel);
//
//        return params;
//    }
//
//    static RequestParams payContinue(long orderId, int payChannel, boolean useBalance) {
//        return getOrderId(orderId).put("channel", payChannel).put("useAccount", useBalance ? 0 : 1);
//    }
//
//    static RequestParams getOrderId(long id) {
//        return getParams().put("orderId", id);
//    }
//
//    static RequestParams getUserParams(User info) {
//        RequestParams params = getParams();
//        if (!TextUtils.isEmpty(info.getNickname())) {
//            params.put("nickname", info.getNickname());
//        }
//        if (!TextUtils.isEmpty(info.getAvatar())) {
//            params.put("avatar", info.getAvatar());
//        }
//        if (!TextUtils.isEmpty(info.getAddress())) {
//            params.put("address", info.getAddress());
//        }
//        if (!TextUtils.isEmpty(info.getDesc())) {
//            params.put("desc", info.getDesc());
//        }
//
//        if (!TextUtils.isEmpty(info.getBirthday())) {
//            params.put("birthday", info.getBirthday());
//        }
//
//        if (!TextUtils.isEmpty(info.getCareer())) {
//            params.put("career", info.getCareer());
//        }
//
//        params.put("education", info.getEducation());
//        params.put("emotionStatus", info.getEmotionStatus());
//        if (info.getHeight() > 0) {
//            params.put("height", info.getHeight());
//        }
//
//
//        params.put("sex", info.getSex());
//        return params;
//    }
//
//    static RequestParams getCourseListParams(long gymId, String date, int category, PageInfo info) {
//        RequestParams params = getPageInfoParams(info);
//        params.put("studioId", gymId)
//                .put("courseDate", date);
//        if (category >= 0) {
//            params.put("category", category);
//        }
//        return params;
//    }
//
//    static RequestParams getDoBookCourseParams(long courseId) {
//        RequestParams params = getParams();
//        params.put("sessionId", courseId);
//        return params;
//    }
//
//    static RequestParams getSignParams(String url) {
//        return getParams().put("token", url);
//    }
//}
