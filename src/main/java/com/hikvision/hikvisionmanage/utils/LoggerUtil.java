/**
 * Copyright by LuNanTing
 * Date:2018年12月29日上午9:20:56
 * @author : LuNanTing
 */
package com.hikvision.hikvisionmanage.utils;

import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @{记录日志}
 * @author 创建人: xuemei
 * @Title 标 题: LoggerUtil.java
 * @update 修改人：xuemei
 * @date 修改事件：2018年12月8日
 */
public class LoggerUtil {

	private static final Object SEPARATOR = ",";

	/**
	 * @title 写入日志
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 *            需要写入的内容
	 * @return 返回类型：void
	 */
	public static void error(String msg) {
		LoggerFactory.getLogger(getClassName()).error(msg);
	}

	/**
	 * @title {写入日志}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 * @param obj
	 *            需要记录的类名
	 * @return 返回类型：void
	 */
	public static void error(String msg, Object... obj) {
		LoggerFactory.getLogger(getClassName()).error(msg, obj);
	}

	/**
	 * @title {将异常信息转为字符串类型}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param e
	 * @return
	 * @return 返回类型：String
	 */
	private static String printException(Throwable e) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		String logInfo = getLogInfo(stackTraceElement);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		StringBuffer sb = sw.getBuffer();
		info(logInfo);
		return sb.toString();
	}

	/**
	 * @title {将异常记录在日志中}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param e
	 *            异常
	 * @return 返回类型：void
	 */
	public static void error(Throwable e) {
		error(printException(e));
	}

	// public static void error(String position,String msg, Throwable e ,
	// Object...obj ) {
	// LoggerFactory.getLogger(position).error(msg+"\n"+printException(e),obj);
	// }

	public static void error(String msg, Throwable e) {
		LoggerFactory.getLogger(getClassName()).error(msg + "\n" + printException(e));
	}

	public static void error(String msg, Throwable e, Object... obj) {
		LoggerFactory.getLogger(getClassName()).error(msg + "\n" + printException(e), obj);
	}

	/**
	 * @title {记录提示信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 *            提示的内容
	 * @return 返回类型：void
	 */
	public static void warn(String msg) {
		LoggerFactory.getLogger(getClassName()).error(msg);
	}

	/**
	 * @title {记录提示信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 *            提示内容
	 * @param obj
	 *            提示信息相关的类
	 * @return 返回类型：void
	 */
	public static void warn(String msg, Object... obj) {
		LoggerFactory.getLogger(getClassName()).error(msg, obj);
	}

	/**
	 * @title {需要记录的重要信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 * @return 返回类型：void
	 */
	public static void info(String msg) {
		LoggerFactory.getLogger(getClassName()).info(msg);
	}

	/**
	 * @title {自定义重要信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 *            信息内容
	 * @param obj
	 *            信息相关的类
	 * @return 返回类型：void
	 */
	public static void info(String msg, Object... obj) {
		LoggerFactory.getLogger(getClassName()).info(msg, obj);
	}

	/**
	 * @title {记录异常}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param e
	 * @return 返回类型：void
	 */
	public static void info(Throwable e) {
		info(printException(e));
	}

	/**
	 * @title {记录异常，使用特殊的位置标记}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param position
	 *            特殊的位置标记
	 * @param e
	 *            异常
	 * @return 返回类型：void
	 */
	public static void info(String msg, Throwable e) {
		LoggerFactory.getLogger(getClassName()).info(msg + "\n" + printException(e));
	}

	public static void info(String msg, Throwable e, Object... obj) {
		LoggerFactory.getLogger(getClassName()).info(msg + "\n" + printException(e), obj);
	}

	/**
	 * @title {调试信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 * @return 返回类型：void
	 */
	public static void debug(String msg) {
		LoggerFactory.getLogger(getClassName()).debug(msg);
	}

	/**
	 * @title {调试信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param msg
	 * @param obj
	 * @return 返回类型：void
	 */
	public static void debug(String msg, Object... obj) {
		LoggerFactory.getLogger(getClassName()).debug(msg, obj);
	}

	/**
	 * @title {获取堆栈的类名}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @return
	 * @return 返回类型：String
	 */
	private static String getClassName() {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		return stackTraceElement.getClassName();
	}

	/**
	 * @title {输出日志包含的具体信息}
	 * @author 创建人: xuemei
	 * @date 时间： 2018年12月8日
	 * @param stackTraceElement
	 * @return
	 * @return 返回类型：String
	 */
	private static String getLogInfo(StackTraceElement stackTraceElement) {
		StringBuilder logInfoStringBuilder = new StringBuilder();
		// 获取线程名
		String threadName = Thread.currentThread().getName();
		// 获取线程ID
		long threadId = Thread.currentThread().getId();
		// 获取文件名.即xxx.java
		String fileName = stackTraceElement.getFileName();
		// 获取类名.即包名+类名
		String className = stackTraceElement.getClassName();
		// 获取方法名称
		String methodName = stackTraceElement.getMethodName();
		// 获取生日输出行数
		int lineNumber = stackTraceElement.getLineNumber();

		logInfoStringBuilder.append("[ ");
		logInfoStringBuilder.append("threadID=" + threadId).append(SEPARATOR);
		logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
		logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
		logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
		logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
		logInfoStringBuilder.append("lineNumber=" + lineNumber);
		logInfoStringBuilder.append(" ] ");
		return logInfoStringBuilder.toString();
	}
}
