/* AziLink: USB tethering for Android
 * Copyright (C) 2009 by James Perry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lfx.azilink;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.lfx.azilink.net.SelectThread;

/**
 * AboutActivity displays author and version information to the user.
 * 
 * @author Jim Perry
 *
 */
public class AboutActivity extends Activity implements OnClickListener {
	
	/**
	 * Retrieves the current version from the Android manifest and places it in the dialog.
	 */
	@SuppressLint("SetTextI18n")
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		setContentView(R.layout.about);
		
		Button ok = (Button) findViewById(R.id.Return);
		ok.setOnClickListener(this);
		
		PackageManager pm = getPackageManager();
		String version = "version unknown";
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			version = pi.versionName;
		} catch (Throwable ignored) {}
		
		TextView ver = (TextView) findViewById(R.id.about_version_tag);
		ver.setText("AziLink " + version + " by Jim Perry & BlinkSD");
		
		String port = String.valueOf(AziLinkApplication.getPort());
		
		TextView aboutHowto = findViewById(R.id.about_body);
		aboutHowto.setText(aboutHowto.getText().toString().replaceAll("//PORT//", port));
		
		TextView aboutCfg = findViewById(R.id.about_config);
		aboutCfg.setText(aboutCfg.getText().toString().replaceAll("//PORT//", port));
	}
	
	/**
	 * Closes the dialog when the user hits the return button.
	 */
	public void onClick(View v) {
		finish();
	}
}
