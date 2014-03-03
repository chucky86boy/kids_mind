package com.mb.kids_mind;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class CDialog {

	private static Dialog m_loadingDialog = null;

	public static void showLoading(Context context) {
		if (m_loadingDialog == null) {
			m_loadingDialog = new Dialog(context, R.style.TransDialog);
			ProgressBar pb = new ProgressBar(context);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			m_loadingDialog.addContentView(pb, params);
			m_loadingDialog.setCancelable(false);
		}

		m_loadingDialog.show();
	}

	public static void hideLoading() {
		if (m_loadingDialog != null) {
			m_loadingDialog.dismiss();
			m_loadingDialog = null;
		}
	}

}
