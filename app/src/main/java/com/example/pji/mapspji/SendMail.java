package com.example.pji.mapspji;

import android.content.Intent;

import java.net.URISyntaxException;

/**
 * Created by jihadgaza on 13/01/2016.
 */
public class SendMail {
    private Intent intent;
    public SendMail(String destinataire, String message) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{destinataire});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Localisation carte");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,message);
        emailIntent.setType("message/rfc822");
        try {
           intent=emailIntent.createChooser(Intent.getIntent(Intent.EXTRA_EMAIL), "Choose Email");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Intent getIntent() {
        return intent;
    }
}
