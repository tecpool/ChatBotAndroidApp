package in.tecpool.chatbot;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;

/**
 * Created by umesha on 3/5/2017.
 */

public class WebserviceCall {

    String namespace = "http://chatbot.tecpool.in/";
    private String url = "";
    private String baseUrl = "google.com";  // webservice.uvtechsoft.com - uvtechsoft.com both check

    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    UVModule uv;
    Context mContext;
    Activity mActivity;


    WebserviceCall(Context context,Activity activity) {
        mContext=context;
        mActivity=activity;
        url=context.getResources().getString(R.string.url);
        uv=new UVModule(mContext);
    }


    WebserviceCall(Context context,String dummy) {
        mContext=context;
        url=context.getResources().getString(R.string.url);
        uv=new UVModule(mContext);
    }




    /**
     * Set Envelope
     */
    protected void SetEnvelope() {

        try {

            // Creating SOAP envelope
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //You can comment that line if your web service is not .NET one.
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.debug = true;
        } catch (Exception e) {
            //  System.out.println("Soap Exception---->>>" + e.toString());
        }
    }

    // MethodName variable is define for which webservice function  will call
    public String getString(String MethodName,String params[][])
    {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);

            for(int i=0;i<params.length;i++)
            {
                PropertyInfo weightProp =new PropertyInfo();
                weightProp.setName(params[i][0]);
                weightProp.setValue(params[i][1]);
                weightProp.setType(String.class);
                request.addProperty(weightProp);
            }

            SetEnvelope();

            try {
                ConnectivityManager connec =
                        (ConnectivityManager)mActivity.getSystemService(mContext.CONNECTIVITY_SERVICE);

                // Check for network connections
                if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

                    if(uv.internetConnectionAvailable(30000,baseUrl))
                    {
                        //SOAP calling webservice
                        androidHttpTransport.call(SOAP_ACTION, envelope);

                        //Got Webservice response
                        String result = envelope.getResponse().toString();

                        return result;
                    }
                    return "Bad Network, Cant connect to server";
                }
                return "No Internet Connection";

            } catch (ConnectException e1) {
                // TODO: handle exception
                return "Bad Network, Cant connect to server";
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }

    }

    public String getStringWithoutActivity(String MethodName,String params[][])
    {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);

            for(int i=0;i<params.length;i++)
            {
                PropertyInfo weightProp =new PropertyInfo();
                weightProp.setName(params[i][0]);
                weightProp.setValue(params[i][1]);
                weightProp.setType(String.class);
                request.addProperty(weightProp);
            }

            SetEnvelope();

            try {

                    if(uv.internetConnectionAvailable(30000,baseUrl))
                    {
                        //SOAP calling webservice
                        androidHttpTransport.call(SOAP_ACTION, envelope);

                        //Got Webservice response
                        String result = envelope.getResponse().toString();

                        return result;
                    }
                    return "Bad Network, Cant connect to server";
            } catch (ConnectException e1) {
                // TODO: handle exception
                return "Bad Network, Cant connect to server";
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }

    }




    public Integer getInt(String MethodName,String params[][])
    {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);

            for(int i=0;i<params.length;i++)
            {
                PropertyInfo weightProp =new PropertyInfo();
                weightProp.setName(params[i][0]);
                weightProp.setValue(params[i][1]);
                weightProp.setType(String.class);
                request.addProperty(weightProp);
            }

            SetEnvelope();

            try {

                if(uv.internetConnectionAvailable(10000,baseUrl))
                {
                    //SOAP calling webservice
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    //Got Webservice response
                    String result = envelope.getResponse().toString();

                    return  Integer.parseInt(result);
                }
                return 404; // no internet connection

            } catch (Exception e) {
                // TODO: handle exception
                return -1;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }

    }

}
