package zerofield.mywallpaper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Preference circlePref = getPreferenceScreen().findPreference("numberOfCircles");
        circlePref.setOnPreferenceChangeListener(mOnPreferenceChangeListener);
    }

    Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            if (newValue != null && newValue.toString().length() > 0
                    && newValue.toString().matches("\\d*")) {
                return true;
            }
            // If now create a message to the user
            Toast.makeText(MyPreferencesActivity.this, "Invalid Input",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    };

}
