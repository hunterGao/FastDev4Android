package com.huntergao.dailyweather;

/**
 * 网络连接的回调接口
 * 主要处理连接完成和连接出错的逻辑
 * @author HG
 *
 */
public interface HttpCallBackListener {

	/**
	 * 连接结束时
	 * @param response 获得服务端返回的数据
	 */
	public void onFinish(String response);
	
	/**
	 * 连接出错时
	 * @param e
	 */
	public void onError(Exception e);
}
