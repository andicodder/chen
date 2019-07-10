package com.lilliemountain.chen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class AboutDeveloperActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        findViewById(R.id.lmgit).setOnClickListener(this);
        findViewById(R.id.lmweb).setOnClickListener(this);
        findViewById(R.id.lmig).setOnClickListener(this);
        findViewById(R.id.lmtwit).setOnClickListener(this);

        findViewById(R.id.rsgit).setOnClickListener(this);
        findViewById(R.id.rsig).setOnClickListener(this);
        findViewById(R.id.rslinkedin).setOnClickListener(this);
        findViewById(R.id.rstwit).setOnClickListener(this);

        findViewById(R.id.ctgit).setOnClickListener(this);
        findViewById(R.id.ctig).setOnClickListener(this);
        findViewById(R.id.ctlinkedin).setOnClickListener(this);
        findViewById(R.id.cttwt).setOnClickListener(this);
    }
    void openWeb(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    void openInstagram(String username){
        Uri uri = Uri.parse("http://instagram.com/_u/"+username);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/"+username)));
        }
    }
    public void openLinkedInPage(String linkedId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + linkedId));
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + linkedId));
        }
        startActivity(intent);
    }
    public void startTwitter(String username) {

        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+username));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+username));
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lmgit:
                openWeb("https://www.github.com/lilliemountain");
                break;
            case R.id.lmweb:
                openWeb("https://www.lilliemountain.com/");
                break;
            case R.id.lmig:
                openInstagram("lillie.mountain");
                break;
            case R.id.lmtwit:
                startTwitter("LillieMountain");
                break;

            case R.id.rsgit:
                openWeb("https://www.github.com/g0li");
                break;
            case R.id.rsig:
                openInstagram("thisisroshans");
                break;
            case R.id.rslinkedin:
                openLinkedInPage("roshan-singh-aba716116");
                break;
            case R.id.rstwit:
                startTwitter("thisisroshans");
                break;

            case R.id.ctgit:
                openWeb("https://www.github.com/Blaze10");
                break;
            case R.id.ctig:
                openInstagram("10blze");
                break;
            case R.id.ctlinkedin:
                openLinkedInPage("chinmay-tagade-42a889137");
                break;
            case R.id.cttwt:
                startTwitter("chinmay_tagade");
                break;
        }
    }
}
