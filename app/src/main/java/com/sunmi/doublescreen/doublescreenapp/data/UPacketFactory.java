package com.sunmi.doublescreen.doublescreenapp.data;

import com.google.gson.Gson;

import sunmi.ds.SF;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DSData.DataType;
import sunmi.ds.data.DataPacket;

/**
 * 用户数据包工厂类
 * 
 * @author longtao.li
 */
public class UPacketFactory {
	
	static Gson gson = new Gson();
	
	//------------DATA----------

	/**
	 * build一个数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildPack(String recePackageName, DataType dataType, DataModel dataModel, String dataJson, ISendCallback callback) {
		String dataJsonStr = createJson(dataModel, dataJson);
		return new DataPacket.Builder(dataType).recPackName(recePackageName).data(dataJsonStr).addCallback(callback).isReport(true).build();
	}
	
	/**
	 * build一个指定taskId的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildPack(String recePackageName, long taskId, DataType dataType, DataModel dataModel, String dataJson, ISendCallback callback) {
		String dataJsonStr = createJson(dataModel, dataJson);
		return new DataPacket.Builder(dataType).recPackName(recePackageName).taskId(taskId).data(dataJsonStr).addCallback(callback).isReport(true).build();
	}
	
	
	//---------------FILE-------------

	/**
	 * build一个文件传输的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildFilePacket(String recePackName, String filePath, ISendCallback callback) {
		return new DataPacket.Builder(DataType.FILE).recPackName(recePackName).data(filePath).
				addCallback(callback).isReport(true).build();
	}
	
	

	//------------CMD--------------

	/**
	 * build一个CMD数据包，可以指定要使用的缓存文件Id
	 * @param recePackageName
	 * @param dataModel
	 * @param dataJson
	 * @param fileId 如果没有则传0
	 * @param callback
     * @return
     */
	public static DataPacket buildCMDPack(String recePackageName, DataModel dataModel, String dataJson, long fileId, ISendCallback callback) {
		String dataJsonStr = createJson(dataModel, dataJson);
		return new DataPacket.Builder(DataType.CMD).recPackName(recePackageName).data(dataJsonStr).fileId(fileId).addCallback(callback).isReport(true).build();
	}


	/**
	 * build一个打开app的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildOpenApp(String packageName, ISendCallback callback) {
		DataPacket pack = buildPack(SF.SUNMI_DSD_PACKNAME, DataType.CMD, DataModel.OPEN_APP, packageName, callback);
		return pack;
	}

	/**
	 * 副屏关机
	 */

	public static DataPacket buildShutDown(String recePacakgeName, ISendCallback callback) {
		DataPacket pack = buildPack(recePacakgeName, DataType.CMD, DataModel.SHUTDOWN, "", callback);
		return pack;
	}
	/**
	 * 副屏重启
	 */

	public static DataPacket buildReboot(String recePacakgeName, ISendCallback callback) {
		DataPacket pack = buildPack(recePacakgeName, DataType.CMD, DataModel.REBOOT, "", callback);
		return pack;
	}

	/**
	 * build一个获取副屏设置的数据的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildSecondScreenData(String recePackageName, ISendCallback callback) {
		DataPacket pack = buildPack(recePackageName, DataType.CMD, DataModel.GET_SECOND_SCREEN_DATA, "", callback);
		return pack;
	}


	/**
	 * build一个解锁副屏的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildScreenUnlock(String recePackageName, ISendCallback callback) {
		DataPacket pack = buildPack(recePackageName, DataType.CMD, DataModel.SCREEN_UNLOCK, "", callback);
		return pack;
	}
	
	/**
	 * build一个读取副屏亮度的数据包
	 *
	 * @param callback
	 * @return
	 */
	public static DataPacket buildReadbrightness(String recePackageName, String sender, ISendCallback callback) {
		DataPacket pack = buildPack(recePackageName, DataType.CMD, DataModel.READ_BRIGHTNESS, sender, callback);
		return pack;
	}


	/**
	 * build一个设置亮度的数据包
	 *
	 * @param brightness
	 * @return
	 */
	public static DataPacket buildSetbrightness(String recePackageName, int brightness, ISendCallback callback) {
		DataPacket pack = buildPack(recePackageName, DataType.CMD, DataModel.SET_BRIGHTNESS, brightness+"", callback);
		return pack;
	}

	/**
	 * 删除HCserver文件夹
	 * @param receiverPackageName
	 * @param text
	 * @param callback
	 * @return
	 */
	public static DataPacket remove_folders(String receiverPackageName, String text, ISendCallback callback) {
		DataPacket pack = buildPack(receiverPackageName, DataType.CMD, DataModel.CLEAN_FILES, text, callback);
		return pack;
	}

	/**
	 * build一个显示文字的数据包
	 *
	 * @param text
	 * @return
	 */
	public static DataPacket buildShowText(String recePackageName, String text, ISendCallback callback) {
		DataPacket pack = buildPack(recePackageName, DataType.DATA, DataModel.TEXT, text, callback);
		return pack;
	}
	

	public static String createJson(DataModel dataModel, String dataStr){
		Data data = new Data();
		data.dataModel = dataModel;
		data.data = dataStr;
		String dataJson = gson.toJson(data);
		return dataJson;
	}



}
