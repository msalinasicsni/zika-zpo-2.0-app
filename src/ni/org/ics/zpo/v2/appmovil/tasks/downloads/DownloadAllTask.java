package ni.org.ics.zpo.v2.appmovil.tasks.downloads;

import android.content.Context;
import android.util.Log;
import ni.org.ics.zpo.v2.appmovil.database.ZpoAdapter;
import ni.org.ics.zpo.v2.appmovil.tasks.DownloadTask;
import ni.org.ics.zpo.v2.domain.*;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


public class DownloadAllTask extends DownloadTask {
	
	private final Context mContext;
	
	public DownloadAllTask(Context context) {
		mContext = context;
	}
	
	protected static final String TAG = DownloadAllTask.class.getSimpleName();
	private ZpoAdapter zpoA = null;
    private static final String TOTAL_TASK = "22";

    private List<Zpo00Screening> mTamizajes = null;
    private List<ZpoEstadoEmbarazada> mStatus = null;
    private List<ZpoInfantData> mInfantData = null;
    private List<ZpoEstadoInfante> mEstadoInfante = null;

    public static final int TAMIZAJE = 1;
    public static final int ESTADO = 2;
    public static final int DAT_INFANTE = 3;
    public static final int ESTADO_INFANTE = 4;
    public static final int INGRESO1 = 5;
    public static final int INGRESO2 = 6;
    public static final int INGRESO3 = 7;
    public static final int EXTENDED1 = 8;
    public static final int EXTENDED2 = 9;
    public static final int EXTENDED3 = 10;
    public static final int PARTO = 11;
    public static final int EVAL_INFANTE = 12;
    public static final int OPHTH_RESULTS = 13;
    public static final int AUDIO_RESULTS = 14;
    public static final int IMAGE_STUDIES = 15;
    public static final int BAYLEY_SCALES = 16;
    public static final int MUESTRAS = 17;
    public static final int CONSSAL = 18;
    public static final int CONSREC = 19;
    public static final int SALIDA = 20;
    public static final int VISITA_FALL = 21;
    public static final int OTOEMI = 22;
    public static final int EXTENDEDAF = 23;
    
	private String error = null;
	private String url = null;
	private String username = null;
	private String password = null;
    private int v =0;
	
	@Override
	protected String doInBackground(String... values) {
		url = values[0];
		username = values[1];
		password = values[2];

		try {
			error = descargarDatos();
			if (error!=null) return error;
		} catch (Exception e) {
			// Regresa error al descargar
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		publishProgress("Abriendo base de datos...","1","1");
		zpoA = new ZpoAdapter(mContext, password, false,false);
		zpoA.open();
        //Borrar los datos de la base de datos
        zpoA.borrarZpo00Screening();
        zpoA.borrarZpoEstadoMadre();
        zpoA.borrarZpoControlConsentimientosSalida();
        zpoA.borrarZpoControlConsentimientosRecepcion();
        zpoA.borrarZpoVisitaFallida();
        zpoA.borrarZpoInfantData();
        zpoA.borrarZpoEstadoInfante();

        try {

            if (mTamizajes != null){
                v = mTamizajes.size();
                ListIterator<Zpo00Screening> iter = mTamizajes.listIterator();
                while (iter.hasNext()){
                    zpoA.crearZpo00Screening(iter.next());
                    publishProgress("Insertando tamizajes en la base de datos...", Integer.valueOf(iter.nextIndex()).toString(), Integer
                            .valueOf(v).toString());
                }
            }
            if (mStatus != null){
                v = mStatus.size();
                ListIterator<ZpoEstadoEmbarazada> iter = mStatus.listIterator();
                while (iter.hasNext()){
                    zpoA.crearZpoEstadoMadre(iter.next());
                    publishProgress("Insertando estado de las madres en la base de datos...", Integer.valueOf(iter.nextIndex()).toString(), Integer
                            .valueOf(v).toString());
                }
            }
            if (mInfantData != null){
                v = mInfantData.size();
                ListIterator<ZpoInfantData> iter = mInfantData.listIterator();
                while (iter.hasNext()){
                    zpoA.crearZpoInfantData(iter.next());
                    publishProgress("Insertando datos de infantes en la base de datos...", Integer.valueOf(iter.nextIndex()).toString(), Integer
                            .valueOf(v).toString());
                }
            }
            if (mEstadoInfante != null){
                v = mEstadoInfante.size();
                ListIterator<ZpoEstadoInfante> iter = mEstadoInfante.listIterator();
                while (iter.hasNext()){
                    zpoA.crearZpoEstadoInfante(iter.next());
                    publishProgress("Insertando datos de estado de infantes en la base de datos...", Integer.valueOf(iter.nextIndex()).toString(), Integer
                            .valueOf(v).toString());
                }
            }
        } catch (Exception e) {
            // Regresa error al insertar
            e.printStackTrace();
            zpoA.close();
            return e.getLocalizedMessage();
        }
		zpoA.close();
		return error;
	}

    // url, username, password
    protected String descargarDatos() throws Exception {
        try {
            // The URL for making the GET request
            String urlRequest;
            // Set the Accept header for "application/json"
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            requestHeaders.setAuthorization(authHeader);
            // Populate the headers in an HttpEntity object to use for the request
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            //Descargar tamizajes
            urlRequest = url + "/movil/zpo00Screenings/{username}";
            publishProgress("Solicitando tamizajes",String.valueOf(TAMIZAJE),TOTAL_TASK);
            // Perform the HTTP GET request
            ResponseEntity<Zpo00Screening[]> responseEntityZpo00Screening = restTemplate.exchange(urlRequest, HttpMethod.GET, requestEntity,
                    Zpo00Screening[].class, username);
            // convert the array to a list and return it
            mTamizajes = Arrays.asList(responseEntityZpo00Screening.getBody());

            //Descargar estado de embarazadas
            urlRequest = url + "/movil/zpoEstadoEmb/{username}";
            publishProgress("Solicitando estado de embarazadas",String.valueOf(ESTADO),TOTAL_TASK);
            // Perform the HTTP GET request
            ResponseEntity<ZpoEstadoEmbarazada[]> responseZpoEstadoEmbarazada = restTemplate.exchange(urlRequest, HttpMethod.GET, requestEntity,
                    ZpoEstadoEmbarazada[].class, username);
            // convert the array to a list and return it
            mStatus = Arrays.asList(responseZpoEstadoEmbarazada.getBody());

            //Descargar datos de infantes
            urlRequest = url + "/movil/zpoInfants/{username}";
            publishProgress("Solicitando datos de infantes",String.valueOf(DAT_INFANTE),TOTAL_TASK);
            // Perform the HTTP GET request
            ResponseEntity<ZpoInfantData[]> responseZpoInfantData = restTemplate.exchange(urlRequest, HttpMethod.GET, requestEntity,
                    ZpoInfantData[].class, username);
            // convert the array to a list and return it
            mInfantData = Arrays.asList(responseZpoInfantData.getBody());

            //Descargar datos de estado de infantes
            urlRequest = url + "/movil/zpoEstadoInfantes/{username}";
            publishProgress("Solicitando datos de estado de infantes",String.valueOf(ESTADO_INFANTE),TOTAL_TASK);
            // Perform the HTTP GET request
            ResponseEntity<ZpoEstadoInfante[]> responseZpoEstadoInfante = restTemplate.exchange(urlRequest, HttpMethod.GET, requestEntity,
                    ZpoEstadoInfante[].class, username);
            // convert the array to a list and return it
            mEstadoInfante = Arrays.asList(responseZpoEstadoInfante.getBody());
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return e.getLocalizedMessage();
        }
    }
}