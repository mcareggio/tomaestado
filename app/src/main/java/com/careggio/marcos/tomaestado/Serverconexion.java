package com.careggio.marcos.tomaestado;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.net.URL;

public class Serverconexion extends AppCompatActivity implements View.OnClickListener {
    private Button probar,reset,ver_parametros,descarga,expor_up,completo;
    private EditText ip;
    private TextView salida;
    private DownloadBack dwnb;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_INTERNET = 1;
    private static String[] PERMISSIONS_INTERNET = {
            Manifest.permission.INTERNET,

    };
    private int VALOR_RETORNO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverconexion);
        Serverconexion.verifyStoragePermissions(this);
        ip=(EditText)findViewById(R.id.ip);
        probar=(Button)findViewById(R.id.probar);
        probar.setOnClickListener(this);
        reset=(Button)findViewById(R.id.reset);
        reset.setOnClickListener(this);
        ver_parametros=(Button)findViewById(R.id.parametros);
        ver_parametros.setOnClickListener(this);
        descarga=(Button)findViewById(R.id.descarga);
        descarga.setOnClickListener(this);
        expor_up=(Button)findViewById(R.id.exportar_y_subir);
        expor_up.setOnClickListener(this);
        completo=(Button)findViewById(R.id.exportar_y_subir_completo);
        completo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.probar:
                HttpManejador httpman3=new HttpManejador(ip.getText().toString(),"","");
                httpman3.enviarPeticion(this,"");

                break;
            case R.id.reset:

                break;
            case R.id.parametros:

                break;
            case R.id.descarga:

                URL [] url = new URL[4];
                try {
                    url[0] = new URL("http", ip.getText().toString(), "tmp/ryf_fox.csv");
                    url[1] = new URL("http", ip.getText().toString(), "tmp/orden_agua.csv");
                    url[2] = new URL("http", ip.getText().toString(), "tmp/orden_energia.csv");
                    url[3] = new URL("http", ip.getText().toString(), "tmp/cons_hist_todo_fox.csv");
                }catch(Exception e){

                }

                for(int i=0;i<4;i++) {
                     new DownloadBack().execute(url[i]);

                }
                break;
            case R.id.exportar_y_subir:
                Periodo per=new Periodo();
                Estado est=new Estado();
                File archivo_exp_1=est.exportarEstados(this,per.getPeriodoActual(this),"energia");
                File archivo_exp_2=est.exportarEstados(this,per.getPeriodoActual(this),"agua");
                HttpManejador httpman=new HttpManejador(ip.getText().toString(),"user","pass");
                httpman.subirArchivoAServer(this,archivo_exp_1);
                httpman.subirArchivoAServer(this,archivo_exp_2);
                break;
            case R.id.exportar_y_subir_completo:
                Periodo per2=new Periodo();
                Estado est2=new Estado();
                File archivo_exp_ambos=est2.exportarEstadosAmbosServicios(this,per2.getPeriodoActual(this));
                HttpManejador httpman2=new HttpManejador(ip.getText().toString(),"user","pass");
                httpman2.subirArchivoAServer(this,archivo_exp_ambos);
                break;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisioninternet= ActivityCompat.checkSelfPermission(activity,Manifest.permission.INTERNET);
        if(permisioninternet!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISSIONS_INTERNET,REQUEST_INTERNET);
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
