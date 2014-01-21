// Programme exercice Eliocity
/*L’application contient 3 fonctionnalités :
La connexion à la plateforme Xee via le serveur Oauth 2.0
L’affichage d’une vue “Utilisateur” contenant les nom et prénom de l’utilisateur précédemment
connecté L’affichage d’une vue “Carte” contenant une carte affichant la position actuelle du téléphone
dans le monde. La carte doit être centrée sur la position actuelle du téléphone*/


package com.ibrahima.appliexo;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



import android.net.http.MySSLSocketFactory;
import android.net.http.SslError;
import android.graphics.Bitmap;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

 
@SuppressLint("SetJavaScriptEnabled")

public class MainActivity extends Activity {
	final Activity activity = this;
	 
    public static String OAUTH_URL = "https://staging.xee.com/v1/auth/auth";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://staging.xee.com/v1/auth/access_token.json";
    public static String OAUTH_ACCESS_NAME_URL = "https://staging.xee.com/v1/user/me.json?access_token=";
    public static String CLIENT_ID = "zu9eSH5zEzPJzxsRMZGB";    
    public static String SCOPE = "user%20data";
    public static String PASSWORD = "vMQBoZl64P1mRmO3TM5I";
    public static String CALLBACK_URL = "http%3A%2F%2Flocalhost%2F";
    public String accessCode = null;
    public String accessToken;
    final String EXTRA_Name = "name";
    final String EXTRA_firstName = "firstName";
   

	    //---------------------------------------------------------------------------------------------------------------------------------------
	    //---------------------------------------------------------------------------------------------------------------------------------------
	
	    @Override
	    public void onCreate(final Bundle savedInstanceState) 
	    {
	    	accessCode = null;
	    	super.onCreate(savedInstanceState);
	    	this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
	        setContentView(R.layout.activity_main);    
	        String url = OAUTH_URL + "?client_id=" + CLIENT_ID + "&scope=" + SCOPE + "&redirect_uri=" + CALLBACK_URL;
	        vueweb(url);
	    }
	      
	    //---------------------------------------------------------------------------------------------------------------------------------------
	    //---------------------------------------------------------------------------------------------------------------------------------------
	    // Affichage vue web et récupération Code Authentification
	    
	    public void vueweb( String url)
	    {
	    	final WebView webview = (WebView)findViewById(R.id.webview);
	        webview.getSettings().setJavaScriptEnabled(true);
	        webview.setWebChromeClient(new WebChromeClient() 
	        {
	        	public void onProgressChanged(WebView view, int progress)
	            {
	                activity.setTitle("Chargement...");
	                activity.setProgress(progress * 100);
	                if(progress == 100)
	                activity.setTitle(R.string.app_name);
	            }
	        });
	        
	        webview.setWebViewClient(new WebViewClient()
	        {
	        	public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) 
	        	{
	                handler.proceed() ;                
	        	}
	            public void onPageStarted(WebView view, String url, Bitmap favicon) 
	            {
	            	if (accessCode==null)
	            	{	 
	            		String accessCodeFragment = "code=";
	            
	            		 if(url.contains(accessCodeFragment)) 
	            			{
			            		accessCode = url.substring(url.indexOf(accessCodeFragment)+accessCodeFragment.length());
			            		//webview.loadUrl("");
			            		//webview.clearCache(true);
			            		//webview.destroy();
			            		token();
	            	    	}
	            	}	 
	            }
	        });
	        webview.loadUrl(url);
	    }
    
    	//---------------------------------------------------------------------------------------------------------------------------------------
    	//---------------------------------------------------------------------------------------------------------------------------------------
	    // Gestion récupération Token et récupération infos user
	    public void token() 
	    {
	    	
	           	HttpPost httppost = new HttpPost(OAUTH_ACCESS_TOKEN_URL);
	        	String authorizationString = "Basic " + Base64.encodeToString((CLIENT_ID + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);
		        authorizationString.replace("\n", "");
		        httppost.setHeader("Authorization", authorizationString);
		        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		        HttpClient httpclient = getNewHttpClient();
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
		        nameValuePairs.add(new BasicNameValuePair("code", accessCode));
		        nameValuePairs.add(new BasicNameValuePair("redirect_uri", "http://localhost/"));
		        nameValuePairs.add(new BasicNameValuePair("scopes", SCOPE));
		        String json = null;
		        try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
		        	json = EntityUtils.toString(response.getEntity());
		        
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        accessToken=json.substring(json.indexOf(":")+2, json.indexOf(",")-1);
		        HttpGet httpget = new HttpGet(OAUTH_ACCESS_NAME_URL + accessToken );
		        HttpClient httpclient2 = getNewHttpClient();
	        	try {
	        		HttpResponse resp = httpclient2.execute(httpget);
					json = EntityUtils.toString(resp.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	Toast.makeText(getApplicationContext(), json, Toast.LENGTH_LONG).show();
	        			// récupère les noms prénoms en faisant un extraction d'une chaine entre 2 mot)
		        String username = json.substring(json.indexOf("name")+7, json.indexOf("firstName")-3);
		        String userfirstname = json.substring(json.indexOf("firstName")+12, json.indexOf("gender")-3);
		        
		        
		         
		        
				Intent intent = new Intent(MainActivity.this, HelloGoogleMapActivity.class);
					// transfère les noms et prénom en mémoire et ouvre la vue carte
				intent.putExtra(EXTRA_Name, username);
				intent.putExtra(EXTRA_firstName, userfirstname);
	    		startActivity(intent);
	    		
	    }

	    //---------------------------------------------------------------------------------------------------------------------------------------
	    //---------------------------------------------------------------------------------------------------------------------------------------
	    // Gestion de la connexion client-Serveur
	    
	    public HttpClient getNewHttpClient() {
	        try {
	            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	            trustStore.load(null, null);
	            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	            HttpParams params = new BasicHttpParams();
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	            SchemeRegistry registry = new SchemeRegistry();
	            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	            registry.register(new Scheme("https", sf, 443));
	            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	            return new DefaultHttpClient(ccm, params);
	        } catch (Exception e) {
	            return new DefaultHttpClient();
	        }
	    }
	    
	    
}	    







