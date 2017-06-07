package fr.pacreau.teamu.util;

import android.app.Application;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * spacreau
 * fr.pacreau.teamu.util
 *
 * @author spacreau
 * @since 07/06/2017
 */

public class DateUtil {

	private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd/mm/YYYY");
	private static final SimpleDateFormat formatDateTime = new SimpleDateFormat("dd/mm/YYYY HH:MM");

	public static String formatDate(Date p_oDate ) {
		if (p_oDate != null) {
			return formatDate.format(p_oDate);
		}
		return "";
	}
	public static String formatDateTime(Date p_oDate ) {
		if (p_oDate != null) {
			return formatDateTime.format(p_oDate);
		}
		return "";
	}

	public static Date convertDateTimeString(String p_oDate ) {
		if (p_oDate != null) {
			try {
				return formatDateTime.parse(p_oDate);
			} catch (ParseException e) {
				Log.e("DateUtil", " java.text.ParseException : " + p_oDate, e);
			}
		}
		return null;
	}
}
