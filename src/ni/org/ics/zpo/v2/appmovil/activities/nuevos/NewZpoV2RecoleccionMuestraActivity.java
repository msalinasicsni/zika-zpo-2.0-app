package ni.org.ics.zpo.v2.appmovil.activities.nuevos;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ni.org.ics.zpo.v2.appmovil.AbstractAsyncActivity;
import ni.org.ics.zpo.v2.appmovil.MainActivity;
import ni.org.ics.zpo.v2.appmovil.MyZpoApplication;
import ni.org.ics.zpo.v2.appmovil.R;
import ni.org.ics.zpo.v2.appmovil.database.ZpoAdapter;
import ni.org.ics.zpo.v2.appmovil.parsers.ZpoV2RecoleccionMuestraXml;
import ni.org.ics.zpo.v2.appmovil.preferences.PreferencesActivity;
import ni.org.ics.zpo.v2.appmovil.utils.Constants;
import ni.org.ics.zpo.v2.appmovil.utils.FileUtils;
import ni.org.ics.zpo.v2.appmovil.domain.ZpoV2RecoleccionMuestra;
import ni.org.ics.zpo.v2.appmovil.AbstractAsyncActivity;
import ni.org.ics.zpo.v2.appmovil.database.ZpoAdapter;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by FIRSTICT on 10/31/2016.
 * V1.0
 */
public class NewZpoV2RecoleccionMuestraActivity extends AbstractAsyncActivity {

    protected static final String TAG = NewZpoV2RecoleccionMuestraActivity.class.getSimpleName();

    private ZpoAdapter zpoA;
    private static ZpoV2RecoleccionMuestra mZpo02 = new ZpoV2RecoleccionMuestra();

	public static final int ADD_ZP02_ODK = 1;
	public static final int EDIT_ZP02_ODK = 2;

    Dialog dialogInit;
    private SharedPreferences settings;
    private String username;
    private String mRecordId = "";
	private Integer accion = 0;
    private String event;
    private boolean esMadre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FileUtils.storageReady()) {
            Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.error)  + "," + getString(R.string.storage_error),Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        username =
                settings.getString(PreferencesActivity.KEY_USERNAME,
                        null);
        String mPass = ((MyZpoApplication) this.getApplication()).getPassApp();
        zpoA = new ZpoAdapter(this.getApplicationContext(),mPass,false,false);
        mRecordId = getIntent().getExtras().getString(Constants.RECORDID);
        event = getIntent().getExtras().getString(Constants.EVENT);
        mZpo02 = (ZpoV2RecoleccionMuestra) getIntent().getExtras().getSerializable(Constants.OBJECTO_ZP02);
        esMadre = getIntent().getExtras().getBoolean("ES_MADRE", true);
        createInitDialog();
    }

    /**
     * Presenta dialogo inicial
     */

    private void createInitDialog() {
        dialogInit = new Dialog(this, R.style.FullHeightDialog);
        dialogInit.setContentView(R.layout.yesno);
        dialogInit.setCancelable(false);

        //to set the message
        TextView message =(TextView) dialogInit.findViewById(R.id.yesnotext);
        if (mZpo02 !=null){
            message.setText(getString(R.string.edit)+ " " + getString(R.string.maternal_b_3)+"?");
        }
        else{
            message.setText(getString(R.string.add)+ " " + getString(R.string.maternal_b_3)+"?");
        }

        //add some action to the buttons

        Button yes = (Button) dialogInit.findViewById(R.id.yesnoYes);
        yes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogInit.dismiss();
                addZpo02BiospecimenCollection();
            }
        });

        Button no = (Button) dialogInit.findViewById(R.id.yesnoNo);
        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Cierra
                dialogInit.dismiss();
                finish();
            }
        });
        dialogInit.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.MENU_BACK){
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.MENU_HOME){
            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
    	if(requestCode == ADD_ZP02_ODK||requestCode == EDIT_ZP02_ODK) {
            if(resultCode == RESULT_OK) {
                Uri instanceUri = intent.getData();
                //Busca la instancia resultado
                String[] projection = new String[] {
                        "_id","instanceFilePath", "status","displaySubtext"};
                Cursor c = getContentResolver().query(instanceUri, projection,
                        null, null, null);
                c.moveToFirst();
                //Captura la id de la instancia y la ruta del archivo para agregarlo al participante
                Integer idInstancia = c.getInt(c.getColumnIndex("_id"));
                String instanceFilePath = c.getString(c.getColumnIndex("instanceFilePath"));
                String complete = c.getString(c.getColumnIndex("status"));
                //cierra el cursor
                if (c != null) {
                    c.close();
                }
                if (complete.matches("complete")){
                    //Parsear el resultado obteniendo un tamizaje si esta completo
                    parseZpo02BiospecimenCollection(idInstancia, instanceFilePath,accion);
                }
                else{
                    Toast.makeText(getApplicationContext(),	getString(R.string.err_not_completed), Toast.LENGTH_LONG).show();
                }
            }
            else{
            	finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void addZpo02BiospecimenCollection() {
        try{
        	Uri formUri;
			if(mZpo02 ==null){
	            //campos de proveedor de collect
	            String[] projection = new String[] {
	                    "_id","jrFormId","displayName"};
	            //cursor que busca el formulario

	            Cursor c = getContentResolver().query(Constants.CONTENT_URI, projection,
                        "jrFormId = 'zpov2_muestras' and displayName = 'Continuacion Estudio ZPO Recoleccion de muestra madre/infante'", null, null);
	            c.moveToFirst();
	            //captura el id del formulario
	            Integer id = Integer.parseInt(c.getString(0));
	            //cierra el cursor
	            if (c != null) {
	                c.close();
	            }
	            formUri = ContentUris.withAppendedId(Constants.CONTENT_URI, id);
	            accion = ADD_ZP02_ODK;
			}
			else{
				//forma el uri para la instancia en ODK Collect
				Integer id = mZpo02.getIdInstancia();
				formUri = ContentUris.withAppendedId(Constants.CONTENT_URI_I,id);
				accion = EDIT_ZP02_ODK;
			}
			Intent odkA =  new Intent(Intent.ACTION_EDIT,formUri);
			//Arranca la actividad proveedor de instancias de ODK Collect en busca de resultado
			startActivityForResult(odkA, accion);
        }
        catch(Exception e){
            //No existe el formulario en el equipo
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void parseZpo02BiospecimenCollection(Integer idInstancia, String instanceFilePath, Integer accion) {
        Serializer serializer = new Persister();
        File source = new File( instanceFilePath );

        try {
            ZpoV2RecoleccionMuestraXml zp02Xml = serializer.read( ZpoV2RecoleccionMuestraXml.class, source );

            if (accion == ADD_ZP02_ODK) mZpo02 = new ZpoV2RecoleccionMuestra();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            Date bloodMomSampleDate = null;
            Date bloodChildSampleDate = null;

            if (zp02Xml.getDate1() != null && zp02Xml.getTime1() != null) {
                bloodMomSampleDate = formatter.parse(zp02Xml.getDate1() + " "+ zp02Xml.getTime1().substring(0,8));

            }

            if (zp02Xml.getDate2() != null && zp02Xml.getTime2() != null) {
                bloodChildSampleDate = formatter.parse(zp02Xml.getDate2() + " "+ zp02Xml.getTime2().substring(0,8));
            }

            mZpo02.setRecordId( mRecordId );
            mZpo02.setEventName( event );
            mZpo02.setBloodTodaysDate(zp02Xml.getBloodTodaysDate());
            mZpo02.setBloodSampleCollected(zp02Xml.getBloodSampleCollected());
            mZpo02.setBloodWhichPerson(zp02Xml.getBloodWhichPerson());
            mZpo02.setBloodMomSampleDate(bloodMomSampleDate);
            mZpo02.setBloodMomTubes(zp02Xml.getBloodMomTubes());
            mZpo02.setBloodMomType(zp02Xml.getBloodMomType());
            mZpo02.setBloodChildSampleDate(bloodChildSampleDate);
            mZpo02.setBloodChildTubes(zp02Xml.getBloodChildTubes());
            mZpo02.setBloodChildType(zp02Xml.getBloodChildType());
            mZpo02.setBloodPersonnel(zp02Xml.getBloodPersonnel());

            mZpo02.setRecordDate( new Date() );
            mZpo02.setRecordUser( username );
            mZpo02.setIdInstancia( idInstancia );
            mZpo02.setInstancePath( instanceFilePath );
            mZpo02.setEstado( Constants.STATUS_NOT_SUBMITTED );
            mZpo02.setStart( zp02Xml.getStart() );
            mZpo02.setEnd( zp02Xml.getEnd() );
            mZpo02.setDeviceid( zp02Xml.getDeviceid() );
            mZpo02.setSimserial( zp02Xml.getSimserial() );
            mZpo02.setPhonenumber( zp02Xml.getPhonenumber() );
            mZpo02.setToday( zp02Xml.getToday() );

            new SaveDataTask().execute( accion );

        } catch (Exception e) {
            // Presenta el error al parsear el xml
            Toast.makeText( getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG ).show();
            e.printStackTrace();
            finish();
        }
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class SaveDataTask extends AsyncTask<Integer, Void, String> {
    	private Integer accionaRealizar = null;
        @Override
        protected void onPreExecute() {
            // before the request begins, show a progress indicator
            showLoadingProgressDialog();
        }

        @Override
        protected String doInBackground(Integer... values) {
        	accionaRealizar = values[0];
			try {
				zpoA.open();
				if (accionaRealizar == ADD_ZP02_ODK){
					zpoA.crearZpoV2RecoleccionMuestra(mZpo02);
				}
				else{
					zpoA.editarZpoV2RecoleccionMuestra(mZpo02);
				}
				zpoA.close();
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				return "error";
			}
			return "exito";
        }

        protected void onPostExecute(String resultado) {
            // after the network request completes, hide the progress indicator
            dismissProgressDialog();
            showResult(resultado);
        }

    }

    // ***************************************
    // Private methods
    // ***************************************
    private void showResult(String resultado) {
        Toast.makeText(getApplicationContext(),	resultado, Toast.LENGTH_LONG).show();
        finish();
    }
}
