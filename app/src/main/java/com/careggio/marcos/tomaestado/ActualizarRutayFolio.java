package com.careggio.marcos.tomaestado;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by marcos on 11/8/2022.
 */

public class ActualizarRutayFolio {
    public static String getTableStructQuery(){
        return "create table usuarios_tmp(id integer  primary key AUTOINCREMENT,ruta integer,folio integer, nombre_apellido text, direccion text,nro_med_energia integer,nro_med_agua integer,med_energia integer,med_agua integer);";
    }
    public String[] cargarTablaTemporal(Context context,String uri) {
        Bbdd bd=new Bbdd();

        if(!bd.isExistTabla(context,"usuarios_tmp"))
            bd.ejecutarConsulta(context,ActualizarRutayFolio.getTableStructQuery());

        bd.vaciar_tabla(context,"usuarios_tmp");

        boolean carga=true;
        String falloOutput="";
        boolean fallo=false;
        int i=0;
        String s="";

        try {
            // Creamos un objeto InputStreamReader, que será el que nos permita
            // leer el contenido del archivo de texto.
            InputStreamReader archivo;


            System.out.println("Archivo "+uri);
            File file=new File(uri);

            archivo = new InputStreamReader(new FileInputStream(file));
            // Creamos un objeto buffer, en el que iremos almacenando el contenido
            // del archivo.
            BufferedReader br = new BufferedReader(archivo);

            bd.resetearTabla(context,"usuarios_tmp");
            String [][] array=new String[10][10];
            array[0][0]="ruta";
            array[1][0]="folio";
            array[2][0]="nombre_apellido";
            array[3][0]="direccion";
            array[4][0]="nro_med_energia";
            array[5][0]="nro_med_agua";
            array[6][0]="med_energia";
            array[7][0]="med_agua";

            int med_energia=0;
            int med_agua=0;
            br.readLine();


            while(br.ready()&&carga) {
                s=new String(br.readLine());
                falloOutput="";
                String [] reslt;
                //reslt=s.split(",");
                //System.out.println("spliteado por comas,"+reslt.length);
                //if(reslt.length<=5)
                reslt=s.split(";");

                System.out.println(s.toString()+reslt.length);

                if(!Calculo.isRuta(reslt[1])) {
                    falloOutput += " ruta no valida ";
                    carga=false;
                }
                if(!Calculo.isFolio(reslt[2])){
                    falloOutput+=" fallo folio ";
                    carga=false;
                }
                if(!Calculo.isValidText(reslt[7])){
                    falloOutput+=" fallo nombre ";
                    carga=false;
                }
                if(!Calculo.isValidText(reslt[8])){
                    falloOutput+=" fallo direccion ";
                    carga=false;
                }
                if(reslt[13]!=null&&reslt[15]!=null&&reslt[28]!=null&&!Calculo.isNumeric(reslt[13])&&!Calculo.isNumeric(reslt[15])&&!Calculo.isNumeric(reslt[28])){
                    System.out.println(reslt[13]+""+reslt[15]+""+reslt[28]);
                    falloOutput+=" fallo datos comp ";
                    carga=false;
                }
                if(reslt[18]!=null&&!Calculo.isNumeric(reslt[18])){
                    falloOutput+=" fallo med energia ";
                    carga=false;
                }
                if(reslt[29]!=null&&!Calculo.isNumeric(reslt[29])){
                    falloOutput+=" fallo med agua ";
                    carga=false;
                }

                if(carga){
                    int tipo_us=Integer.parseInt(reslt[13]);
                    int tipomede=Integer.parseInt(reslt[15]);
                    int tipomeda=Integer.parseInt(reslt[28]);
                    if(tipo_us!=7)
                    {
                        if(tipomede!=0)
                        {

                            med_energia=1;
                        }
                        else
                            med_energia=0;
                        if(tipomeda!=0&&tipomeda!=17)
                        {
                            med_agua=1;
                        }
                        else
                            med_agua=0;

                        array[0][1]=reslt[1];
                        array[1][1]=reslt[2];
                        array[2][1]=reslt[7];
                        array[3][1]=reslt[8];
                        array[4][1]=reslt[18];
                        array[5][1]=reslt[29];
                        array[6][1]=String.valueOf(med_energia);
                        array[7][1]=String.valueOf(med_agua);
                        bd.insertar(context,"usuarios_tmp",array);
                    }
                    else {
                        System.out.println("No Carga Usuario dado de baja");
                    }
                }
                i++;
            }
            if(!carga)
            {
                fallo=true;
                System.out.println("Error al cargar ruta y folio"+falloOutput);
            }
            System.out.println("fin lectura");


        } catch (Exception e) {e.printStackTrace();
            fallo=true;
            System.out.println("Error al cargar ruta y folio");
        }
        String [] res=new String[4];
        if(fallo) {
            bd.vaciar_tabla(context,"usuarios_tmp");
            res[0] = "1";
            res[1] = falloOutput;
            res[2]=String.valueOf(i);
            res[3]=s;
        }
        else {
            res[0] = "0";
            res[1]="";
        }
        return res;
    }
    public void actualizarNombreDireNrommed(Context context){
        Bbdd bd=new Bbdd();
        String consulta="SELECT ruta,folio,nombre_apellido,direccion,nro_med_energia,nro_med_agua,med_energia,med_agua from usuarios_tmp";
        String [][] res=bd.consutlar(context,consulta);
        int i=0;

        while(res[0][i]!=null){
            consulta="update usuarios set nombre_apellido="+res[2][i]+",direccion="+res[3][i]+",nro_med_energia="+res[3][i]+",nro_med_agua="+res[4][i]+",med_energia="+res[5][i]+",med_agua="+res[6][i]+" where ruta="+res[0][i]+" and folio="+res[1][i];

            bd.ejecutarConsulta(context,consulta);
            i++;
        }
    }
    public String[][] actualizarRutayFolio(){

        return null;
    }

}
