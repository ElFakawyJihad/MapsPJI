package com.example.pji.mapspji.ftp;

/**
 * This <code>UploadFileAsStreamSample</code> class
 * This sample demonstrates how to upload a file via SFTP
 * This sample uses a convenient method, for a Spring-able version of this refer to FTPsClient
 * @see com.zehon.ftp.FTPsClient
 * Please refer to http://www.zehon.com/features.htm for more information about our SFTP.
 * @author Zehon Team (we're happy to serve you!)  <a href="http://www.zehon.com/">http://www.zehon.com/</a>
 *
 */

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class SendDownloadFileFTP {
    private static final String HOST = "31.170.164.84";
    private static final String USERNAME = "u782179569";
    private static final String PASS = "pjiandroid";
    FTPClient client=null;
    public SendDownloadFileFTP(){
        client=new FTPClient();
        try {
            client.connect(HOST,21);
            client.login(USERNAME, PASS);
            client.setPassive(true);
            Log.i("information","c'est connecter "+client.isConnected());
        } catch (Exception e) {
            Log.e("erreurrr",e.toString());
        }
    }
    public void upload(File f) throws FTPException, IOException, FTPDataTransferException, FTPIllegalReplyException, FTPAbortedException {
        client.changeDirectory("/public_html/app");
        client.upload(f);
    }
    public File download(String name) throws FTPException, IOException, FTPIllegalReplyException, FTPDataTransferException, FTPAbortedException {
        client.changeDirectory("/public_html/app");
        File f  = new File(Environment.getExternalStorageDirectory() + File.separator + name);
        client.download(name, f);
        return f;
    }
    public void disconnect() throws FTPException, IOException, FTPIllegalReplyException {
        client.disconnect(true);

    }



}